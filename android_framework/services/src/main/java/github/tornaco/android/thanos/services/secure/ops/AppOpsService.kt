package github.tornaco.android.thanos.services.secure.ops

import android.app.AppOpsManager
import android.content.Context
import android.os.*
import android.util.Log
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.core.Res
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.app.AppResources
import github.tornaco.android.thanos.core.persist.RepoFactory
import github.tornaco.android.thanos.core.persist.StringSetRepo
import github.tornaco.android.thanos.core.secure.ops.IAppOpsService
import github.tornaco.android.thanos.core.util.DevNull
import github.tornaco.android.thanos.core.util.Noop
import github.tornaco.android.thanos.core.util.PkgUtils
import github.tornaco.android.thanos.core.util.Timber
import github.tornaco.android.thanos.services.S
import github.tornaco.android.thanos.services.SystemService
import github.tornaco.android.thanos.services.apihint.ExecuteBySystemHandler
import lombok.SneakyThrows
import java.util.*
import kotlin.collections.HashSet

class AppOpsService(private val s: S) : SystemService(), IAppOpsService {

    private var androidService: com.android.internal.app.IAppOpsService? = AndroidAppOpsDefault()

    private lateinit var opRemindRepo: StringSetRepo
    private lateinit var opRemindNotificationHelper: OpRemindNotificationHelper

    private val opRemindWhiteList: MutableSet<String> = HashSet()

    @SneakyThrows
    override fun onStart(context: Context) {
        super.onStart(context)
        androidService =
            com.android.internal.app.IAppOpsService.Stub.asInterface(ServiceManager.getService(Context.APP_OPS_SERVICE))
        Timber.d("IAppOpsService of android is: %s", androidService)
        if (androidService == null) {
            androidService = AndroidAppOpsDefault()
        }
        Timber.d("IAppOpsService of android is: %s", androidService)

        opRemindRepo = RepoFactory.get().getOrCreateStringSetRepo(T.remindOpsFile().path)
        opRemindNotificationHelper = OpRemindNotificationHelper(context, s)
    }

    override fun systemReady() {
        super.systemReady()
        val appResources = AppResources(context, BuildProp.THANOS_APP_PKG_NAME)
        val opWhiteList: Array<String> = appResources.getStringArray(Res.Strings.OP_REMIND_WHITELIST)
        this.opRemindWhiteList.addAll(opWhiteList)
        Timber.d("opRemindWhiteList: ${Arrays.toString(opRemindWhiteList.toTypedArray())}")
    }

    @Throws(RemoteException::class)
    override fun checkPackage(uid: Int, packageName: String): Int {
        val ident = Binder.clearCallingIdentity()
        try {
            return androidService!!.checkPackage(uid, packageName)
        } finally {
            Binder.restoreCallingIdentity(ident)
        }
    }

    @Throws(RemoteException::class)
    override fun getPackagesForOps(ops: IntArray): List<AppOpsManager.PackageOps> {
        val ident = Binder.clearCallingIdentity()
        try {
            return androidService!!.getPackagesForOps(ops)
        } finally {
            Binder.restoreCallingIdentity(ident)
        }
    }

    @Throws(RemoteException::class)
    override fun getOpsForPackage(uid: Int, packageName: String, ops: IntArray): List<AppOpsManager.PackageOps> {
        val ident = Binder.clearCallingIdentity()
        try {
            return androidService!!.getOpsForPackage(uid, packageName, ops)
        } finally {
            Binder.restoreCallingIdentity(ident)
        }
    }

    @Throws(RemoteException::class)
    override fun getUidOps(uid: Int, ops: IntArray): List<AppOpsManager.PackageOps> {
        val ident = Binder.clearCallingIdentity()
        try {
            return androidService!!.getUidOps(uid, ops)
        } finally {
            Binder.restoreCallingIdentity(ident)
        }
    }

    @Throws(RemoteException::class)
    override fun setUidMode(code: Int, uid: Int, mode: Int) {
        enforceCallingPermissions()
        val ident = Binder.clearCallingIdentity()
        try {
            androidService!!.setUidMode(code, uid, mode)
        } finally {
            Binder.restoreCallingIdentity(ident)
        }
    }

    @Throws(RemoteException::class)
    override fun setMode(code: Int, uid: Int, packageName: String, mode: Int) {
        enforceCallingPermissions()
        val ident = Binder.clearCallingIdentity()
        try {
            Timber.v("setMode: %s %s %s %s", code, uid, packageName, mode)
            androidService!!.setMode(code, uid, packageName, mode)
        } finally {
            Binder.restoreCallingIdentity(ident)
        }
    }

    @Throws(RemoteException::class)
    override fun resetAllModes(reqUserId: Int, reqPackageName: String) {
        enforceCallingPermissions()
        val ident = Binder.clearCallingIdentity()
        try {
            androidService!!.resetAllModes(reqUserId, reqPackageName)
        } finally {
            Binder.restoreCallingIdentity(ident)
        }
    }

    @Throws(RemoteException::class)
    override fun checkOperation(code: Int, uid: Int, packageName: String): Int {
        val ident = Binder.clearCallingIdentity()
        // IllegalArgumentException: Bad operation #71
        try {
            return androidService!!.checkOperation(code, uid, packageName)
        } catch (e: Throwable) {
            Timber.w("checkOperation: %s", Log.getStackTraceString(e))
            return github.tornaco.android.thanos.core.secure.ops.AppOpsManager.MODE_ERRORED
        } finally {
            Binder.restoreCallingIdentity(ident)
        }
    }

    override fun setUserRestrictions(restrictions: Bundle, token: IBinder, userHandle: Int) {
        enforceCallingPermissions()
        val ident = Binder.clearCallingIdentity()
        try {
            androidService!!.setUserRestrictions(restrictions, token, userHandle)
        } finally {
            Binder.restoreCallingIdentity(ident)
        }
    }

    override fun setUserRestriction(
        code: Int,
        restricted: Boolean,
        token: IBinder,
        userHandle: Int,
        exceptionPackages: Array<String>
    ) {
        enforceCallingPermissions()
        val ident = Binder.clearCallingIdentity()
        try {
            androidService!!.setUserRestriction(code, restricted, token, userHandle, exceptionPackages)
        } finally {
            Binder.restoreCallingIdentity(ident)
        }
    }

    @Throws(RemoteException::class)
    override fun removeUser(userHandle: Int) {
        enforceCallingPermissions()
        val ident = Binder.clearCallingIdentity()
        try {
            androidService!!.removeUser(userHandle)
        } finally {
            Binder.restoreCallingIdentity(ident)
        }
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
            opRemindRepo.add(code.toString())
        } else {
            opRemindRepo.remove(code.toString())
        }
    }

    @Throws(RemoteException::class)
    override fun isOpRemindEnabled(code: Int): Boolean {
        return opRemindRepo.has(code.toString())
    }

    private fun isOpRemindablePkg(pkg: String?): Boolean {
        return pkg != null && !opRemindWhiteList.contains(pkg)
    }

    @Throws(RemoteException::class)
    override fun onStartOp(token: IBinder?, code: Int, uid: Int, packageName: String?) {
        if (isSystemReady && isNotificationPostReady && isOpRemindEnabled(code) && isOpRemindablePkg(packageName)) {
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

    private fun enforceCallingPermissions() {
        if (Process.myPid() == Binder.getCallingPid()) {
            return
        }

        val callingUid = Binder.getCallingUid()
        if (PkgUtils.isSystemOrPhoneOrShell(callingUid)) {
            return
        }
        val thanosAppUid = s.pkgManagerService.thanosAppUid
        if (thanosAppUid == callingUid) {
            return
        }

        throw SecurityException("Uid of $callingUid it not allowed to interact with Thanos server")
    }

    override fun asBinder(): IBinder {
        return Noop.notSupported()
    }
}
