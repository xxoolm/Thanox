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
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.xposed.annotation.XposedHook;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._21;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._22;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._23;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._24;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._25;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._26;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._27;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._28;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._29;

@XposedHook(targetSdkVersion = {_21, _22, _23, _24, _25, _26, _27, _28, _29})
public class ActivityStackRegistry implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookResumeTopActivityInnerLocked(lpparam);
        }
    }

    private void hookResumeTopActivityInnerLocked(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class ams = XposedHelpers.findClass(
                    OsUtils.isQOrAbove()
                            ? "com.android.server.wm.ActivityStack"
                            : "com.android.server.am.ActivityStack",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams, "resumeTopActivityInnerLocked", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    boolean res = (boolean) param.getResult();
                    if (res) {
                        // ActivityRecord next = topRunningActivityLocked(true /* focusableOnly */);
                        // final ActivityRecord topRunningActivityLocked(): N
                        Object nextObj;
                        try {
                            nextObj = XposedHelpers.callMethod(param.thisObject, "topRunningActivityLocked", true);
                        } catch (NoSuchMethodError e) {
                            nextObj = XposedHelpers.callMethod(param.thisObject, "topRunningActivityLocked");
                        }
                        if (nextObj != null) {
                            Intent intent = (Intent) XposedHelpers.getObjectField(nextObj, "intent");
                            Timber.v("resumeTopActivityInnerLocked, next: %s, args: %s", intent, Arrays.toString(param.args));
                            if (intent != null) {
                                BootStrap.THANOS_X.getActivityStackSupervisor().onActivityResumed(intent);
                            }
                        } else {
                            Timber.e("Call topRunningActivityLocked failed...Please file a bug.");
                        }
                    }
                }
            });
            Timber.i("hookResumeTopActivityInnerLocked, unhooks %s", unHooks);
        } catch (Throwable e) {
            Timber.i("hookResumeTopActivityInnerLocked error %s", Log.getStackTraceString(e));
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {

    }
}
