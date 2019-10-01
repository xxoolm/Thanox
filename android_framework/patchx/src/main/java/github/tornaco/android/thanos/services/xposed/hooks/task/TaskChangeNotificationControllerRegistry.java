package github.tornaco.android.thanos.services.xposed.hooks.task;

import android.content.ComponentName;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.app.IActivityManager;
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

@XposedHook(targetSdkVersion = {_26, _27, _28, _29})
public class TaskChangeNotificationControllerRegistry implements IXposedHook {
    private final IActivityManager activityManager =
            BootStrap.THANOS_X.getActivityManagerService();

    private void hookNotifyTaskCreated(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.v("hookNotifyTaskCreated...");

        try {
            // Android Q: Moved Activity management out of ActivityManager to WindowManager
            // TaskChangeNotificationController
            String clazzName = OsUtils.isQOrAbove()
                    ? "com.android.server.wm.TaskChangeNotificationController"
                    : "com.android.server.am.TaskChangeNotificationController";
            Class clz = XposedHelpers.findClass(clazzName,
                    lpparam.classLoader);

            Set unHooks = XposedBridge.hookAllMethods(clz,
                    "notifyTaskCreated", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            int taskId = (int) param.args[0];
                            ComponentName componentName = (ComponentName) param.args[1];
                            activityManager.notifyTaskCreated(taskId, componentName);
                        }
                    });
            Timber.v("hookNotifyTaskCreated OK: " + unHooks);
        } catch (Exception e) {
            Timber.v("Fail hookNotifyTaskCreated: " + e);
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookNotifyTaskCreated(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        // Noop.
    }
}
