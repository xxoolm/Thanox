package github.tornaco.android.thanos.services.secure.ops

import android.content.Context
import android.os.IBinder
import android.os.RemoteException
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.core.Res
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.app.AppResources
import github.tornaco.android.thanos.core.persist.RepoFactory
import github.tornaco.android.thanos.core.persist.StringMapRepo
import github.tornaco.android.thanos.core.persist.StringSetRepo
import github.tornaco.android.thanos.core.pref.IPrefChangeListener
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager
import github.tornaco.android.thanos.core.secure.ops.IAppOpsService
import github.tornaco.android.thanos.core.util.Noop
import github.tornaco.android.thanos.core.util.Timber
import github.tornaco.android.thanos.services.BootStrap
import github.tornaco.android.thanos.services.S
import github.tornaco.android.thanos.services.ThanoxSystemService
import github.tornaco.android.thanos.services.apihint.ExecuteBySystemHandler
import lombok.SneakyThrows
import util.ObjectsUtils

class AppOpsService(s: S) : ThanoxSystemService(s), IAppOpsService {
    // Turn off for production build.
    private val debugOp = !BootStrap.IS_RELEASE_BUILD

    private lateinit var opRemindOpRepo: StringSetRepo
    private lateinit var opRemindPkgRepo: StringSetRepo

    private lateinit var opTemplateRepo: StringMapRepo
    private lateinit var opSettingsRepo: StringMapRepo

    private var opsEnabled = false

    private lateinit var opRemindNotificationHelper: OpRemindNotificationHelper

    private val opRemindWhiteList: MutableSet<String> = HashSet()

    @SneakyThrows
    override fun onStart(context: Context) {
        super.onStart(context)
        opRemindOpRepo = RepoFactory.get().getOrCreateStringSetRepo(T.opRemindOpsFile().path)
        opRemindPkgRepo = RepoFactory.get().getOrCreateStringSetRepo(T.opRemindPkgFile().path)
        opTemplateRepo = RepoFactory.get().getOrCreateStringMapRepo(T.opTemplateFile().path)
        opSettingsRepo = RepoFactory.get().getOrCreateStringMapRepo(T.opSettingsFile().path)
        opRemindNotificationHelper = OpRemindNotificationHelper(context, s)
    }

    override fun systemReady() {
        super.systemReady()
        val appResources = AppResources(context, BuildProp.THANOS_APP_PKG_NAME)
        val opWhiteList: Array<String> =
            appResources.getStringArray(Res.Strings.OP_REMIND_WHITELIST)
        this.opRemindWhiteList.addAll(opWhiteList)
        Timber.d("opRemindWhiteList: ${opRemindWhiteList.toTypedArray().contentToString()}")
        initPrefs()
    }

    private fun initPrefs() {
        readPrefs()
        listenToPrefs()
    }

    private fun readPrefs() {
        val preferenceManagerService = s.preferenceManagerService
        this.opsEnabled = preferenceManagerService.getBoolean(
            T.Settings.PREF_OPS_ENABLED.key,
            T.Settings.PREF_OPS_ENABLED.defaultValue
        )
    }

    private fun listenToPrefs() {
        val listener = object : IPrefChangeListener.Stub() {
            override fun onPrefChanged(key: String) {
                if (ObjectsUtils.equals(T.Settings.PREF_OPS_ENABLED.key, key)) {
                    Timber.i("Pref changed, reload.")
                    readPrefs()
                }
            }
        }
        s.preferenceManagerService.registerSettingsChangeListener(listener)
    }

    @Throws(RemoteException::class)
    override fun setMode(code: Int, uid: Int, packageName: String, mode: Int) {
        enforceCallingPermissions()
        Timber.v("setMode: %s %s %s %s", code, uid, packageName, mode)
        opSettingsRepo["$packageName-$code"] = mode.toString()
    }

    @Throws(RemoteException::class)
    override fun resetAllModes(reqUserId: Int, reqPackageName: String) {
        enforceCallingPermissions()
    }

    @Throws(RemoteException::class)
    override fun checkOperation(code: Int, uid: Int, packageName: String): Int {
        if (debugOp) Timber.v("checkOperation: $packageName $code")
        // IllegalArgumentException: Bad operation #71
        return opSettingsRepo["$packageName-$code"]?.toInt() ?: AppOpsManager.MODE_ALLOWED
    }

    @Throws(RemoteException::class)
    override fun isOpsEnabled(): Boolean {
        return opsEnabled
    }

    @Throws(RemoteException::class)
    override fun setOpsEnabled(enabled: Boolean) {
        enforceCallingPermissions()
        opsEnabled = enabled

        val preferenceManagerService = s.preferenceManagerService
        preferenceManagerService.putBoolean(
            T.Settings.PREF_OPS_ENABLED.key,
            enabled
        )
    }

    @Throws(RemoteException::class)
    override fun setOpRemindEnable(code: Int, enable: Boolean) {
        enforceCallingPermissions()
        if (enable) {
            opRemindOpRepo.add(code.toString())
        } else {
            opRemindOpRepo.remove(code.toString())
        }
    }

    @Throws(RemoteException::class)
    override fun isOpRemindEnabled(code: Int): Boolean {
        return opRemindOpRepo.has(code.toString())
    }

    private fun isOpRemindablePkg(pkg: String?): Boolean {
        return pkg != null
                && !opRemindWhiteList.contains(pkg)
                && opRemindPkgRepo.has(pkg)
    }

    @Throws(RemoteException::class)
    override fun onStartOp(token: IBinder?, code: Int, uid: Int, packageName: String?) {
        if (debugOp) Timber.v("onStartOp: $code, $packageName")
        if (isSystemReady && isNotificationPostReady && isOpRemindEnabled(code) && isOpRemindablePkg(
                packageName
            )
        ) {
            executeInternal(Runnable {
                onStartOpInternal(token, code, uid, packageName)
            })
        }
    }

    @ExecuteBySystemHandler
    fun onStartOpInternal(token: IBinder?, code: Int, uid: Int, packageName: String?) {
        packageName?.let { opRemindNotificationHelper.remindOpStart(it, code) }
    }

    @Throws(RemoteException::class)
    override fun onFinishOp(token: IBinder?, code: Int, uid: Int, packageName: String?) {
        if (isSystemReady && isNotificationPostReady && isOpRemindEnabled(code)) {
            executeInternal(Runnable {
                onFinishOpInternal(token, code, uid, packageName)
            })
        }
    }

    @ExecuteBySystemHandler
    private fun onFinishOpInternal(token: IBinder?, code: Int, uid: Int, packageName: String?) {
        packageName?.let { opRemindNotificationHelper.remindOpFinish(it, code) }
    }

    override fun setPkgOpRemindEnable(pkg: String?, enable: Boolean) {
        if (enable) opRemindPkgRepo.add(pkg) else opRemindPkgRepo.remove(pkg)
    }

    override fun isPkgOpRemindEnable(pkg: String?): Boolean {
        return opRemindPkgRepo.has(pkg)
    }

    override fun asBinder(): IBinder {
        return Noop.notSupported()
    }
}
