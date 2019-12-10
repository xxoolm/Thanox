package github.tornaco.android.thanos.services.xposed.hooks.task;

import android.content.Intent;
import android.util.Log;

import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.annotation.Keep;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.android.thanos.services.xposed.hooks.ErrorReporter;
import github.tornaco.xposed.annotation.XposedHook;
import util.ObjectsUtils;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._28;

@XposedHook(targetSdkVersion = {_28})
@Beta
@Keep
public class CreateRecentTaskInfoRegistryP implements IXposedHook {

    protected Class clazzToHook(XC_LoadPackage.LoadPackageParam lpparam) {
        return XposedHelpers.findClass("com.android.server.am.RecentTasks",
                lpparam.classLoader);
    }

    protected String methodToHook() {
        return "isVisibleRecentTask";
    }

    // private boolean isVisibleRecentTask(TaskRecord task)
    private void hookIsVisibleRecentTask(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.v("hookIsVisibleRecentTask...");
        try {
            Class clazz = clazzToHook(lpparam);
            Timber.v("hookIsVisibleRecentTask...class:" + clazz);
            Set unHooks = XposedBridge.hookAllMethods(clazz, methodToHook(),
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);

                            Object taskRecordObject = param.args[0];
                            if (taskRecordObject == null) {
                                return;
                            }
                            Intent intent = (Intent) XposedHelpers.getObjectField(taskRecordObject, "intent");
                            if (intent == null) {
                                return;
                            }
                            String pkgName = PkgUtils.packageNameOf(intent);
                            if (pkgName == null) {
                                return;
                            }

                            if (ObjectsUtils.equals(
                                    BootStrap.THANOS_X.getActivityStackSupervisor().getCurrentFrontApp(),
                                    pkgName)) {
                                Timber.v("-BEFORE isVisibleRecentTask, %s is current top, won't check.", pkgName);
                                return;
                            }

                            boolean result = (boolean) param.getResult();
                            if (!BootStrap.IS_RELEASE_BUILD) {
                                Timber.v("-BEFORE isVisibleRecentTask, intent is: %s, result is: %s", intent, result);
                            }

                            int setting = BootStrap.THANOS_X.getActivityManagerService().getRecentTaskExcludeSettingForPackage(pkgName);
                            if (setting == github.tornaco.android.thanos.core.app.ActivityManager.ExcludeRecentSetting.NONE) {
                                return;
                            }

                            if (setting == github.tornaco.android.thanos.core.app.ActivityManager.ExcludeRecentSetting.EXCLUDE) {
                                result = false;
                            }

                            if (setting == github.tornaco.android.thanos.core.app.ActivityManager.ExcludeRecentSetting.INCLUDE) {
                                result = true;
                            }

                            param.setResult(result);

                            if (!BootStrap.IS_RELEASE_BUILD) {
                                Timber.v("-AFTER isVisibleRecentTask, intent is: %s, result is: %s", intent, result);
                            }
                        }
                    });
            Timber.v("hookIsVisibleRecentTask OK:" + unHooks);
        } catch (Exception e) {
            Timber.e("Fail hookIsVisibleRecentTask: " + Log.getStackTraceString(e));
            ErrorReporter.report("hookIsVisibleRecentTask", Log.getStackTraceString(e));
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookIsVisibleRecentTask(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Noop.
    }
}
