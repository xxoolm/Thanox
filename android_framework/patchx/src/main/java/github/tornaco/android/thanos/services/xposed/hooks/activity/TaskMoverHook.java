package github.tornaco.android.thanos.services.xposed.hooks.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor;
import github.tornaco.android.thanos.core.app.activity.IVerifyCallback;
import github.tornaco.android.thanos.core.app.activity.VerifyResult;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.Timber;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

@AllArgsConstructor
class TaskMoverHook implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    @Getter
    private IActivityStackSupervisor verifier;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookTaskMover(lpparam);
        }
    }

    private void hookTaskMover(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.d("hookTaskMover...");
        try {
            final Method moveToFront = methodForTaskMover(lpparam);
            XC_MethodHook.Unhook unhook = XposedBridge.hookMethod(moveToFront, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    try {
                        String pkgName = null;
                        ComponentName componentName = null;
                        Object realActivityObj = XposedHelpers.getObjectField(param.args[0], "realActivity");
                        if (realActivityObj == null) {
                            Timber.e("realActivityObj is null!!!");
                            return;
                        }
                        componentName = (ComponentName) realActivityObj;
                        pkgName = componentName.getPackageName();
                        Timber.v("findTaskToMoveToFrontLocked:" + pkgName);

                        Intent intent = new Intent();
                        intent.setComponent(componentName);
                        intent.setPackage(pkgName);

                        // Package has been passed.
                        if (!getVerifier().shouldVerifyActivityStarting(componentName, pkgName, "findTaskToMoveToFrontLocked")) {
                            getVerifier()
                                    .reportActivityLaunching(intent, "TaskMover: already checked");
                            return;
                        }

                        getVerifier().verifyActivityStarting(null, pkgName, componentName, 0, 0, new IVerifyCallback.Stub() {
                            @Override
                            public void onVerifyResult(int verifyResult, int reason) {
                                if (verifyResult == VerifyResult.ALLOW) {
                                    try {
                                        getVerifier()
                                                .reportActivityLaunching(intent, "TaskMover: checked");
                                        XposedBridge.invokeOriginalMethod(moveToFront,
                                                param.thisObject, param.args);
                                    } catch (Exception e) {
                                        Timber.e("Error@"
                                                + Log.getStackTraceString(e));
                                    }
                                }
                            }
                        });

                        param.setResult(null);

                    } catch (Exception e) {
                        Timber.wtf("Error@hookTaskMover- findTaskToMoveToFrontLocked:" + Log.getStackTraceString(e));
                    }
                }
            });
            Timber.d("hookTaskMover OK:" + unhook);
        } catch (Exception e) {
            Timber.d("hookTaskMover" + Log.getStackTraceString(e));
        }
    }

    @SuppressLint("PrivateApi")
    Method methodForTaskMover(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException, NoSuchMethodException {
        throw new IllegalStateException("Need org.slf4j.impl here");
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        // Noop.
    }
}
