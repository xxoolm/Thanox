package github.tornaco.android.thanos.services.xposed.hooks.activity.record;

import android.content.Intent;
import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.annotation.XposedHook;

import java.util.Set;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._28;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._29;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */
@XposedHook(targetSdkVersion = {_28, _29})
// ActivityStackSupervisorReportActivityVisible
public class ASSRAVRegistry implements IXposedHook {
    private IActivityStackSupervisor supervisor =
            BootStrap.THANOS_X
                    .getActivityStackSupervisor();

    private void hookReportActivityVisible(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.v("hookReportActivityVisible...");
        try {
            String clazzName = OsUtils.isQOrAbove()
                    ? "com.android.server.wm.ActivityStackSupervisor"
                    : "com.android.server.am.ActivityStackSupervisor";
            Class ams = XposedHelpers.findClass(clazzName,
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams, "reportActivityVisibleLocked", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object ar = param.args[0];
                    if (ar == null) return;
                    Intent intent = (Intent) XposedHelpers.getObjectField(ar, "intent");
                    if (intent == null) return;
                    supervisor.reportActivityLaunching(intent, "reportActivityVisibleLocked");
                }
            });
            Timber.v("hookReportActivityVisible OK:" + unHooks);
        } catch (Exception e) {
            Timber.e("Fail hookReportActivityVisible: " + Log.getStackTraceString(e));
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookReportActivityVisible(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        // Noop.
    }
}
