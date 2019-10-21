package github.tornaco.android.thanos.services.wm

import android.os.IBinder
import github.tornaco.android.thanos.core.wm.IWindowManager


class WindowManagerServiceStub(private val service: WindowManagerService) : IWindowManager.Stub(),
    IWindowManager by service {
    override fun asBinder(): IBinder {
        return super.asBinder()
    }
}