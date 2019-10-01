package github.tornaco.android.thanos.services.xposed.hooks.location;

import android.location.Location;
import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.annotation.Keep;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.secure.IPrivacyManager;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.annotation.XposedHook;

import java.lang.reflect.Method;
import java.util.Set;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions.*;

@Beta
@XposedHook(targetSdkVersion = {_21, _22, _23, _24, _25, _26, _27, _28, _29}, active = false)
@Keep
public class LMSRegistry implements IXposedHook {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookNMS$handleLocationChanged(lpparam);
            hookNMS$ReceiverCallLocationChangedLocked(lpparam);
            hookWifi(lpparam);
        }
    }

    private void hookNMS$handleLocationChanged(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class nms = XposedHelpers.findClass("com.android.server.LocationManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(nms, "handleLocationChanged", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Timber.d("handleLocationChanged: %s", param.args[0]);

                }
            });
            Timber.i("hookNMS$handleLocationChanged, unhooks %s", unHooks);
        } catch (Throwable e) {
            Timber.i("hookNMS$handleLocationChanged error %s", Log.getStackTraceString(e));
        }
    }

    private void hookNMS$ReceiverCallLocationChangedLocked(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class receiverClazz = XposedHelpers.findClass("com.android.server.LocationManagerService$Receiver",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(receiverClazz, "callLocationChangedLocked", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);

                    String pkgName = getReceiverPackageName(param.thisObject);
                    Timber.d("callLocationChangedLocked, pkgName: %s", pkgName);
                    if (pkgName == null) {
                        return;
                    }

                    IThanos thanos = ThanosManagerNative.getDefault();
                    if (thanos == null) return;
                    IPrivacyManager priv = thanos.getPrivacyManager();
                    if (priv == null) return;
//                    boolean enabledPkg = priv.isPkgPrivacyDataCheat(pkgName);
//                    if (!enabledPkg) return;

                    Location location = (Location) param.args[0];
                    Location cheated = priv.getCheatedLocationForPkg(pkgName, location);

                    location.setLatitude(cheated.getLatitude());
                    location.setLongitude(cheated.getLongitude());

                    try {
                        @SuppressWarnings("unchecked")
                        Method changeMethod = receiverClazz.getDeclaredMethod("callLocationChangedLocked", Location.class);
                        changeMethod.setAccessible(true);
                        XposedBridge.invokeOriginalMethod(changeMethod, param.thisObject, new Object[]{location});
                        param.setResult(null);
                    } catch (Throwable e) {
                        Timber.e(e, "callMethod: callLocationChangedLocked");
                    }

                    Timber.d("Location cheated for pkg: %s", pkgName);
                }
            });


            XposedBridge.hookAllMethods(receiverClazz, "getLastLocation", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Timber.i("tijiao %s", param.args);
                    Location location = (Location) param.getResult();
                    if (location != null) {
                        location.setLongitude(99);
                        location.setLatitude(99);
                    }
                    param.setResult(location);
                }
            });

            Timber.i("hookNMS$Receiver-callLocationChangedLocked, unhooks %s", unHooks);
        } catch (Throwable e) {
            Timber.i("hookNMS$Receiver-callLocationChangedLocked error %s", Log.getStackTraceString(e));
        }
    }

    private String getReceiverPackageName(Object receiver) {
        try {
            Object identify = XposedHelpers.getObjectField(receiver, "mIdentity");
            return (String) XposedHelpers.getObjectField(identify, "mPackageName");
        } catch (Throwable e) {
            Timber.w("No field mIdentity, try next solution, maybe sdk <= 25");
            try {
                return (String) XposedHelpers.getObjectField(receiver, "mPackageName");
            } catch (Throwable e2) {
                Timber.e(e2, "getReceiverPackageName");
                return null;
            }
        }
    }

    private void hookWifi(XC_LoadPackage.LoadPackageParam lpparam) {
        Class wifiClazz = XposedHelpers.findClass("com.android.server.wifi.WifiServiceImpl",
                lpparam.classLoader);
        Set unHooks = XposedBridge.hookAllMethods(wifiClazz, "getScanResults", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Timber.w("getScanResults: %s, %s", param.args[0], "XZ");
                param.setResult(null);
            }
        });
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Noop.
    }

}
