package github.tornaco.android.thanos.services.backup

import android.os.IBinder
import github.tornaco.android.thanos.core.backup.IBackupAgent

class BackupAgentServiceStub(private val service: BackupAgentService) : IBackupAgent.Stub(), IBackupAgent by service {

    override fun asBinder(): IBinder {
        return super.asBinder()
    }

}