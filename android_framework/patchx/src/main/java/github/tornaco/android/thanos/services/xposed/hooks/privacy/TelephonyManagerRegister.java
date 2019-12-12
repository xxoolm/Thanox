package github.tornaco.android.thanos.services.xposed.hooks.privacy;

import android.app.AndroidAppHelper;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.core.secure.IPrivacyManager;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.FeatureManager;
import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
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

@Beta
@XposedHook(targetSdkVersion = {_21, _22, _23, _24, _25, _26, _27, _28, _29})
public class TelephonyManagerRegister implements IXposedHook {

    @Override
    public void initZygote(StartupParam startupParam) {
        hookTelephonyManagerGetDeviceId();
        hookTelephonyManagerGetLine1Number();
        hookTelephonyManagerGetSimSerial();

        if (FeatureManager.hasFeature(BuildProp.THANOX_FEATURE_PRIVACY_FIELD_IMEI)) {
            hookTelephonyManagerGetImei();
        }
        if (FeatureManager.hasFeature(BuildProp.THANOX_FEATURE_PRIVACY_FIELD_MEID)) {
            hookTelephonyManagerGetMeid();
        }
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
            Timber.e("TelephonyManagerRegister Fail hookTelephonyManagerGetDeviceId: " + Log.getStackTraceString(e));
            ErrorReporter.report("hookTelephonyManagerGetDeviceId", Log.getStackTraceString(e));
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
            Timber.e("TelephonyManagerRegister Fail hookTelephonyManagerGetLine1Number: " + Log.getStackTraceString(e));
            ErrorReporter.report("hookTelephonyManagerGetLine1Number", Log.getStackTraceString(e));
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
            Timber.e("TelephonyManagerRegister Fail hookTelephonyManagerGetSimSerial: " + Log.getStackTraceString(e));
            ErrorReporter.report("hookTelephonyManagerGetSimSerial", Log.getStackTraceString(e));
        }
    }

    private void hookTelephonyManagerGetImei() {
        Timber.d("TelephonyManagerRegister hookTelephonyManagerGetImei...");
        try {
            Class c = XposedHelpers.findClass("android.telephony.TelephonyManager",
                    null);
            Method getImeiWithSlotIndexMethod = XposedHelpers.findMethodExact(c, "getImei", int.class);
            Timber.d("getImeiWithSlotIndexMethod method: %s", getImeiWithSlotIndexMethod);
            Object unHooks = XposedBridge.hookMethod(getImeiWithSlotIndexMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    String callPackageName = AndroidAppHelper.currentPackageName();
                    int slotIndex = (int) param.args[0];
                    Timber.d("getImei: %s, index: %s", callPackageName, slotIndex);

                    if (callPackageName == null) return;

                    IThanos thanos = ThanosManagerNative.getDefault();
                    if (thanos == null) return;
                    IPrivacyManager priv = thanos.getPrivacyManager();
                    if (priv == null) return;
                    if (!priv.isPrivacyEnabled()) return;

                    boolean enabledUid = priv.isPkgPrivacyDataCheat(callPackageName);
                    if (!enabledUid) return;

                    String res = priv.getCheatedImeiForPkg(callPackageName, slotIndex);
                    Timber.w("getImei: %s, index: %s, using value: %s", callPackageName, slotIndex, res);
                    param.setResult(res);

                }
            });
            Timber.d("TelephonyManagerRegister hookTelephonyManagerGetImei OK:" + unHooks);
        } catch (Exception e) {
            Timber.e("TelephonyManagerRegister Fail hookTelephonyManagerGetImei: " + Log.getStackTraceString(e));
            ErrorReporter.report("hookTelephonyManagerGetImei", Log.getStackTraceString(e));
        }
    }

    private void hookTelephonyManagerGetMeid() {
        Timber.d("TelephonyManagerRegister hookTelephonyManagerGetMeid...");
        try {
            Class c = XposedHelpers.findClass("android.telephony.TelephonyManager",
                    null);
            Method getMeidWithSlotIndexMethod = XposedHelpers.findMethodExact(c, "getMeid", int.class);
            Timber.d("getMeidWithSlotIndexMethod method: %s", getMeidWithSlotIndexMethod);
            Object unHooks = XposedBridge.hookMethod(getMeidWithSlotIndexMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    String callPackageName = AndroidAppHelper.currentPackageName();
                    int slotIndex = (int) param.args[0];
                    Timber.d("getMeid: %s, index: %s", callPackageName, slotIndex);

                    if (callPackageName == null) return;

                    IThanos thanos = ThanosManagerNative.getDefault();
                    if (thanos == null) return;
                    IPrivacyManager priv = thanos.getPrivacyManager();
                    if (priv == null) return;
                    if (!priv.isPrivacyEnabled()) return;

                    boolean enabledUid = priv.isPkgPrivacyDataCheat(callPackageName);
                    if (!enabledUid) return;

                    String res = priv.getCheatedMeidForPkg(callPackageName, slotIndex);
                    Timber.w("getMeid: %s, index: %s, using value: %s", callPackageName, slotIndex, res);
                    param.setResult(res);

                }
            });
            Timber.d("TelephonyManagerRegister hookTelephonyManagerGetMeid OK:" + unHooks);
        } catch (Exception e) {
            Timber.e("TelephonyManagerRegister Fail hookTelephonyManagerGetMeid: " + Log.getStackTraceString(e));
            ErrorReporter.report("hookTelephonyManagerGetMeid", Log.getStackTraceString(e));
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        // Noop.
    }
}
