package github.tornaco.android.thanos.services.wm

import android.content.Context.WINDOW_SERVICE
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Pair
import android.view.WindowManager
import android.view.accessibility.AccessibilityNodeInfo
import github.tornaco.android.thanos.core.util.Noop
import github.tornaco.android.thanos.core.util.Timber
import github.tornaco.android.thanos.core.wm.IWindowManager
import github.tornaco.android.thanos.services.S
import github.tornaco.android.thanos.services.SystemService
import github.tornaco.android.thanos.services.accessibility.AccessibilityManagerServiceProxy
import github.tornaco.android.thanos.services.apihint.ExecuteBySystemHandler
import java.util.concurrent.atomic.AtomicReference

class WindowManagerService(private val s: S) : SystemService(), IWindowManager {

    private var screenSize: Pair<Int, Int>? = null

    private var accessServiceProxy: AtomicReference<AccessibilityManagerServiceProxy> =
        AtomicReference()
    private val automation = UiAutomationManager()

    fun onAccessibilityServiceAttach(server: AccessibilityManagerServiceProxy) {
        accessServiceProxy.set(server)
        Timber.w("onAccessibilityServiceAttach: $server")
    }

    override fun systemReady() {
        super.systemReady()
        automation.connect()
    }

    override fun shutdown() {
        super.shutdown()
        automation.disconnect()
    }

    fun dumpActiveWindow() {
        executeInternal(Runnable { dumpActiveWindowInternal() })
    }

    @ExecuteBySystemHandler
    private fun dumpActiveWindowInternal() {
        if (!isSystemReady) return
        val info = automation.rootInActiveWindow
        Timber.d("rootInActiveWindow: $info")
        if (info != null) {
            val list = info.findAccessibilityNodeInfosByText("跳过")
            Timber.v("findAccessibilityNodeInfosByText: $list")
            list?.forEach {
                it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        }
    }

    override fun getScreenSize(): IntArray {
        if (screenSize == null) {
            val displayMetrics = DisplayMetrics()
            val windowManager = context!!.getSystemService(WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels
            screenSize = Pair(width, height)
        }
        return intArrayOf(screenSize!!.first, screenSize!!.second)
    }

    override fun asBinder(): IBinder {
        return Noop.notSupported()
    }
}
