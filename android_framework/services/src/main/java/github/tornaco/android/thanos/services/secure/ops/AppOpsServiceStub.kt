package github.tornaco.android.thanos.services.secure.ops

import android.os.IBinder
import github.tornaco.android.thanos.core.secure.ops.IAppOpsService

class AppOpsServiceStub(service: AppOpsService) : IAppOpsService.Stub(), IAppOpsService by service {
    override fun asBinder(): IBinder {
        return super.asBinder()
    }
}
