package github.tornaco.android.thanos.services.secure.ops

import android.app.AppOpsManager
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import com.android.internal.app.IAppOpsCallback
import com.android.internal.app.IAppOpsService

class AndroidAppOpsDefault : IAppOpsService.Stub() {

    @Throws(RemoteException::class)
    override fun checkOperation(i: Int, i1: Int, s: String): Int {
        return 0
    }

    @Throws(RemoteException::class)
    override fun noteOperation(i: Int, i1: Int, s: String): Int {
        return 0
    }

    @Throws(RemoteException::class)
    override fun startOperation(iBinder: IBinder, i: Int, i1: Int, s: String): Int {
        return 0
    }

    @Throws(RemoteException::class)
    override fun finishOperation(iBinder: IBinder, i: Int, i1: Int, s: String) {

    }

    @Throws(RemoteException::class)
    override fun startWatchingMode(i: Int, s: String, iAppOpsCallback: IAppOpsCallback) {

    }

    @Throws(RemoteException::class)
    override fun stopWatchingMode(iAppOpsCallback: IAppOpsCallback) {

    }

    @Throws(RemoteException::class)
    override fun getToken(iBinder: IBinder): IBinder? {
        return null
    }

    @Throws(RemoteException::class)
    override fun permissionToOpCode(s: String): Int {
        return 0
    }

    @Throws(RemoteException::class)
    override fun noteProxyOperation(i: Int, s: String, i1: Int, s1: String): Int {
        return 0
    }

    @Throws(RemoteException::class)
    override fun checkPackage(i: Int, s: String): Int {
        return 0
    }

    @Throws(RemoteException::class)
    override fun getPackagesForOps(ints: IntArray): List<AppOpsManager.PackageOps>? {
        return null
    }

    @Throws(RemoteException::class)
    override fun getOpsForPackage(i: Int, s: String, ints: IntArray): List<AppOpsManager.PackageOps>? {
        return null
    }

    @Throws(RemoteException::class)
    override fun getUidOps(i: Int, ints: IntArray): List<AppOpsManager.PackageOps>? {
        return null
    }

    @Throws(RemoteException::class)
    override fun setUidMode(i: Int, i1: Int, i2: Int) {

    }

    @Throws(RemoteException::class)
    override fun setMode(i: Int, i1: Int, s: String, i2: Int) {

    }

    @Throws(RemoteException::class)
    override fun resetAllModes(i: Int, s: String) {

    }

    @Throws(RemoteException::class)
    override fun checkAudioOperation(i: Int, i1: Int, i2: Int, s: String): Int {
        return 0
    }

    @Throws(RemoteException::class)
    override fun setAudioRestriction(i: Int, i1: Int, i2: Int, i3: Int, strings: Array<String>) {

    }

    @Throws(RemoteException::class)
    override fun setUserRestrictions(bundle: Bundle, iBinder: IBinder, i: Int) {

    }

    @Throws(RemoteException::class)
    override fun setUserRestriction(i: Int, b: Boolean, iBinder: IBinder, i1: Int, strings: Array<String>) {

    }

    @Throws(RemoteException::class)
    override fun removeUser(i: Int) {

    }

    @Throws(RemoteException::class)
    override fun isOperationActive(i: Int, i1: Int, s: String): Boolean {
        return false
    }
}
