package github.tornaco.android.thanos.services.xposed.hooks.privacy;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ParceledListSlice;
import android.os.Binder;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.secure.IPrivacyManager;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.annotation.XposedHook;

import java.util.Arrays;
import java.util.Set;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions.*;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

@Beta
@XposedHook(targetSdkVersion = {_21, _22, _23, _24, _25, _26, _27, _28, _29})
public class PMSGetInstalledPackagesRegistry implements IXposedHook {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookGetInstalledPkgs(lpparam);
            hookGetInstalledApps(lpparam);
        }
    }

    private void hookGetInstalledPkgs(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.d("hookGetInstalledPkgs...");
        try {
            Class clz = XposedHelpers.findClass("com.android.server.pm.PackageManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(clz,
                    "getInstalledPackages", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);

                            int uid = Binder.getCallingUid();
                            if (PkgUtils.isSystemOrPhoneOrShell(uid)) return;

                            IThanos thanos = ThanosManagerNative.getDefault();
                            if (thanos == null) return;
                            IPrivacyManager priv = thanos.getPrivacyManager();
                            if (priv == null) return;
                            if (!priv.isPrivacyEnabled()) return;

                            boolean enabledUid = priv.isUidPrivacyDataCheat(uid);
                            if (!enabledUid) return;

                            PackageInfo[] cheated = priv.getCheatedInstalledPackagesForUid(uid);
                            if (cheated == null) return;

                            ParceledListSlice<PackageInfo> empty = new ParceledListSlice<>(Arrays.asList(cheated));
                            param.setResult(empty);
                        }
                    });
            Timber.d("hookGetInstalledPkgs OK:" + unHooks);
        } catch (Exception e) {
            Timber.d("Fail hookGetInstalledPkgs:" + e);
        }
    }

    private void hookGetInstalledApps(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.d("hookGetInstalledApps...");
        try {
            Class clz = XposedHelpers.findClass("com.android.server.pm.PackageManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(clz,
                    "getInstalledApplications", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);

                            int uid = Binder.getCallingUid();
                            if (PkgUtils.isSystemOrPhoneOrShell(uid)) return;

                            IThanos thanos = ThanosManagerNative.getDefault();
                            if (thanos == null) return;
                            IPrivacyManager priv = thanos.getPrivacyManager();
                            if (priv == null) return;
                            if (!priv.isPrivacyEnabled()) return;

                            boolean enabledUid = priv.isUidPrivacyDataCheat(uid);
                            if (!enabledUid) return;

                            ApplicationInfo[] cheated = priv.getCheatedInstalledApplicationsUid(uid);
                            if (cheated == null) return;

                            ParceledListSlice<ApplicationInfo> empty = new ParceledListSlice<>(Arrays.asList(cheated));
                            param.setResult(empty);
                        }
                    });
            Timber.d("hookGetInstalledApps OK:" + unHooks);
        } catch (Exception e) {
            Timber.d("Fail hookGetInstalledApps:" + e);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Nothing.
    }
}
