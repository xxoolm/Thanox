package github.tornaco.android.thanos.services.power

import android.os.IBinder
import github.tornaco.android.thanos.core.power.IPowerManager

class PowerServiceStub(private val service: PowerService) : IPowerManager.Stub(), IPowerManager by service {
    override fun asBinder(): IBinder {
        return super.asBinder()
    }
}
