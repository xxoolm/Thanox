package github.tornaco.android.thanos.services.xposed.hooks.task;

import android.app.ActivityManagerNative;
import android.content.ComponentName;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.app.ActivityManagerService;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.annotation.XposedHook;

import java.util.Set;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions.*;

@XposedHook(targetSdkVersion = {_21, _22, _23, _24, _25})
public class RecentTaskBlurRegistryNAndBlow implements IXposedHook {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookScreenshotApplicationsForNAndBelow(lpparam);
        }
    }

    /**
     * @see #onScreenshotApplicationsNAndBelow(XC_MethodHook.MethodHookParam)
     */
    private void hookScreenshotApplicationsForNAndBelow(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.d("hookScreenshotApplicationsForNAndBelow...");

        try {
            Class clz = XposedHelpers.findClass("com.android.server.wm.WindowManagerService",
                    lpparam.classLoader);

            Set unHooks = XposedBridge.hookAllMethods(clz,
                    "screenshotApplications", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            try {
                                onScreenshotApplicationsNAndBelow(param);
                            } catch (Exception e) {
                                Timber.e(e, "Fail onScreenshotApplicationsNAndBelow");
                            }
                        }
                    });
            Timber.d("hookScreenshotApplicationsForNAndBelow OK: " + unHooks);
        } catch (Exception e) {
            Timber.e(e, "Fail hookScreenshotApplicationsForNAndBelow");
        }
    }

    private void onScreenshotApplicationsNAndBelow(XC_MethodHook.MethodHookParam param) throws RemoteException {

        IBinder token = (IBinder) param.args[0];
        ComponentName activityClassForToken = ActivityManagerNative.getDefault().getActivityClassForToken(token);
        String pkgName = activityClassForToken == null ? null : activityClassForToken.getPackageName();

        if (TextUtils.isEmpty(pkgName)) {
            return;
        }

        Timber.v("onScreenshotApplicationsNAndBelow: " + pkgName);
        ActivityManagerService ams = BootStrap.THANOS_X.getActivityManagerService();
        //noinspection ConstantConditions
        if (ams.isRecentTaskBlurEnabled() && ams.isPkgRecentTaskBlurEnabled(pkgName) && param.getResult() != null) {

            Bitmap res = (Bitmap) param.getResult();
            Timber.v("onScreenshotApplicationsNAndBelow. res: " + res);
            int radius = 18;
            float scale = 0.18f;
            Timber.v("onScreenshotApplicationsNAndBelow, bluring, r and s: " + radius + "-" + scale);
            Bitmap blured = (RecentTaskBlurUtil.createBlurredBitmap(res, radius, scale));
            if (blured != null) {
                param.setResult(blured);
            }
        } else {
            Timber.v("onScreenshotApplicationsNAndBelow, blur is disabled...");
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Noop.
    }
}
