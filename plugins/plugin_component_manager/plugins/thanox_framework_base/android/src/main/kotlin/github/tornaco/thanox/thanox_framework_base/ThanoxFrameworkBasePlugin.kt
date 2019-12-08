package github.tornaco.thanox.thanox_framework_base

import android.content.Context
import github.tornaco.android.thanos.core.app.ThanosManager
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

class ThanoxFrameworkBasePlugin(private val context: Context) : MethodCallHandler {

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "thanox_framework_base")
            channel.setMethodCallHandler(ThanoxFrameworkBasePlugin(registrar.context()))
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
            return
        }

        if (call.method == "isServiceInstalled") {
            val thanox = ThanosManager.from(context)
            result.success(thanox.isServiceInstalled)
            return
        }

        if (call.method == "fingerPrint") {
            val thanox = ThanosManager.from(context)
            result.success(thanox.fingerPrint())
            return
        }

        if (call.method == "versionName") {
            val thanox = ThanosManager.from(context)
            result.success(thanox.versionName)
            return
        }

        result.notImplemented()
    }
}
