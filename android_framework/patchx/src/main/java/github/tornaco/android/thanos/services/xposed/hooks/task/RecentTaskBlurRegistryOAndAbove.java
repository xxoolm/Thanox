package github.tornaco.android.thanos.services.xposed.hooks.task;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.annotation.RequiresApi;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.android.thanos.services.app.ActivityManagerService;
import github.tornaco.android.thanos.services.wm.WindowManagerService;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.annotation.XposedHook;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions.*;

@XposedHook(targetSdkVersion = {_26, _27, _28, _29})
@Beta
public class RecentTaskBlurRegistryOAndAbove implements IXposedHook {
    private static final Executor BLUR_EXE = Executors.newCachedThreadPool();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookTaskSnapshotController(lpparam);
            hookGetTaskSnapshot(lpparam);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void hookTaskSnapshotController(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.d("hookTaskSnapshotController...");

        try {
            Class clz = XposedHelpers.findClass("com.android.server.wm.TaskSnapshotController",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(clz, "snapshotTask", new XC_MethodHook() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    onSnapshotTask(param);
                }
            });
            Timber.d("hookTaskSnapshotController OK: " + unHooks);
        } catch (Exception e) {
            Timber.d("Fail hookTaskSnapshotController: " + e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void hookGetTaskSnapshot(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.d("hookGetTaskSnapshot...");

        try {
            Class clz = XposedHelpers.findClass("com.android.server.am.ActivityManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(clz, "getTaskSnapshot", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    ActivityManager.TaskSnapshot orig = (ActivityManager.TaskSnapshot) param.getResult();
                    ActivityManager.TaskSnapshot taskSnapshot = onGetTaskSnapshot(orig, (Integer) param.args[0]);
                    if (taskSnapshot != null) {
                        param.setResult(taskSnapshot);
                    }
                }
            });
            Timber.d("hookGetTaskSnapshot OK: " + unHooks);
        } catch (Exception e) {
            Timber.d("Fail hookGetTaskSnapshot: " + e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void blurAndCacheAsync(XC_MethodHook.MethodHookParam param) {
        Object taskObj = param.args[0];
        Timber.v("onSnapshotTask, taskObj: " + taskObj);
        int taskId = (int) XposedHelpers.getObjectField(taskObj, "mTaskId");
        Timber.v("onSnapshotTask, taskId: " + taskId);
        long startTimeMills = System.currentTimeMillis();

        WindowManagerService wm = BootStrap.THANOS_X.getWindowManagerService();
        ActivityManagerService am = BootStrap.THANOS_X.getActivityManagerService();

        if (ThanosManagerNative.isServiceInstalled()) {
            String taskPkgName = am.getPackageNameForTaskId(taskId);
            Timber.v("onSnapshotTask, taskPkgName: " + taskPkgName);
            if (taskPkgName == null) return;
            if (am.isPkgRecentTaskBlurEnabled(taskPkgName)) {
                ActivityManager.TaskSnapshot snapshot = (ActivityManager.TaskSnapshot) param.getResult();
                if (snapshot == null) {
                    return;
                }
                try {
                    int[] screenSize = wm.getScreenSize();
                    Timber.v("onSnapshotTask, screenSize: " + Arrays.toString(screenSize));
                    if (screenSize == null) {
                        Timber.v("onSnapshotTask, no screen size");
                        return;
                    }

                    Bitmap hwBitmap = Bitmap.createHardwareBitmap(snapshot.getSnapshot());
                    Timber.v("onSnapshotTask, hwBitmap: " + hwBitmap);
                    if (hwBitmap != null) {
                        Bitmap blurBitmap = blurBitmap(hwBitmap, Pair.create(screenSize[0], screenSize[1]));
                        Timber.v("onSnapshotTask, bitmap: " + blurBitmap);
                        XposedHelpers.setObjectField(snapshot, "mSnapshot", blurBitmap.createGraphicBufferHandle());
                        Timber.v("onSnapshotTask, mSnapshot: " + snapshot);

                        // Cache.
                        BlurTask cachedTask = BlurTask.from(taskPkgName, blurBitmap);
                        BlurTaskCache.getInstance().put(taskPkgName, cachedTask);

                        long timeTaken = System.currentTimeMillis() - startTimeMills;
                        reportBlurTimeIfNeed(timeTaken);
                    }


                } catch (Exception e) {
                    Timber.e("Error TaskSnapshotBuilder " + Log.getStackTraceString(e));
                }
            }
        }
    }

    private ActivityManager.TaskSnapshot onGetTaskSnapshot(ActivityManager.TaskSnapshot orig, int taskId) {
        ActivityManagerService am = BootStrap.THANOS_X.getActivityManagerService();
        String taskPkgName = am.getPackageNameForTaskId(taskId);
        Timber.v("onGetTaskSnapshot, taskPkgName: " + taskPkgName);
        if (taskPkgName == null) {
            return null;
        }
        BlurTask cache = BlurTaskCache.getInstance().get(taskPkgName);
        if (cache == null || cache.bitmap == null) {
            return null;
        }
        XposedHelpers.setObjectField(orig, "mSnapshot", cache.bitmap.createGraphicBufferHandle());
        return orig;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onSnapshotTask(final XC_MethodHook.MethodHookParam param) {
        BLUR_EXE.execute(() -> blurAndCacheAsync(param));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Bitmap blurBitmap(Bitmap hwBitmap, Pair<Integer, Integer> screenSize) {
        return jBlur(hwBitmap, screenSize);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NewApi")
    private Bitmap jBlur(Bitmap hwBitmap, Pair<Integer, Integer> screenSize) {
        Bitmap swBitmap = hwBitmap.copy(Bitmap.Config.ARGB_8888, false);
        Timber.v("jBlur, copy done");
        int br = 16;
        swBitmap = RecentTaskBlurUtil.createBlurredBitmap(swBitmap, br, 0.18f);
        Timber.v("jBlur, done");
        swBitmap = RecentTaskBlurUtil.createScaledBitmap(swBitmap, screenSize.first, screenSize.second);
        Timber.v("jBlur, scale done");
        Bitmap newHwBitmap = swBitmap.copy(Bitmap.Config.HARDWARE, false);
        Timber.v("jBlur, copy done");
        swBitmap.recycle();
        hwBitmap.recycle();
        return newHwBitmap;
    }

    private void reportBlurTimeIfNeed(long timeMills) {
        Timber.v("reportBlurTimeIfNeed, time taken: " + timeMills);
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Noop.
    }
}
