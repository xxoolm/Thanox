package github.tornaco.android.thanos.services.app

import android.os.IBinder
import github.tornaco.android.thanos.core.app.IActivityManager

class ActivityManagerStub(private val ams: ActivityManagerService) : IActivityManager.Stub(), IActivityManager by ams {

    override fun asBinder(): IBinder {
        return super.asBinder()
    }
}
