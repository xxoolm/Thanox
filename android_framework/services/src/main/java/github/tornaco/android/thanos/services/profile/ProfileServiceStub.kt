package github.tornaco.android.thanos.services.profile

import android.os.IBinder
import github.tornaco.android.thanos.core.profile.IProfileManager

class ProfileServiceStub(private val service: ProfileService) : IProfileManager.Stub(),
    IProfileManager by service {
    override fun asBinder(): IBinder {
        return super.asBinder()
    }
}
