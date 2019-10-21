package github.tornaco.android.thanos.services.wm

import android.content.Context.WINDOW_SERVICE
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Pair
import android.view.WindowManager
import github.tornaco.android.thanos.core.util.Noop
import github.tornaco.android.thanos.core.wm.IWindowManager
import github.tornaco.android.thanos.services.S
import github.tornaco.android.thanos.services.SystemService

class WindowManagerService(private val s: S) : SystemService(), IWindowManager {

    private var screenSize: Pair<Int, Int>? = null

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
