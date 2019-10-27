package github.tornaco.android.thanos.services.power

import android.content.Context
import android.os.IBinder
import android.os.PowerManager
import github.tornaco.android.thanos.core.power.IPowerManager
import github.tornaco.android.thanos.core.util.Noop
import github.tornaco.android.thanos.services.S
import github.tornaco.android.thanos.services.ThanoxSystemService
import github.tornaco.android.thanos.services.apihint.ExecuteBySystemHandler

class PowerService(s: S) : ThanoxSystemService(s), IPowerManager {

    override fun softReboot() {
        enforceCallingPermissions()
    }

    override fun goToSleep() {
        enforceCallingPermissions()
    }

    override fun reboot() {
        enforceCallingPermissions()
        executeInternal(Runnable {
            rebootInternal()
        })
    }

    @ExecuteBySystemHandler
    private fun rebootInternal() {
        val systemPower: PowerManager = context!!.getSystemService(Context.POWER_SERVICE) as PowerManager
        systemPower.reboot(null)
    }

    override fun asBinder(): IBinder {
        return Noop.notSupported()
    }
}
