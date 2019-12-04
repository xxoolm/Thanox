package github.tornaco.android.thanos.services.xposed.hooks.privacy;

import android.app.AndroidAppHelper;
import android.util.Log;

import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.core.secure.IPrivacyManager;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
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

@Beta
@XposedHook(targetSdkVersion = {_21, _22, _23, _24, _25, _26, _27, _28, _29})
public class TelephonyManagerRegister implements IXposedHook {

    @Override
    public void initZygote(StartupParam startupParam) {
        hookTelephonyManagerGetDeviceId();
        hookTelephonyManagerGetLine1Number();
        hookTelephonyManagerGetSimSerial();
    }

    private void hookTelephonyManagerGetDeviceId() {
        Timber.d("TelephonyManagerRegister hookTelephonyManagerGetDeviceId...");
        try {
            Class c = XposedHelpers.findClass("android.telephony.TelephonyManager",
                    null);
            Set unHooks = XposedBridge.hookAllMethods(c, "getDeviceId",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);

                            String callPackageName = AndroidAppHelper.currentPackageName();

                            Timber.d("getDeviceId: " + callPackageName);

                            if (callPackageName == null) return;

                            IThanos thanos = ThanosManagerNative.getDefault();
                            if (thanos == null) return;
                            IPrivacyManager priv = thanos.getPrivacyManager();
                            if (priv == null) return;
                            if (!priv.isPrivacyEnabled()) return;

                            boolean enabledUid = priv.isPkgPrivacyDataCheat(callPackageName);
                            if (!enabledUid) return;

                            param.setResult(priv.getCheatedDeviceIdForPkg(callPackageName));
                        }
                    });
            Timber.d("TelephonyManagerRegister hookTelephonyManagerGetDeviceId OK:" + unHooks);
        } catch (Exception e) {
            Timber.d("TelephonyManagerRegister Fail hookTelephonyManagerGetDeviceId: " + Log.getStackTraceString(e));
        }
    }

    private void hookTelephonyManagerGetLine1Number() {
        Timber.d("TelephonyManagerRegister hookTelephonyManagerGetLine1Number...");
        try {
            Class c = XposedHelpers.findClass("android.telephony.TelephonyManager",
                    null);
            Set unHooks = XposedBridge.hookAllMethods(c, "getLine1Number",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            String callPackageName = AndroidAppHelper.currentPackageName();

                            Timber.v("getLine1Number: " + callPackageName);

                            if (callPackageName == null) return;

                            IThanos thanos = ThanosManagerNative.getDefault();
                            if (thanos == null) return;
                            IPrivacyManager priv = thanos.getPrivacyManager();
                            if (priv == null) return;
                            if (!priv.isPrivacyEnabled()) return;

                            boolean enabledUid = priv.isPkgPrivacyDataCheat(callPackageName);
                            if (!enabledUid) return;

                            param.setResult(priv.getCheatedLine1NumberForPkg(callPackageName));

                        }
                    });
            Timber.d("TelephonyManagerRegister hookTelephonyManagerGetLine1Number OK:" + unHooks);
        } catch (Exception e) {
            Timber.d("TelephonyManagerRegister Fail hookTelephonyManagerGetLine1Number: " + Log.getStackTraceString(e));
        }
    }

    private void hookTelephonyManagerGetSimSerial() {
        Timber.d("TelephonyManagerRegister hookTelephonyManagerGetSimSerial...");
        try {
            Class c = XposedHelpers.findClass("android.telephony.TelephonyManager",
                    null);
            Set unHooks = XposedBridge.hookAllMethods(c, "getSimSerialNumber",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);

                            String callPackageName = AndroidAppHelper.currentPackageName();

                            Timber.v("getSimSerialNumber: " + callPackageName);

                            if (callPackageName == null) return;

                            IThanos thanos = ThanosManagerNative.getDefault();
                            if (thanos == null) return;
                            IPrivacyManager priv = thanos.getPrivacyManager();
                            if (priv == null) return;
                            if (!priv.isPrivacyEnabled()) return;

                            boolean enabledUid = priv.isPkgPrivacyDataCheat(callPackageName);
                            if (!enabledUid) return;

                            param.setResult(priv.getCheatedSimSerialNumberForPkg(callPackageName));
                        }
                    });
            Timber.d("TelephonyManagerRegister hookTelephonyManagerGetSimSerial OK:" + unHooks);
        } catch (Exception e) {
            Timber.d("TelephonyManagerRegister Fail hookTelephonyManagerGetSimSerial: " + Log.getStackTraceString(e));
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        // Noop.
    }
}
