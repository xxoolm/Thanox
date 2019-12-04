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
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager
import github.tornaco.android.thanos.core.secure.ops.IAppOpsService
import github.tornaco.android.thanos.core.util.DevNull
import github.tornaco.android.thanos.core.util.Noop
import github.tornaco.android.thanos.core.util.Timber
import github.tornaco.android.thanos.services.S
import github.tornaco.android.thanos.services.ThanoxSystemService
import github.tornaco.android.thanos.services.apihint.ExecuteBySystemHandler
import lombok.SneakyThrows

class AppOpsService(s: S) : ThanoxSystemService(s), IAppOpsService {

    private lateinit var opRemindOpRepo: StringSetRepo
    private lateinit var opRemindPkgRepo: StringSetRepo

    private lateinit var opTemplateRepo: StringMapRepo
    private lateinit var opSettingsRepo: StringMapRepo

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
        // IllegalArgumentException: Bad operation #71
        return opSettingsRepo["$packageName-$code"]?.toInt() ?: AppOpsManager.MODE_ALLOWED
    }

    @Throws(RemoteException::class)
    override fun isOpsEnabled(): Boolean {
        // Ops is not supported to toggle.
        // it is work with system ops.
        return Noop.notSupported()
    }

    @Throws(RemoteException::class)
    override fun setOpsEnabled(enabled: Boolean) {
        // Ops is not supported to toggle.
        // it is work with system ops.
        val res: Boolean = Noop.notSupported()
        DevNull.accept(res)
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
