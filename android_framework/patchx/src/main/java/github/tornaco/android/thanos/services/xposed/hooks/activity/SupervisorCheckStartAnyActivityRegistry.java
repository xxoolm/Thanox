package github.tornaco.android.thanos.services.xposed.hooks.activity;

import android.content.Intent;
import android.util.Log;

import java.util.Arrays;
import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.xposed.annotation.XposedHook;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._24;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._25;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._26;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._27;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._28;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._29;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */
@XposedHook(targetSdkVersion = {_24, _25, _26, _27, _28, _29})
public class SupervisorCheckStartAnyActivityRegistry implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    private void hookCheckStartAnyActivityPermission(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.d("hookCheckStartAnyActivityPermission...");
        try {
            Class ams = XposedHelpers.findClass(
                    OsUtils.isQOrAbove()
                            ? "com.android.server.wm.ActivityStackSupervisor"
                            : "com.android.server.am.ActivityStackSupervisor",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams, "checkStartAnyActivityPermission", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Timber.v("checkStartAnyActivityPermission: %s", Arrays.toString(param.args));
                    Intent intent = (Intent) param.args[0];
                    if (BootStrap.THANOS_X.getActivityStackSupervisor().shouldFixAroundStartAnyActivityPermission(intent)) {
                        Timber.v("Fixed checkStartAnyActivityPermission: %s", intent);
                        param.setResult(true);
                    }
                }
            });
            Timber.d("hookCheckStartAnyActivityPermission OK:" + unHooks);
        } catch (Exception e) {
            Timber.d("Fail hookCheckStartAnyActivityPermission: " + Log.getStackTraceString(e));
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (github.tornaco.android.thanos.core.pm.PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookCheckStartAnyActivityPermission(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Noop.
    }
}
