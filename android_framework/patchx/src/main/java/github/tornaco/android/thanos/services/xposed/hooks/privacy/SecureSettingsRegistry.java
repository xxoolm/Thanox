package github.tornaco.android.thanos.services.xposed.hooks.privacy;

import android.app.AndroidAppHelper;
import android.provider.Settings;
import android.util.Log;

import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.android.thanos.services.xposed.XposedLogger;
import github.tornaco.android.thanos.services.xposed.hooks.ErrorReporter;
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

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

// Hook hookGetStringForUser settings.
@Beta
@XposedHook(targetSdkVersion = {_21, _22, _23, _24, _25, _26, _27, _28, _29})
class SecureSettingsRegistry implements IXposedHook {

    private void hookGetStringForUser() {
        Timber.v("hookGetStringForUser...");
        try {
            Class sceclass = XposedHelpers.findClass("android.provider.Settings$Secure",
                    null);
            Set unHooks = XposedBridge.hookAllMethods(sceclass, "getStringForUser",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            String name = String.valueOf(param.args[1]);
                            if (Settings.Secure.ANDROID_ID.equals(name)) {
                                // Use of defined id.
                                IThanos thanox = ThanosManagerNative.getDefault();
                                if (thanox == null) return;
                                if (!thanox.getPrivacyManager().isPrivacyEnabled()) return;

                                String pkgName = AndroidAppHelper.currentPackageName();
                                if (pkgName == null) return;
                                if (!thanox.getPrivacyManager().isPkgPrivacyDataCheat(pkgName)) {
                                    return;
                                }
                                if (!BootStrap.IS_RELEASE_BUILD) {
                                    Log.v(XposedLogger.LOG_PREFIX, "Will handle get ANDROID_ID, pkg:" + pkgName);
                                }
                                String androidId = thanox.getPrivacyManager().getCheatedAndroidIdForPkg(pkgName);
                                if (androidId != null) {
                                    if (!BootStrap.IS_RELEASE_BUILD) {
                                        Log.w(XposedLogger.LOG_PREFIX, "Using user defined ANDROID_ID: " + pkgName + " for: " + pkgName);
                                    }
                                    param.setResult(androidId);
                                }
                            }
                        }
                    });
            Timber.v("hookGetStringForUser OK:" + unHooks);
        } catch (Exception e) {
            Timber.e("Fail hookGetStringForUser: " + Log.getStackTraceString(e));
            ErrorReporter.report("hookGetStringForUser", Log.getStackTraceString(e));
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        // Noop.
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        hookGetStringForUser();
    }
}
