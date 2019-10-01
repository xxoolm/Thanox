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

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions.*;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */
@XposedHook(targetSdkVersion = {_21, _22, _23, _24, _25, _26, _27, _28, _29})
public class ActivityRecordRegistry implements IXposedHook {

    private final IActivityStackSupervisor supervisor = BootStrap
            .THANOS_X
            .getActivityStackSupervisor();

    private void hookStartLaunchTickingLocked(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.v("hookStartLaunchTickingLocked...");
        try {
            // Android Q: Moved Activity management out of ActivityManager to WindowManager
            String clazzName = OsUtils.isQOrAbove()
                    ? "com.android.server.wm.ActivityRecord"
                    : "com.android.server.am.ActivityRecord";
            Class c = XposedHelpers.findClass(clazzName, lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(c, "startLaunchTickingLocked", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object ar = param.thisObject;
                    Intent intent = (Intent) XposedHelpers.getObjectField(ar, "intent");
                    if (intent != null) {
                        supervisor.reportActivityLaunching(intent, "startLaunchTickingLocked");
                    }
                }
            });
            Timber.v("hookStartLaunchTickingLocked OK:" + unHooks);
        } catch (Exception e) {
            Timber.e("Fail hookStartLaunchTickingLocked: " + Log.getStackTraceString(e));
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookStartLaunchTickingLocked(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Noop.
    }
}
