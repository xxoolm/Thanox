package github.tornaco.android.thanos.services.xposed.hooks.privacy;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.secure.IPrivacyManager;
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager;
import github.tornaco.android.thanos.core.secure.ops.IAppOpsService;
import github.tornaco.android.thanos.core.util.ArrayUtils;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.core.util.Timber;
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
public class PMSGetInstalledPackagesRegistry implements IXposedHook {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookGetInstalledPkgs(lpparam);
            hookGetInstalledApps(lpparam);
            hookQueryIntentActivities(lpparam);
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

                            IAppOpsService ops = thanos.getAppOpsService();
                            if (ops != null && ops.isOpsEnabled()) {
                                String[] pkgNames = thanos.getPkgManager()
                                        .getPkgNameForUid(uid);
                                if (!ArrayUtils.isEmpty(pkgNames)) for (String pkg : pkgNames) {
                                    int mode = ops.checkOperation(
                                            AppOpsManager.OP_GET_INSTALLED_PACKAGES,
                                            uid,
                                            pkg);
                                    if (mode == AppOpsManager.MODE_IGNORED) {
                                        Timber.v("getInstalledPackages, Op denied for %s OP_GET_INSTALLED_PACKAGES", pkg);
                                        ParceledListSlice<PackageInfo> empty = new ParceledListSlice<>(new ArrayList<>(0));
                                        param.setResult(empty);
                                        break;
                                    }
                                }
                            }

                            IPrivacyManager priv = thanos.getPrivacyManager();
                            if (priv != null && priv.isPrivacyEnabled()) {
                                boolean enabledUid = priv.isUidPrivacyDataCheat(uid);
                                if (!enabledUid) return;

                                PackageInfo[] cheated = priv.getCheatedInstalledPackagesForUid(uid);
                                if (cheated == null) return;

                                Timber.v("getInstalledPackages, priv cheat for %s", uid);
                                ParceledListSlice<PackageInfo> empty = new ParceledListSlice<>(Arrays.asList(cheated));
                                param.setResult(empty);
                            }
                        }
                    });
            Timber.d("hookGetInstalledPkgs OK:" + unHooks);
        } catch (Exception e) {
            Timber.e("Fail hookGetInstalledPkgs:" + e);
            ErrorReporter.report("hookGetInstalledPkgs", Log.getStackTraceString(e));
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

                            // Check op.
                            IAppOpsService ops = thanos.getAppOpsService();
                            if (ops != null && ops.isOpsEnabled()) {
                                String[] pkgNames = thanos.getPkgManager()
                                        .getPkgNameForUid(uid);
                                if (!ArrayUtils.isEmpty(pkgNames)) for (String pkg : pkgNames) {
                                    int mode = ops.checkOperation(
                                            AppOpsManager.OP_GET_INSTALLED_PACKAGES,
                                            uid,
                                            pkg);
                                    if (mode == AppOpsManager.MODE_IGNORED) {
                                        Timber.v("getInstalledApplications, Op denied for %s OP_GET_INSTALLED_PACKAGES", pkg);
                                        ParceledListSlice<ApplicationInfo> empty = new ParceledListSlice<>(new ArrayList<>(0));
                                        param.setResult(empty);
                                        break;
                                    }
                                }
                            }

                            // Check priv.
                            IPrivacyManager priv = thanos.getPrivacyManager();
                            if (priv != null && priv.isPrivacyEnabled()) {
                                boolean enabledUid = priv.isUidPrivacyDataCheat(uid);
                                if (!enabledUid) return;

                                ApplicationInfo[] cheated = priv.getCheatedInstalledApplicationsUid(uid);
                                if (cheated == null) return;

                                Timber.v("getInstalledApplications, priv cheat for %s", uid);
                                ParceledListSlice<ApplicationInfo> empty = new ParceledListSlice<>(Arrays.asList(cheated));
                                param.setResult(empty);
                            }
                        }
                    });
            Timber.d("hookGetInstalledApps OK:" + unHooks);
        } catch (Exception e) {
            Timber.d("Fail hookGetInstalledApps:" + e);
            ErrorReporter.report("hookGetInstalledApps", Log.getStackTraceString(e));
        }
    }

    private void hookQueryIntentActivities(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.d("hookQueryIntentActivities...");
        try {
            Class clz = XposedHelpers.findClass("com.android.server.pm.PackageManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(clz,
                    "queryIntentActivities", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);

                            int uid = Binder.getCallingUid();
                            if (PkgUtils.isSystemOrPhoneOrShell(uid)) return;

                            IThanos thanos = ThanosManagerNative.getDefault();
                            if (thanos == null) return;

                            IAppOpsService ops = thanos.getAppOpsService();
                            if (ops != null && ops.isOpsEnabled()) {
                                String[] pkgNames = thanos.getPkgManager()
                                        .getPkgNameForUid(uid);
                                if (!ArrayUtils.isEmpty(pkgNames)) for (String pkg : pkgNames) {
                                    int mode = ops.checkOperation(
                                            AppOpsManager.OP_QUERY_INTENT_ACTIVITIES,
                                            uid,
                                            pkg);
                                    if (mode == AppOpsManager.MODE_IGNORED) {
                                        Timber.v("queryIntentActivities, Op denied for %s OP_QUERY_INTENT_ACTIVITIES", pkg);
                                        ParceledListSlice<PackageInfo> empty = new ParceledListSlice<>(new ArrayList<>(0));
                                        param.setResult(empty);
                                        break;
                                    }
                                }
                            }
                        }
                    });
            Timber.d("hookQueryIntentActivities OK:" + unHooks);
        } catch (Exception e) {
            Timber.d("Fail hookQueryIntentActivities:" + e);
            ErrorReporter.report("hookQueryIntentActivities", Log.getStackTraceString(e));
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Nothing.
    }
}
