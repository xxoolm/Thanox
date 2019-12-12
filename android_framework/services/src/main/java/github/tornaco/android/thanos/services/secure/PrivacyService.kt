package github.tornaco.android.thanos.services.secure

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.location.Location
import android.os.IBinder
import android.provider.Settings
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.core.Res
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.T.Tags.N_TAG_PKG_PRIVACY_DATA_CHEATING
import github.tornaco.android.thanos.core.app.AppResources
import github.tornaco.android.thanos.core.app.event.IEventSubscriber
import github.tornaco.android.thanos.core.app.event.ThanosEvent
import github.tornaco.android.thanos.core.compat.NotificationCompat
import github.tornaco.android.thanos.core.compat.NotificationManagerCompat
import github.tornaco.android.thanos.core.persist.RepoFactory
import github.tornaco.android.thanos.core.persist.StringMapRepo
import github.tornaco.android.thanos.core.persist.StringSetRepo
import github.tornaco.android.thanos.core.pm.PackageManager
import github.tornaco.android.thanos.core.pref.IPrefChangeListener
import github.tornaco.android.thanos.core.secure.IPrivacyManager
import github.tornaco.android.thanos.core.util.*
import github.tornaco.android.thanos.services.S
import github.tornaco.android.thanos.services.ThanosSchedulers
import github.tornaco.android.thanos.services.ThanoxSystemService
import github.tornaco.android.thanos.services.app.EventBus
import github.tornaco.android.thanos.services.n.NotificationHelper
import github.tornaco.android.thanos.services.n.NotificationIdFactory
import github.tornaco.android.thanos.services.n.SystemUI
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import util.ObjectsUtils
import java.util.concurrent.TimeUnit

class PrivacyService(s: S) : ThanoxSystemService(s), IPrivacyManager {

    private val notificationHelper: NotificationHelper = NotificationHelper()

    private lateinit var pkgDeviceIdRepo: StringMapRepo
    private lateinit var pkgSimNumRepo: StringMapRepo
    private lateinit var pkgLine1NumRepo: StringMapRepo
    private lateinit var pkgAndroidIdRepo: StringMapRepo
    private lateinit var pkgImeiRepo: StringMapRepo
    private lateinit var pkgMeidRepo: StringMapRepo

    private lateinit var privacyDataCheatPkgRepo: StringSetRepo

    private var privacyEnabled = false
    private var privacyNotificationEnabled = false

    private var privacyRequestHandleTimes = 0L

    private val frontEventSubscriber = object : IEventSubscriber.Stub() {
        override fun onEvent(e: ThanosEvent) {
            val intent = e.intent
            val to = intent.getStringExtra(T.Actions.ACTION_FRONT_PKG_CHANGED_EXTRA_PACKAGE_TO)
            onFrontPkgChanged(to)
        }
    }

    override fun onStart(context: Context) {
        super.onStart(context)

        pkgDeviceIdRepo = RepoFactory.get().getOrCreateStringMapRepo(T.privacyDeviceIdFile().path)
        pkgLine1NumRepo = RepoFactory.get().getOrCreateStringMapRepo(T.privacyLine1NumFile().path)
        pkgSimNumRepo = RepoFactory.get().getOrCreateStringMapRepo(T.privacySimNumFile().path)
        pkgAndroidIdRepo = RepoFactory.get().getOrCreateStringMapRepo(T.privacyAndroidIdFile().path)
        pkgImeiRepo = RepoFactory.get().getOrCreateStringMapRepo(T.privacyImeiFile().path)
        pkgMeidRepo = RepoFactory.get().getOrCreateStringMapRepo(T.privacyMeidFile().path)

        privacyDataCheatPkgRepo =
            RepoFactory.get().getOrCreateStringSetRepo(T.privacyPkgSettingsFile().path)
    }

    override fun systemReady() {
        super.systemReady()
        registerReceivers()
        initPrefs()
    }

    private fun initPrefs() {
        readPrefs()
        listenToPrefs()
    }

    private fun readPrefs() {
        val preferenceManagerService = s.preferenceManagerService
        this.privacyEnabled = preferenceManagerService.getBoolean(
            T.Settings.PREF_PRIVACY_ENABLED.key,
            T.Settings.PREF_PRIVACY_ENABLED.defaultValue
        )
        this.privacyNotificationEnabled = preferenceManagerService.getBoolean(
            T.Settings.PREF_PRIVACY_N_ENABLED.key,
            T.Settings.PREF_PRIVACY_N_ENABLED.defaultValue
        )

    }

    private fun listenToPrefs() {
        val listener = object : IPrefChangeListener.Stub() {
            override fun onPrefChanged(key: String) {
                if (ObjectsUtils.equals(T.Settings.PREF_PRIVACY_ENABLED.key, key)
                    || ObjectsUtils.equals(T.Settings.PREF_PRIVACY_N_ENABLED.key, key)
                ) {
                    Timber.i("Pref changed, reload.")
                    readPrefs()
                }
            }
        }
        s.preferenceManagerService.registerSettingsChangeListener(listener)
    }

    override fun isPrivacyEnabled(): Boolean {
        return privacyEnabled
    }

    override fun setPrivacyEnabled(enabled: Boolean) {
        privacyEnabled = enabled

        val preferenceManagerService = s.preferenceManagerService
        preferenceManagerService.putBoolean(
            T.Settings.PREF_PRIVACY_ENABLED.key,
            enabled
        )
    }

    override fun isPrivacyNotificationEnabled(): Boolean {
        return privacyNotificationEnabled
    }

    override fun setPrivacyNotificationEnabled(enabled: Boolean) {
        privacyNotificationEnabled = enabled

        val preferenceManagerService = s.preferenceManagerService
        preferenceManagerService.putBoolean(
            T.Settings.PREF_PRIVACY_N_ENABLED.key,
            enabled
        )
    }

    override fun isUidPrivacyDataCheat(uid: Int): Boolean {
        val pkgArr = s.pkgManagerService.getPkgNameForUid(uid)
        if (pkgArr == null) {
            Timber.e("Can not find pkg for uid: $uid")
            return false
        }
        for (pkg in pkgArr) {
            if (isPkgPrivacyDataCheat(pkg)) return true
        }
        return false
    }

    override fun isPkgPrivacyDataCheat(pkg: String): Boolean {
        return if (s.pkgManagerService.isPkgInWhiteList(pkg)
            || PackageManager.packageNameOfAndroid() == pkg
            || PackageManager.packageNameOfPhone() == pkg
            || PackageManager.packageNameOfTelecom() == pkg
        ) {
            false
        } else {
            privacyDataCheatPkgRepo.has(pkg)
        }
    }

    override fun setPkgPrivacyDataCheat(pkg: String, enable: Boolean) {
        if (enable) privacyDataCheatPkgRepo.add(pkg) else privacyDataCheatPkgRepo.remove(pkg)
    }

    override fun getCheatedDeviceIdForPkg(pkg: String): String? {
        privacyRequestHandleTimes++
        val useSet = pkgDeviceIdRepo[pkg]
        if (!TextUtils.isEmpty(useSet)) {
            return useSet
        }
        val allSet = pkgDeviceIdRepo["*"]
        if (!TextUtils.isEmpty(allSet)) {
            return allSet
        }
        return null
    }

    override fun getCheatedLine1NumberForPkg(pkg: String): String? {
        privacyRequestHandleTimes++
        val useSet = pkgLine1NumRepo[pkg]
        if (!TextUtils.isEmpty(useSet)) {
            return useSet
        }
        val allSet = pkgLine1NumRepo["*"]
        if (!TextUtils.isEmpty(allSet)) {
            return allSet
        }
        return null
    }

    override fun getCheatedSimSerialNumberForPkg(pkg: String): String? {
        privacyRequestHandleTimes++
        val useSet = pkgSimNumRepo[pkg]
        if (!TextUtils.isEmpty(useSet)) {
            return useSet
        }
        val allSet = pkgSimNumRepo["*"]
        if (!TextUtils.isEmpty(allSet)) {
            return allSet
        }
        return null
    }

    override fun getCheatedAndroidIdForPkg(pkg: String?): String? {
        privacyRequestHandleTimes++
        val useSet = pkgAndroidIdRepo[pkg]
        if (!TextUtils.isEmpty(useSet)) {
            return useSet
        }
        val allSet = pkgAndroidIdRepo["*"]
        if (!TextUtils.isEmpty(allSet)) {
            return allSet
        }
        return null
    }

    override fun getCheatedImeiForPkg(pkg: String?, slotIndex: Int): String? {
        privacyRequestHandleTimes++
        val useSet = pkgImeiRepo["${pkg}_$slotIndex"]
        if (!TextUtils.isEmpty(useSet)) {
            return useSet
        }
        val allSet = pkgImeiRepo["*_$slotIndex"]
        if (!TextUtils.isEmpty(allSet)) {
            return allSet
        }
        return null
    }

    override fun getCheatedMeidForPkg(pkg: String?, slotIndex: Int): String? {
        privacyRequestHandleTimes++
        val useSet = pkgMeidRepo["${pkg}_$slotIndex"]
        if (!TextUtils.isEmpty(useSet)) {
            return useSet
        }
        val allSet = pkgMeidRepo["*_$slotIndex"]
        if (!TextUtils.isEmpty(allSet)) {
            return allSet
        }
        return null
    }

    override fun getCheatedLocationForPkg(pkg: String?, actual: Location?): Location {
        privacyRequestHandleTimes++
        val res = Location(actual)
        res.latitude = 99.0
        res.longitude = 99.0
        res.accuracy = 1f
        return res
    }

    override fun setCheatedDeviceIdForPkg(pkg: String, deviceId: String) {
        enforceCallingPermissions()
        pkgDeviceIdRepo[pkg] = deviceId
    }

    override fun setCheatedLine1NumberForPkg(pkg: String, num: String) {
        enforceCallingPermissions()
        pkgLine1NumRepo[pkg] = num
    }

    override fun setCheatedSimSerialNumberForPkg(pkg: String, num: String) {
        enforceCallingPermissions()
        pkgSimNumRepo[pkg] = num
    }

    override fun setCheatedAndroidIdForPkg(pkg: String?, id: String?) {
        enforceCallingPermissions()
        pkgAndroidIdRepo[pkg] = id
    }

    override fun setCheatedImeiForPkg(pkg: String?, id: String?, slotIndex: Int) {
        enforceCallingPermissions()
        pkgImeiRepo["${pkg}_$slotIndex"] = id
    }

    override fun setCheatedMeidForPkg(pkg: String?, id: String?, slotIndex: Int) {
        enforceCallingPermissions()
        pkgMeidRepo["${pkg}_$slotIndex"] = id
    }

    @SuppressLint("HardwareIds")
    override fun getOriginalDeviceId(): String {
        enforceCallingPermissions()
        return Single
            .create(SingleOnSubscribe<String> { emitter ->
                emitter.onSuccess(
                    Optional.ofNullable(TelephonyManager.from(context).deviceId).orElse(
                        NPEFixing.emptyString()
                    )
                )
            })
            .subscribeOn(ThanosSchedulers.serverThread())
            .timeout(300, TimeUnit.MILLISECONDS)
            .onErrorReturnItem(NPEFixing.emptyString())
            .blockingGet()
    }

    @SuppressLint("HardwareIds")
    override fun getOriginalLine1Number(): String {
        enforceCallingPermissions()
        return Single
            .create(SingleOnSubscribe<String> { emitter ->
                emitter.onSuccess(
                    Optional.ofNullable(TelephonyManager.from(context).line1Number).orElse(
                        NPEFixing.emptyString()
                    )
                )
            })
            .subscribeOn(ThanosSchedulers.serverThread())
            .timeout(300, TimeUnit.MILLISECONDS)
            .onErrorReturnItem(NPEFixing.emptyString())
            .blockingGet()
    }

    @SuppressLint("HardwareIds")
    override fun getOriginalSimSerialNumber(): String {
        enforceCallingPermissions()
        return Single
            .create(SingleOnSubscribe<String> { emitter ->
                emitter.onSuccess(
                    Optional.ofNullable(TelephonyManager.from(context).simSerialNumber).orElse(
                        NPEFixing.emptyString()
                    )
                )
            })
            .subscribeOn(ThanosSchedulers.serverThread())
            .timeout(300, TimeUnit.MILLISECONDS)
            .onErrorReturnItem(NPEFixing.emptyString())
            .blockingGet()
    }

    @SuppressLint("HardwareIds")
    override fun getOriginalAndroidId(): String {
        enforceCallingPermissions()
        return Single
            .create(SingleOnSubscribe<String> { emitter ->
                emitter.onSuccess(
                    Optional.ofNullable(
                        Settings.Secure
                            .getString(context!!.contentResolver, Settings.Secure.ANDROID_ID)
                    ).orElse(
                        NPEFixing.emptyString()
                    )
                )
            })
            .subscribeOn(ThanosSchedulers.serverThread())
            .timeout(300, TimeUnit.MILLISECONDS)
            .onErrorReturnItem(NPEFixing.emptyString())
            .blockingGet()
    }

    // It is hidden API before 26
    @SuppressLint("NewApi")
    override fun getOriginalImei(slotIndex: Int): String {
        enforceCallingPermissions()
        return Single
            .create(SingleOnSubscribe<String> { emitter ->
                emitter.onSuccess(
                    Optional.ofNullable(TelephonyManager.from(context).getImei(slotIndex)).orElse(
                        NPEFixing.emptyString()
                    )
                )
            })
            .subscribeOn(ThanosSchedulers.serverThread())
            .timeout(300, TimeUnit.MILLISECONDS)
            .onErrorReturnItem(NPEFixing.emptyString())
            .blockingGet()
    }

    // It is hidden API before 26
    @SuppressLint("NewApi")
    override fun getOriginalMeid(slotIndex: Int): String {
        enforceCallingPermissions()
        return Single
            .create(SingleOnSubscribe<String> { emitter ->
                emitter.onSuccess(
                    Optional.ofNullable(TelephonyManager.from(context).getMeid(slotIndex)).orElse(
                        NPEFixing.emptyString()
                    )
                )
            })
            .subscribeOn(ThanosSchedulers.serverThread())
            .timeout(300, TimeUnit.MILLISECONDS)
            .onErrorReturnItem(NPEFixing.emptyString())
            .blockingGet()
    }

    /**
     * Returns the number of phones available.
     * Returns 0 if none of voice, sms, data is not supported
     * Returns 1 for Single standby mode (Single SIM functionality)
     * Returns 2 for Dual standby mode.(Dual SIM functionality)
     * Returns 3 for Tri standby mode.(Tri SIM functionality)
     */
    override fun getPhoneCount(): Int {
        enforceCallingPermissions()
        return Single
            .create(SingleOnSubscribe<Int> { emitter ->
                emitter.onSuccess(
                    Optional.ofNullable(TelephonyManager.from(context).phoneCount).orElse(
                        0
                    )
                )
            })
            .subscribeOn(ThanosSchedulers.serverThread())
            .timeout(300, TimeUnit.MILLISECONDS)
            .onErrorReturnItem(0)
            .blockingGet()
    }

    /**
     * Gets the SubscriptionInfo(s) of all embedded subscriptions accessible to the calling app, if
     * any.
     *
     * <p>Only those subscriptions for which the calling app has carrier privileges per the
     * subscription metadata, if any, will be included in the returned list.
     *
     * <p>The records will be sorted by {@link SubscriptionInfo#getSimSlotIndex} then by
     * {@link SubscriptionInfo#getSubscriptionId}.
     *
     * @return Sorted list of the current embedded {@link SubscriptionInfo} records available on the
     * device which are accessible to the caller.
     * <ul>
     * <li>
     * If null is returned the current state is unknown but if a
     * {@link OnSubscriptionsChangedListener} has been registered
     * {@link OnSubscriptionsChangedListener#onSubscriptionsChanged} will be invoked in the future.
     * <li>
     * If the list is empty then there are no {@link SubscriptionInfo} records currently available.
     * <li>
     * if the list is non-empty the list is sorted by {@link SubscriptionInfo#getSimSlotIndex}
     * then by {@link SubscriptionInfo#getSubscriptionId}.
     * </ul>
     */
    override fun getAccessibleSubscriptionInfoList(): Array<SubscriptionInfo> {
        enforceCallingPermissions()
        return Single
            .create(SingleOnSubscribe<Array<SubscriptionInfo>> { emitter ->
                emitter.onSuccess(
                    Optional.ofNullable(
                        SubscriptionManager.from(context).activeSubscriptionInfoList.toTypedArray()
                    ).orElse(
                        emptyArray()
                    )
                )
            })
            .subscribeOn(ThanosSchedulers.serverThread())
            .timeout(300, TimeUnit.MILLISECONDS)
            .onErrorReturnItem(emptyArray())
            .blockingGet()
    }

    override fun getPrivacyDataCheatRequestCount(): Long {
        return privacyRequestHandleTimes
    }

    override fun getPrivacyDataCheatPkgCount(): Int {
        return privacyDataCheatPkgRepo.size()
    }

    override fun asBinder(): IBinder {
        return Noop.notSupported()
    }

    private fun registerReceivers() {
        EventBus.getInstance().registerEventSubscriber(
            IntentFilter(T.Actions.ACTION_FRONT_PKG_CHANGED), frontEventSubscriber
        )
    }

    private fun onFrontPkgChanged(pkg: String) {
        Timber.v("onFrontPkgChanged: %s", pkg)
        Completable
            .fromRunnable { showPackagePrivacyDataCheatingNotification(pkg) }
            .subscribeOn(ThanosSchedulers.serverThread())
            .subscribe()
    }

    private fun showPackagePrivacyDataCheatingNotification(pkg: String) {
        if (!privacyNotificationEnabled) return
        if (!isNotificationPostReady) return
        if (!privacyEnabled) return

        // Cancel first.
        NotificationManagerCompat.from(context)
            .cancel(NotificationIdFactory.getIdByTag(N_TAG_PKG_PRIVACY_DATA_CHEATING))

        if (!isPkgPrivacyDataCheat(pkg)) {
            return
        }

        Timber.v("Will show showPackagePrivacyDataCheatingNotification for pkg: %s", pkg)

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

        val appLabel = s.pkgManagerService.getAppInfo(pkg).appLabel

        // TODO Extract string res.
        val n = builder
            .setContentTitle("隐私防护")
            .setContentText(appLabel + "已被限制访问真实隐私数据")
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        if (OsUtils.isMOrAbove()) {
            n.smallIcon = appResource.getIcon(Res.Drawables.DRAWABLE_EYE_CLOSE_FILL)
        }

        NotificationManagerCompat.from(context)
            .notify(NotificationIdFactory.getIdByTag(N_TAG_PKG_PRIVACY_DATA_CHEATING), n)
    }
}
