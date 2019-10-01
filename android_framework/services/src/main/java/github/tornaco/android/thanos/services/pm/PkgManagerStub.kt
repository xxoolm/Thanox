package github.tornaco.android.thanos.services.pm

import android.os.IBinder
import github.tornaco.android.thanos.core.pm.IPkgManager

class PkgManagerStub(private val pm: PkgManagerService) : IPkgManager.Stub(), IPkgManager by  pm {

    override fun asBinder(): IBinder {
        return super.asBinder()
    }
}
