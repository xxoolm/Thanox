package github.tornaco.android.thanos.services.app

import android.os.IBinder
import github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor

class ActivityStackSupervisorStub(private val supervisor: ActivityStackSupervisorService) :
    IActivityStackSupervisor.Stub(), IActivityStackSupervisor by supervisor{

    override fun asBinder(): IBinder {
        return super.asBinder()
    }
}
