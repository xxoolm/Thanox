package github.tornaco.android.thanos.services.os

import android.os.IBinder
import github.tornaco.android.thanos.core.os.IServiceManager

class ServiceManagerStub(private val serviceManagerService: ServiceManagerService) : IServiceManager.Stub(),
    IServiceManager by serviceManagerService {

    override fun asBinder(): IBinder {
        return super.asBinder()
    }
}