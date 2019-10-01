package github.tornaco.android.thanos.services.xposed.hooks.activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor;
import github.tornaco.android.thanos.core.app.activity.IVerifyCallback;
import github.tornaco.android.thanos.core.app.activity.VerifyResult;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.core.util.Timber;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

@AllArgsConstructor
class ActivityStartHook implements IXposedHookLoadPackage {

    @Getter
    private IActivityStackSupervisor verifier;

    private void hookStartActivityMayWait(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.v("hookStartActivityMayWait...");
        try {
            Class clz = clzForStartActivityMayWait(lpparam);

            // Search method.
            String targetMethodName = "startActivityMayWait";
            int matchCount = 0;
            int activityOptsIndex = -1;
            int intentIndex = -1;
            Method startActivityMayWaitMethod = null;
            if (clz != null) {
                for (Method m : clz.getDeclaredMethods()) {
                    if (m.getName().equals(targetMethodName)) {
                        startActivityMayWaitMethod = m;
                        startActivityMayWaitMethod.setAccessible(true);
                        matchCount++;

                        Class[] classes = m.getParameterTypes();
                        for (int i = 0; i < classes.length; i++) {
                            if (Bundle.class == classes[i]) {
                                activityOptsIndex = i;
                            } else if (Intent.class == classes[i]) {
                                intentIndex = i;
                            }
                        }

                        if (activityOptsIndex >= 0 && intentIndex >= 0) {
                            break;
                        }
                    }
                }
            }

            if (startActivityMayWaitMethod == null) {
                Timber.wtf("*** FATAL can not find startActivityMayWait method ***");
                return;
            }

            if (matchCount > 1) {
                Timber.wtf("*** FATAL more than 1 startActivityMayWait method ***");
                // return;
            }

            if (intentIndex < 0) {
                Timber.wtf("*** FATAL can not find intentIndex ***");
                return;
            }

            Timber.d("startActivityMayWait method:" + startActivityMayWaitMethod);
            Timber.d("intentIndex index:" + intentIndex);
            Timber.d("activityOptsIndex index:" + activityOptsIndex);

            final int finalActivityOptsIndex = activityOptsIndex;
            final int finalIntentIndex = intentIndex;

            final Method finalStartActivityMayWaitMethod = startActivityMayWaitMethod;
            XC_MethodHook.Unhook unhook = XposedBridge.hookMethod(startActivityMayWaitMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    try {
                        Intent intent =
                                finalIntentIndex > 0 ?
                                        (Intent) param.args[finalIntentIndex]
                                        : null;
                        if (intent == null) {
                            return;
                        }

                        // Use checked Intent instead of previous one.
                        Intent checkedIntent = getVerifier().replaceActivityStartingIntent(intent);
                        if (checkedIntent != null) {
                            intent = checkedIntent;
                            param.args[finalIntentIndex] = intent;
                        } else {
                            param.setResult(ActivityManager.START_SUCCESS);
                            return;
                        }

                        ComponentName componentName = intent.getComponent();
                        if (componentName == null) {
                            return;
                        }

                        // Incas the component is disabled.
                        boolean checkRes = getVerifier().checkActivity(componentName);
                        if (!checkRes) {
                            param.setResult(ActivityManager.START_SUCCESS);
                            return;
                        }

                        final String pkgName = componentName.getPackageName();

                        boolean isHomeIntent = PkgUtils.isHomeIntent(intent);
                        if (isHomeIntent) {
                            getVerifier()
                                    .reportActivityLaunching(intent, "ActivityStart:isHomeIntent");
                            return;
                        }

                        // Package has been passed.
                        if (!getVerifier().shouldVerifyActivityStarting(componentName, pkgName, "startActivityMayWait")) {
                            getVerifier()
                                    .reportActivityLaunching(intent, "ActivityStart: already checked");
                            return;
                        }

                        Bundle options =
                                finalActivityOptsIndex > 0 ?
                                        (Bundle) param.args[finalActivityOptsIndex]
                                        : null;

                        Timber.w("Verifying %s", componentName);

                        Intent finalIntent = intent;
                        getVerifier().verifyActivityStarting(
                                options,
                                pkgName,
                                componentName,
                                0,
                                0,
                                new IVerifyCallback.Stub() {
                                    @Override
                                    public void onVerifyResult(int verifyResult, int reason) {
                                        if (verifyResult == VerifyResult.ALLOW) {
                                            try {
                                                getVerifier()
                                                        .reportActivityLaunching(finalIntent, "ActivityStart: checked");
                                                XposedBridge.invokeOriginalMethod(finalStartActivityMayWaitMethod,
                                                        param.thisObject, param.args);
                                            } catch (Exception e) {
                                                Timber.wtf("Error@" + Log.getStackTraceString(e));
                                            }
                                        }
                                    }
                                });
                        param.setResult(ActivityManager.START_SUCCESS);
                    } catch (Throwable e) {
                        // replacing did not work.. but no reason to crash the VM! Log the error and go on.
                        Timber.wtf("Error@startActivityMayWaitMethod:" + Log.getStackTraceString(e));
                    }
                }
            });
            Timber.d("hookStartActivityMayWait OK: " + unhook);
        } catch (Exception e) {
            Timber.wtf("Fail hookStartActivityMayWait:" + e);
        }
    }

    Class clzForStartActivityMayWait(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException {
        throw new IllegalStateException("Need impl here");
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookStartActivityMayWait(lpparam);
        }
    }
}
