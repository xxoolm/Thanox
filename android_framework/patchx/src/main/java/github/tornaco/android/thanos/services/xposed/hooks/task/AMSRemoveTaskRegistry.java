package github.tornaco.android.thanos.services.xposed.hooks.task;

import android.os.Binder;
import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.Timber;
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
public class AMSRemoveTaskRegistry implements IXposedHook {

    private void hookRemoveTask(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.v("hookRemoveTask...");
        try {
            Class ams = XposedHelpers.findClass("com.android.server.am.ActivityManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams, "removeTask",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            int callingUid = Binder.getCallingUid();
                            int taskId = (int) param.args[0];
                            IThanos thanos = ThanosManagerNative.getDefault();
                            if (thanos != null) {
                                thanos.getActivityManager().onTaskRemoving(callingUid, taskId);
                            }
                        }
                    });
            Timber.v("hookRemoveTask OK:" + unHooks);
        } catch (Exception e) {
            Timber.e("Fail hookRemoveTask: " + Log.getStackTraceString(e));
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookRemoveTask(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        // noop.
    }
}
