package github.tornaco.android.thanos.services.push

import android.os.IBinder
import github.tornaco.android.thanos.core.push.IPushManager

class PushManagerServiceStub(service: PushManagerService) : IPushManager.Stub(), IPushManager by service {
    override fun asBinder(): IBinder {
        return super.asBinder()
    }
}