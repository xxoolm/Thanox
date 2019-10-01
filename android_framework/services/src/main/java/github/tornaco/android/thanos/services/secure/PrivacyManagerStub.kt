package github.tornaco.android.thanos.services.secure

import android.os.IBinder
import github.tornaco.android.thanos.core.secure.IPrivacyManager

class PrivacyManagerStub(private val service: PrivacyService) : IPrivacyManager.Stub(), IPrivacyManager by service{
    override fun asBinder(): IBinder {
        return super.asBinder()
    }
}
