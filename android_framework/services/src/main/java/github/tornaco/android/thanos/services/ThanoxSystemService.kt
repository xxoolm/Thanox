package github.tornaco.android.thanos.services

import android.os.Binder
import android.os.Process
import github.tornaco.android.thanos.core.util.PkgUtils

open class ThanoxSystemService(@JvmField protected val s: S) : SystemService() {

    protected fun enforceCallingPermissions() {
        if (Process.myPid() == Binder.getCallingPid()) {
            return
        }

        val callingUid = Binder.getCallingUid()
        if (PkgUtils.isSystemOrPhoneOrShell(callingUid)) {
            return
        }

        if (s.pkgManagerService.mayBeThanosAppUid(callingUid)) {
            return
        }

        throw SecurityException("Uid of $callingUid it not allowed to interact with Thanos server")
    }
}
