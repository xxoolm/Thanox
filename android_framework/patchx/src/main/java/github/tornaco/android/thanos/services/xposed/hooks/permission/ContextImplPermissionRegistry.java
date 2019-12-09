package github.tornaco.android.thanos.services.xposed.hooks.permission;

import android.Manifest;
import android.app.AndroidAppHelper;
import android.os.Binder;
import android.util.Log;

import java.util.Arrays;
import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.android.thanos.services.xposed.XposedLogger;
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

// For permission enhancement.
// No need, we will find new solution instead of this hook.
@XposedHook(targetSdkVersion = {_21, _22, _23, _24, _25, _26, _27, _28, _29}, active = true)
public class ContextImplPermissionRegistry implements IXposedHook {

    private void hookEnforce() {
        Timber.v("ContextImplSubModule hookEnforce...");
        try {
            Class clz = XposedHelpers.findClass("android.app.ContextImpl", null);
            Set unHooks = XposedBridge.hookAllMethods(clz,
                    "enforce", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(final MethodHookParam param)
                                throws Throwable {
                            super.afterHookedMethod(param);
                            boolean hasErr = param.hasThrowable();
                            if (hasErr) {
                                int uid = Binder.getCallingUid();
                                Object permission = param.args[0];
                                if (isSystemCall(uid)) {
                                    Log.e(Timber.tagWithPrefix(XposedLogger.LOG_PREFIX, "ContextImpl"),
                                            "ContextImpl enforce err@system:" + Arrays.toString(param.args));
                                    if (String.valueOf(permission).startsWith("android.permission")) {
                                        // This is really the permission param!
                                        boolean isIdlePermission = Manifest.permission.CHANGE_APP_IDLE_STATE
                                                .equals(permission);
                                        if (isIdlePermission) {
                                            Log.e(Timber.tagWithPrefix(XposedLogger.LOG_PREFIX, "ContextImpl"),
                                                    "ContextImpl clearing err@system:" + Arrays.toString(param.args));
                                            param.setThrowable(null);
                                        }
                                    }
                                } else if (isThanosCalling()) {
                                    Log.e(Timber.tagWithPrefix(XposedLogger.LOG_PREFIX, "ContextImpl"),
                                            "ContextImpl enforce clearing err@thanox app:" + Arrays.toString(param.args));
                                    param.setThrowable(null);
                                } else //noinspection ConstantConditions
                                    if (false) {
                                        Log.e(Timber.tagWithPrefix(XposedLogger.LOG_PREFIX, "ContextImpl"),
                                                "ContextImpl enforce err@?:" + Arrays.toString(param.args)
                                                        + " - "
                                                        + AndroidAppHelper.currentPackageName());
                                    }
                            }
                        }
                    });
            Timber.v("ContextImplSubModule hookEnforce OK:" + unHooks);
        } catch (Throwable e) {
            Timber.e("ContextImplSubModule Fail hookEnforce:" + e);
        }
    }

    private static boolean isSystemCall(int uid) {
        return PkgUtils.isSystemCall(uid);
    }

    private static boolean isThanosCalling() {
        return AndroidAppHelper.currentPackageName().contains(BuildProp.THANOS_APP_PKG_NAME_PREFIX);
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // Noop.
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        hookEnforce();
    }
}
