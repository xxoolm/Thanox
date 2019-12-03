package github.tornaco.android.thanos.services.wm

import android.content.ComponentName
import android.content.Context.WINDOW_SERVICE
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Pair
import android.view.IWindow
import android.view.WindowManager
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.IAccessibilityInteractionConnection
import github.tornaco.android.thanos.core.util.Noop
import github.tornaco.android.thanos.core.util.Timber
import github.tornaco.android.thanos.core.util.collection.ArrayMap
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

    private val connectionMap = ArrayMap<IBinder, IAccessibilityInteractionConnection>()

    fun onAccessibilityServiceAttach(server: AccessibilityManagerServiceProxy) {
        accessServiceProxy.set(server)
        Timber.w("onAccessibilityServiceAttach: $server")
    }

    fun onIAccessibilityInteractionConnectionAttach(
        token: IWindow?,
        connection: IAccessibilityInteractionConnection?,
        userId: Int
    ) {
        Timber.d("onIAccessibilityInteractionConnectionAttach: $connection, $token, $userId")
        if (connection == null || token == null) return
        Timber.d("Add windowToken-as-binder: ${token.asBinder()}")
        connectionMap[token.asBinder()] = connection
    }

    fun onIAccessibilityInteractionConnectionRemoved(
        token: IWindow?
    ) {
        Timber.d("onIAccessibilityInteractionConnectionRemoved: $token")
        if (token == null) return
        Timber.d("Remove windowToken-as-binder: ${token.asBinder()}")
        connectionMap.remove(token.asBinder())
    }

    override fun systemReady() {
        super.systemReady()
    }

    override fun shutdown() {
        super.shutdown()
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
        if (connectionMap.isEmpty)return false
        val connection = connectionMap.valueAt(0)
        return false
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
