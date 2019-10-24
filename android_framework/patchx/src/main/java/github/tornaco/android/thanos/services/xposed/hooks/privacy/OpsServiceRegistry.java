package github.tornaco.android.thanos.services.xposed.hooks.privacy;

import android.os.IBinder;
import android.util.Log;
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

import java.util.Set;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions.*;

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
            hookOpsStartOp(lpparam);
            hookOpsFinishOp(lpparam);
        }
    }

    private void hookOpsStartOp(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class nms = XposedHelpers.findClass(
                    OsUtils.isQOrAbove()
                            ? "com.android.server.appop.AppOpsService"
                            : "com.android.server.AppOpsService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(nms, "startOperation", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    // Report.
                    ops.onStartOp(
                            (IBinder) param.args[0],
                            (int) param.args[1],
                            (int) param.args[2],
                            (String) param.args[3]);
                }
            });
            Timber.i("hookOpsStartOp, unhooks %s", unHooks);
        } catch (Throwable e) {
            Timber.i("hookOpsStartOp error %s", Log.getStackTraceString(e));
        }
    }

    private void hookOpsFinishOp(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class nms = XposedHelpers.findClass(
                    OsUtils.isQOrAbove()
                            ? "com.android.server.appop.AppOpsService"
                            : "com.android.server.AppOpsService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(nms, "finishOperation", new XC_MethodHook() {
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

    @Override
    public void initZygote(StartupParam startupParam) {
        // Noop.
    }

}
