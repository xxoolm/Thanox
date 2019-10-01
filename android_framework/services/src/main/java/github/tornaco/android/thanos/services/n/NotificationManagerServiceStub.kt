package github.tornaco.android.thanos.services.n

import android.os.IBinder
import github.tornaco.android.thanos.core.n.INotificationManager

class NotificationManagerServiceStub(private val service: NotificationManagerService) : INotificationManager.Stub(),
    INotificationManager by service {
    override fun asBinder(): IBinder {
        return super.asBinder()
    }
}