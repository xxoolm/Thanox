package github.tornaco.android.thanos.services.xposed.hooks.privacy;

import android.app.AppOpsManager;
import android.os.IBinder;
import android.util.Log;

import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.annotation.Keep;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.secure.ops.IAppOpsService;
import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BootStrap;
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

@Beta
@XposedHook(targetSdkVersion = {_21, _22, _23, _24, _25, _26, _27, _28, _29}, active = true)
@Keep
public class OpsServiceRegistry implements IXposedHook {
    private IAppOpsService ops =
            BootStrap.THANOS_X
                    .getAppOpsService();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookOpsFinishOp(lpparam);

            hookCheckOp(lpparam);
            hookNoteOperation(lpparam);
            hookNoteProxyOperation(lpparam);
            hookStartOperation(lpparam);
        }
    }

    private void hookOpsFinishOp(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Set unHooks = XposedBridge.hookAllMethods(opsServiceClass(lpparam),
                    "finishOperation",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            // Report.
                            ops.onFinishOp(
                                    (IBinder) param.args[0],
                                    (int) param.args[1],
                                    (int) param.args[2],
                                    (String) param.args[3]);
                        }
                    });
            Timber.i("hookOpsFinishOp, unhooks %s", unHooks);
        } catch (Throwable e) {
            Timber.i("hookOpsFinishOp error %s", Log.getStackTraceString(e));
        }
    }

    private void hookCheckOp(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.i("hookCheckOp...");
        try {
            Set unHooks = XposedBridge.hookAllMethods(opsServiceClass(lpparam),
                    "checkOperation",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);

                            boolean opsEnabled = ops.isOpsEnabled();
                            if (!opsEnabled) {
                                return;
                            }

                            int code = (int) param.args[0];
                            int uid = (int) param.args[1];
                            String pkgName = (String) param.args[2];
                            int mode = ops.checkOperation(code, uid, pkgName);
                            if (mode == AppOpsManager.MODE_IGNORED) {
                                param.setResult(AppOpsManager.MODE_IGNORED);
                            }
                        }
                    });
            Timber.i("hookCheckOp OK:" + unHooks);
        } catch (Exception e) {
            Timber.e("Fail hookCheckOp: " + Log.getStackTraceString(e));
        }
    }

    private void hookStartOperation(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.i("hookStartOperation...");
        try {
            Set unHooks = XposedBridge.hookAllMethods(opsServiceClass(lpparam),
                    "startOperation",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);

                            boolean opsEnabled = ops.isOpsEnabled();
                            if (!opsEnabled) {
                                return;
                            }

                            int code = (int) param.args[1];
                            int uid = (int) param.args[2];
                            String pkgName = (String) param.args[3];
                            int mode = ops.checkOperation(code, uid, pkgName);
                            if (mode == AppOpsManager.MODE_IGNORED) {
                                param.setResult(AppOpsManager.MODE_IGNORED);
                            } else {
                                // Report.
                                ops.onStartOp(
                                        (IBinder) param.args[0],
                                        (int) param.args[1],
                                        (int) param.args[2],
                                        (String) param.args[3]);
                            }
                        }
                    });
            Timber.i("hookStartOperation OK:" + unHooks);
        } catch (Exception e) {
            Timber.e("Fail hookStartOperation: " + Log.getStackTraceString(e));
        }
    }

    private void hookNoteProxyOperation(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.i("hookNoteProxyOperation...");
        try {
            Set unHooks = XposedBridge.hookAllMethods(opsServiceClass(lpparam),
                    "noteProxyOperation",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);

                            boolean opsEnabled = ops.isOpsEnabled();
                            if (!opsEnabled) {
                                return;
                            }

                            int code = (int) param.args[0];
                            int uid = (int) param.args[2];
                            String pkgName = (String) param.args[3];
                            int mode = ops.checkOperation(code, uid, pkgName);
                            if (mode == AppOpsManager.MODE_IGNORED) {
                                param.setResult(AppOpsManager.MODE_IGNORED);
                            }
                        }
                    });
            Timber.i("hookNoteProxyOperation OK:" + unHooks);
        } catch (Exception e) {
            Timber.e("Fail hookNoteProxyOperation: " + Log.getStackTraceString(e));
        }
    }

    private void hookNoteOperation(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.i("hookNoteOperation...");
        try {
            Set unHooks = XposedBridge.hookAllMethods(opsServiceClass(lpparam),
                    "noteOperation",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);

                            boolean opsEnabled = ops.isOpsEnabled();
                            if (!opsEnabled) {
                                return;
                            }

                            int code = (int) param.args[0];
                            int uid = (int) param.args[1];
                            String pkgName = (String) param.args[2];
                            int mode = ops.checkOperation(code, uid, pkgName);
                            if (mode == AppOpsManager.MODE_IGNORED) {
                                param.setResult(AppOpsManager.MODE_IGNORED);
                            }
                        }
                    });
            Timber.i("hookNoteOperation OK:" + unHooks);
        } catch (Exception e) {
            Timber.e("Fail hookNoteOperation: " + Log.getStackTraceString(e));
        }
    }

    private Class opsServiceClass(XC_LoadPackage.LoadPackageParam lpparam) {
        return XposedHelpers.findClass(
                OsUtils.isQOrAbove()
                        ? "com.android.server.appop.AppOpsService"
                        : "com.android.server.AppOpsService",
                lpparam.classLoader);
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Noop.
    }

}
