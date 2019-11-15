package github.tornaco.android.thanos.services.xposed.hooks.task;

import android.content.Intent;
import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.annotation.XposedHook;

import java.util.Set;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions.*;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

// Hook hookRemoveTask settings.
@XposedHook(targetSdkVersion = {_21, _22, _23, _24, _25, _26, _27, _28, _29})
@Beta
public class AMSRemoveTaskRegistry implements IXposedHook {

    private void hookSuperVisorCleanUpTask(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.v("hookSuperVisorCleanUpTask...");
        try {
            Class clazz;
            if (OsUtils.isQOrAbove()) {
                clazz = XposedHelpers.findClass("com.android.server.wm.ActivityStackSupervisor", lpparam.classLoader);
            } else if (OsUtils.isOOrAbove()) {
                clazz = XposedHelpers.findClass("com.android.server.am.ActivityStackSupervisor", lpparam.classLoader);
            } else {
                // http://androidxref.com/6.0.1_r10/xref/frameworks/base/services/core/java/com/android/server/am/ActivityManagerService.java
                clazz = XposedHelpers.findClass("com.android.server.am.ActivityManagerService", lpparam.classLoader);
            }
            Set unHooks = XposedBridge.hookAllMethods(clazz,
                    "cleanUpRemovedTaskLocked",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            // void cleanUpRemovedTaskLocked(TaskRecord tr, boolean killProcess, boolean removeFromRecents)
                            Object taskRecordObject = param.args[0];
                            Timber.d("cleanUpRemovedTaskLocked taskRecordObject: %s", taskRecordObject);
                            Intent intent = (Intent) XposedHelpers.getObjectField(taskRecordObject, "intent");
                            BootStrap.THANOS_X.getActivityManagerService().onTaskRemoving(intent);
                        }
                    });
            Timber.v("hookSuperVisorCleanUpTask OK:" + unHooks);
        } catch (Exception e) {
            Timber.e("Fail hookSuperVisorCleanUpTask: " + Log.getStackTraceString(e));
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookSuperVisorCleanUpTask(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // noop.
    }
}
