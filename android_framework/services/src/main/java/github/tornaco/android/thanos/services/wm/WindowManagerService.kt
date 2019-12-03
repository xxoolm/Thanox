package github.tornaco.android.thanos.services.wm

import android.content.ComponentName
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

    fun findAndClickViewByText(
        text: String,
        targetComponent: ComponentName,
        interval: Long,
        maxRetryTimes: Int
    ) {
        executeInternal(ViewClickWorker(text, targetComponent, interval, maxRetryTimes, s))
    }

    @ExecuteBySystemHandler
    fun findAndClickViewByTextInternal(text: String, targetComponent: ComponentName): Boolean {
        if (!isSystemReady) return false
        if (s.activityStackSupervisor.currentFrontComponentName != targetComponent) return false
        val info = automation.rootInActiveWindow
        Timber.v("findAndClickViewByText: $info")
        if (info == null) return false
        val list = info.findAccessibilityNodeInfosByText(text)
        Timber.v("findAndClickViewByText.findAccessibilityNodeInfosByText: $list")
        var res = false
        list?.forEach {
            Timber.d("Hit! findAndClickViewByText.performAction: $it")
            if (it.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                res = true
            }
        }
        return res
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
