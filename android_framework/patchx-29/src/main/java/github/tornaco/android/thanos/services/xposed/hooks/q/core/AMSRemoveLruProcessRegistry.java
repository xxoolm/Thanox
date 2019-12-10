package github.tornaco.android.thanos.services.xposed.hooks.q.core;

import android.util.Log;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.android.thanos.services.util.ProcessRecordUtils;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.android.thanos.services.xposed.hooks.q.ErrorReporter;
import github.tornaco.xposed.annotation.XposedHook;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._29;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

// Listen for app process added.
@XposedHook(targetSdkVersion = {_29})
@Beta
public class AMSRemoveLruProcessRegistry implements IXposedHook {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookRemoveProcess(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Noop.
    }

    private void hookRemoveProcess(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.i("hookRemoveProcess...");
        try {
            Class clazz = XposedHelpers.findClass("com.android.server.am.ProcessList",
                    lpparam.classLoader);
            Timber.d("clazz=%s", clazz);
            Class processRecordClazz = XposedHelpers.findClass("com.android.server.am.ProcessRecord",
                    lpparam.classLoader);
            Timber.d("processRecordClazz=%s", processRecordClazz);
            Method method = XposedHelpers.findMethodExactIfExists(clazz, "removeLruProcessLocked",
                    processRecordClazz);
            Timber.d("removeLruProcessLocked=%s", method);
            if (method == null) {
                Timber.e("Method: removeLruProcessLocked not found....");
                return;
            }
            XC_MethodHook.Unhook unHooks = XposedBridge.hookMethod(method, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object processRecord = param.args[0];
                    if (processRecord != null) {
                        BootStrap.THANOS_X.getActivityManagerService().onProcessRemoved(
                                ProcessRecordUtils.fromLegacy(processRecord));
                    }
                }
            });
            Timber.i("hookRemoveProcess OK:" + unHooks);
        } catch (Exception e) {
            Timber.i("Fail hookRemoveProcess: " + Log.getStackTraceString(e));
            ErrorReporter.report("hookRemoveProcess", Log.getStackTraceString(e));
        }
    }


}
