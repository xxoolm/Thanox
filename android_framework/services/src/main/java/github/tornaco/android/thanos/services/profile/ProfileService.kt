package github.tornaco.android.thanos.services.profile

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.os.UserHandle
import android.util.Log
import com.google.common.collect.Sets
import com.google.common.io.Files
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.core.Res
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.app.AppResources
import github.tornaco.android.thanos.core.app.event.IEventSubscriber
import github.tornaco.android.thanos.core.app.event.ThanosEvent
import github.tornaco.android.thanos.core.compat.NotificationCompat
import github.tornaco.android.thanos.core.compat.NotificationManagerCompat
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pref.IPrefChangeListener
import github.tornaco.android.thanos.core.profile.IProfileManager
import github.tornaco.android.thanos.core.profile.IRuleAddCallback
import github.tornaco.android.thanos.core.profile.ProfileManager
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager
import github.tornaco.android.thanos.core.util.Noop
import github.tornaco.android.thanos.core.util.OsUtils
import github.tornaco.android.thanos.core.util.PkgUtils
import github.tornaco.android.thanos.core.util.Timber
import github.tornaco.android.thanos.core.util.collection.ArrayMap
import github.tornaco.android.thanos.services.BackgroundThread
import github.tornaco.android.thanos.services.S
import github.tornaco.android.thanos.services.ThanosSchedulers
import github.tornaco.android.thanos.services.ThanoxSystemService
import github.tornaco.android.thanos.services.app.EventBus
import github.tornaco.android.thanos.services.n.NotificationHelper
import github.tornaco.android.thanos.services.n.NotificationIdFactory
import github.tornaco.android.thanos.services.n.SystemUI
import github.tornaco.android.thanos.services.pm.PackageMonitor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.Rules
import org.jeasy.rules.api.RulesEngine
import org.jeasy.rules.core.DefaultRulesEngine
import org.jeasy.rules.mvel.MVELRuleFactory
import org.jeasy.rules.support.JsonRuleDefinitionReader
import util.ObjectsUtils
import java.io.File
import java.io.StringReader
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit

class ProfileService(s: S) : ThanoxSystemService(s), IProfileManager {

    private val notificationHelper: NotificationHelper = NotificationHelper()

    private var autoApplyForNewInstalledAppsEnabled = false

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val rulesMapping: ArrayMap<String, Rule> = ArrayMap()
    private val rules: Rules = Rules(Sets.newHashSet())
    private val rulesEngine: RulesEngine = DefaultRulesEngine()

    private val monitor = object : PackageMonitor() {
        override fun onPackageAdded(packageName: String, uid: Int) {
            super.onPackageAdded(packageName, uid)
            Timber.v("onPackageAdded: %s", packageName)

            val disposable = Observable.just(packageName).delay(8, TimeUnit.SECONDS)
                .observeOn(ThanosSchedulers.serverThread())
                .observeOn(ThanosSchedulers.serverThread())
                .subscribe {
                    setupAutoConfigForNewInstalledAppsIfNeed(packageName)

                    val pkgFacts = Facts()
                    pkgFacts.put("pkgAdd", packageName)
                    publishFacts(pkgFacts)
                }
            compositeDisposable.add(disposable)
        }
    }

    private val frontEventSubscriber = object : IEventSubscriber.Stub() {
        override fun onEvent(e: ThanosEvent) {
            val intent = e.intent
            val to = intent.getStringExtra(T.Actions.ACTION_FRONT_PKG_CHANGED_EXTRA_PACKAGE_TO)

            val pkgFacts = Facts()
            pkgFacts.put("frontPkg", to)
            pkgFacts.put("frontPkgChanged", true)
            publishFacts(pkgFacts)
        }
    }

    override fun systemReady() {
        super.systemReady()
        registerReceivers()
        initPrefs()
        registerRules()
    }

    override fun shutdown() {
        super.shutdown()
        monitor.unregister()
        compositeDisposable.clear()
    }

    private fun initPrefs() {
        readPrefs()
        listenToPrefs()
    }

    private fun registerReceivers() {
        monitor.register(context, UserHandle.CURRENT, true, BackgroundThread.getHandler())
        EventBus.getInstance().registerEventSubscriber(
            IntentFilter(T.Actions.ACTION_FRONT_PKG_CHANGED),
            frontEventSubscriber
        )
    }

    private fun registerRules() {
        rules.clear()
        rulesMapping.clear()
        rulesMapping.putAll(LocalRuleScanner().getRulesUnder(T.profileRulesDir()))
        rulesMapping.forEach {
            Timber.v("Register rule: ${it.key}")
            rules.register(it.value)
        }
    }

    private fun readPrefs() {
        val preferenceManagerService = s.preferenceManagerService
        this.autoApplyForNewInstalledAppsEnabled = preferenceManagerService.getBoolean(
            T.Settings.PREF_AUTO_CONFIG_NEW_INSTALLED_APPS_ENABLED.key,
            T.Settings.PREF_AUTO_CONFIG_NEW_INSTALLED_APPS_ENABLED.defaultValue
        )
    }

    private fun listenToPrefs() {
        val listener = object : IPrefChangeListener.Stub() {
            override fun onPrefChanged(key: String) {
                if (ObjectsUtils.equals(
                        T.Settings.PREF_AUTO_CONFIG_NEW_INSTALLED_APPS_ENABLED.key,
                        key
                    )
                ) {
                    Timber.i("Pref changed, reload.")
                    readPrefs()
                }
            }
        }
        s.preferenceManagerService.registerSettingsChangeListener(listener)
    }

    override fun setAutoApplyForNewInstalledAppsEnabled(enable: Boolean) {
        autoApplyForNewInstalledAppsEnabled = enable

        val preferenceManagerService = s.preferenceManagerService
        preferenceManagerService.putBoolean(
            T.Settings.PREF_AUTO_CONFIG_NEW_INSTALLED_APPS_ENABLED.key,
            enable
        )
    }

    override fun isAutoApplyForNewInstalledAppsEnabled(): Boolean {
        return autoApplyForNewInstalledAppsEnabled
    }

    private fun setupAutoConfigForNewInstalledAppsIfNeed(pkg: String) {
        Timber.v("setupAutoConfigForNewInstalledAppsIfNeed: %s", pkg)
        if (!autoApplyForNewInstalledAppsEnabled) return

        val appInfo: AppInfo? = s.pkgManagerService.getAppInfo(pkg)

        if (appInfo == null) {
            Timber.e("setupAutoConfigForNewInstalledAppsIfNeed app not installed!")
            return
        }

        s.activityManagerService.setPkgBgRestrictEnabled(
            pkg,
            s.activityManagerService.isPkgBgRestricted(ProfileManager.PROFILE_AUTO_APPLY_NEW_INSTALLED_APPS_CONFIG_PKG_NAME)
        )
        s.activityManagerService.setPkgStartBlockEnabled(
            pkg,
            s.activityManagerService.isPkgStartBlocking(ProfileManager.PROFILE_AUTO_APPLY_NEW_INSTALLED_APPS_CONFIG_PKG_NAME)
        )
        s.activityManagerService.setPkgCleanUpOnTaskRemovalEnabled(
            pkg,
            s.activityManagerService.isPkgCleanUpOnTaskRemovalEnabled(ProfileManager.PROFILE_AUTO_APPLY_NEW_INSTALLED_APPS_CONFIG_PKG_NAME)
        )
        s.activityManagerService.setPkgRecentTaskBlurEnabled(
            pkg,
            s.activityManagerService.isPkgRecentTaskBlurEnabled(ProfileManager.PROFILE_AUTO_APPLY_NEW_INSTALLED_APPS_CONFIG_PKG_NAME)
        )
        s.privacyService.setPkgPrivacyDataCheat(
            pkg,
            s.privacyService.isPkgPrivacyDataCheat(ProfileManager.PROFILE_AUTO_APPLY_NEW_INSTALLED_APPS_CONFIG_PKG_NAME)
        )
        s.appOpsService.setPkgOpRemindEnable(
            pkg,
            s.appOpsService.isPkgOpRemindEnable(ProfileManager.PROFILE_AUTO_APPLY_NEW_INSTALLED_APPS_CONFIG_PKG_NAME)
        )
        s.notificationManagerService.setScreenOnNotificationEnabledForPkg(
            pkg,
            s.notificationManagerService.isScreenOnNotificationEnabledForPkg(ProfileManager.PROFILE_AUTO_APPLY_NEW_INSTALLED_APPS_CONFIG_PKG_NAME)
        )
        s.activityManagerService.setPkgSmartStandByEnabled(
            pkg,
            s.activityManagerService.isPkgSmartStandByEnabled(ProfileManager.PROFILE_AUTO_APPLY_NEW_INSTALLED_APPS_CONFIG_PKG_NAME)
        )

        // Set ops.
        val numOp = AppOpsManager._NUM_OP
        for (i in 0 until numOp) {
            val templateMode = s.appOpsService.checkOperation(
                i,
                -1,
                ProfileManager.PROFILE_AUTO_APPLY_NEW_INSTALLED_APPS_CONFIG_PKG_NAME
            )
            if (templateMode != AppOpsManager.MODE_IGNORED) {
                Timber.d("Op $i is not MODE_IGNORED, no need to set...")
                continue
            }
            Timber.v("Set op by template: $i, mode: $templateMode")
            try {
                s.appOpsService.setMode(i, appInfo.uid, appInfo.pkgName, templateMode)
            } catch (e: Throwable) {
                Timber.e(e, "Fail set mode $templateMode for app $appInfo")
            }
        }

        Timber.d("setupAutoConfigForNewInstalledApps done: %s", pkg)

        showAutoConfigAppNotification(pkg)
    }

    private fun showAutoConfigAppNotification(pkg: String) {
        if (!isNotificationPostReady) return

        Timber.v("Will show showAutoConfigAppNotification for pkg: %s", pkg)

        // For oreo.
        notificationHelper.createSilenceNotificationChannel(context!!)

        val builder = NotificationCompat.Builder(
            context,
            T.serviceSilenceNotificationChannel()
        )

        val appResource = AppResources(context, BuildProp.THANOS_APP_PKG_NAME)

        SystemUI.overrideNotificationAppName(
            context!!, builder,
            appResource.getString(Res.Strings.STRING_SERVICE_NOTIFICATION_OVERRIDE_THANOS)
        )

        val appLabel = PkgUtils.loadNameByPkgName(context, pkg)

        val viewer = Intent()
        viewer.setPackage(BuildProp.THANOS_APP_PKG_NAME)
        viewer.setClassName(
            BuildProp.THANOS_APP_PKG_NAME,
            BuildProp.ACTIVITY_APP_DETAILS
        )

        val appInfo = s.pkgManagerService.getAppInfo(pkg)
        viewer.putExtra("app", appInfo)
        viewer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        // TODO Extract string res.
        val n = builder
            .setContentTitle("自动配置")
            .setContentText("${appLabel}已经自动启用模板配置")
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    NotificationIdFactory.getIdByTag(UUID.randomUUID().toString()),
                    viewer,
                    0
                )
            )
            .build()

        if (OsUtils.isMOrAbove()) {
            n.smallIcon = appResource.getIcon(Res.Drawables.DRAWABLE_CHECK_DOUBLE_FILL)
        }

        NotificationManagerCompat.from(context)
            .notify(NotificationIdFactory.getIdByTag(UUID.randomUUID().toString()), n)
    }

    @Suppress("UnstableApiUsage")
    override fun addRule(id: String?, ruleJson: String?, callback: IRuleAddCallback?) {
        enforceCallingPermissions()
        Timber.v("addRule: $id, $ruleJson")
        Objects.requireNonNull(id, "Rule is null")
        Objects.requireNonNull(ruleJson, "Rule id is null")
        executeInternal(Runnable {
            try {
                val ruleFactory = MVELRuleFactory(JsonRuleDefinitionReader())
                val rule = ruleFactory.createRule(StringReader(ruleJson!!))
                if (rule != null) {
                    rulesMapping[id] = rule
                    // Persist.
                    val f = File(T.profileRulesDir(), "$id.r")
                    Files.createParentDirs(f)
                    Files.asByteSink(f)
                        .asCharSink(Charset.defaultCharset())
                        .write(ruleJson)
                    callback?.onRuleAddSuccess()
                } else {
                    callback?.onRuleAddFail(0, "Create rule fail with unknown error.")
                }
            } catch (e: Exception) {
                Timber.e(e, "createRule: $rulesEngine")
                callback?.onRuleAddFail(0, Log.getStackTraceString(e))
            }
        })
    }

    override fun deleteRule(ruleId: String?) {

    }

    override fun setRuleEnabled(ruleId: String?, enable: Boolean) {
        val rule = rulesMapping[ruleId]
        if (rule != null) rules.register(rule)
    }

    override fun isRuleEnabled(ruleId: String?) {
    }

    fun publishFacts(facts: Facts) {
        rulesEngine.fire(rules, injectThanoxServiceToFacts(facts))
    }

    private fun injectThanoxServiceToFacts(facts: Facts): Facts {
        facts.put("thanox", ThanoxImpl(s, context))
        return facts
    }

    override fun asBinder(): IBinder {
        return Noop.notSupported()
    }
}
