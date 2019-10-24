package github.tornaco.android.thanos.services.profile

import android.app.PendingIntent
import android.content.Intent
import android.os.IBinder
import android.os.UserHandle
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.core.Res
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.app.AppResources
import github.tornaco.android.thanos.core.compat.NotificationCompat
import github.tornaco.android.thanos.core.compat.NotificationManagerCompat
import github.tornaco.android.thanos.core.pref.IPrefChangeListener
import github.tornaco.android.thanos.core.profile.IProfileManager
import github.tornaco.android.thanos.core.profile.ProfileManager
import github.tornaco.android.thanos.core.util.Noop
import github.tornaco.android.thanos.core.util.OsUtils
import github.tornaco.android.thanos.core.util.PkgUtils
import github.tornaco.android.thanos.core.util.Timber
import github.tornaco.android.thanos.services.BackgroundThread
import github.tornaco.android.thanos.services.S
import github.tornaco.android.thanos.services.SystemService
import github.tornaco.android.thanos.services.n.NotificationHelper
import github.tornaco.android.thanos.services.n.NotificationIdFactory
import github.tornaco.android.thanos.services.n.SystemUI
import github.tornaco.android.thanos.services.pm.PackageMonitor
import github.tornaco.java.common.util.ObjectsUtils
import java.util.*

class ProfileService(private val s: S) : SystemService(), IProfileManager {
    private val notificationHelper: NotificationHelper = NotificationHelper()

    private var autoApplyForNewInstalledAppsEnabled = false

    private val monitor = object : PackageMonitor() {
        override fun onPackageAdded(packageName: String, uid: Int) {
            super.onPackageAdded(packageName, uid)
            Timber.v("onPackageAdded: %s", packageName)
            executeInternal(Runnable {
                setupAutoConfigForNewInstalledAppsIfNeed(packageName)
            }, 6 * 1000 /* Delay to make it safe.*/)
        }
    }

    override fun systemReady() {
        super.systemReady()
        registerReceivers()
        initPrefs()
    }

    override fun shutdown() {
        super.shutdown()
        monitor.unregister()
    }

    private fun initPrefs() {
        readPrefs()
        listenToPrefs()
    }

    private fun registerReceivers() {
        monitor.register(context, UserHandle.CURRENT, true, BackgroundThread.getHandler())
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
                if (ObjectsUtils.equals(T.Settings.PREF_AUTO_CONFIG_NEW_INSTALLED_APPS_ENABLED.key, key)) {
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

        if (s.pkgManagerService.getAppInfo(pkg) == null) {
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

    override fun asBinder(): IBinder {
        return Noop.notSupported()
    }

}
