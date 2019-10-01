package github.tornaco.android.thanos.services.xposed.hooks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor;
import github.tornaco.android.thanos.core.app.activity.IVerifyCallback;
import github.tornaco.android.thanos.core.app.activity.VerifyResult;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.xposed.annotation.XposedHook;
import lombok.Getter;

import java.lang.reflect.Method;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions.*;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

@XposedHook(targetSdkVersion = {_24, _25, _26, _27, _28, _29})
public class LauncherAppServiceRegistry implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    @Getter
    private final IActivityStackSupervisor verifier = BootStrap.THANOS_X
            .getActivityStackSupervisor();

    private void hookVerifyCallingPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.v("LauncherAppServiceSubModule hookVerifyCallingPackage...");
        try {
            Class clz = XposedHelpers.findClass("com.android.server.pm.LauncherAppsService$LauncherAppsImpl",
                    lpparam.classLoader);

            Method verifyMethod = null;

            for (Method m : clz.getDeclaredMethods()) {
                if ("verifyCallingPackage".equals(m.getName())) {
                    verifyMethod = m;
                }
            }

            Timber.d("verifyCallingPackage method: " + verifyMethod);

            if (verifyMethod == null) {
                return;
            }

            Object unHooks = XposedBridge.hookMethod(verifyMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Timber.v("LauncherAppService verifyCallingPackage: " + param.args[0]);
                    param.setResult(null);
                }
            });
            Timber.v("LauncherAppServiceSubModule hookVerifyCallingPackage OK:" + unHooks);
        } catch (Exception e) {
            Timber.v("LauncherAppServiceSubModule Fail hookVerifyCallingPackage: " + Log.getStackTraceString(e));
        }
    }

    private void hookStartShortcut(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.v("LauncherAppServiceSubModule hookStartShortcut...");
        try {
            Class clz = XposedHelpers.findClass("com.android.server.pm.LauncherAppsService$LauncherAppsImpl",
                    lpparam.classLoader);

            Method startShortcutMethod = null;

            for (Method m : clz.getDeclaredMethods()) {
                if ("startShortcut".equals(m.getName())) {
                    startShortcutMethod = m;
                }
            }

            Timber.d("startShortcut method: " + startShortcutMethod);

            if (startShortcutMethod == null) {
                return;
            }

            final Method finalStartShortcutMethod = startShortcutMethod;
            Object unHooks = XposedBridge.hookMethod(startShortcutMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    String pkgName = (String) param.args[1];
                    Bundle op = (Bundle) param.args[4];

                    Timber.v("startShortcut: %s %s", pkgName, op);

                    if (pkgName == null) {
                        return;
                    }

                    Intent intent = new Intent();
                    intent.setPackage(pkgName);

                    if (!getVerifier().shouldVerifyActivityStarting(null, pkgName, "startShortcut")) {
                        getVerifier()
                                .reportActivityLaunching(intent, "LauncherAppService: already checked");
                        return;
                    }

                    getVerifier().verifyActivityStarting(op, pkgName, null, 0, 0, new IVerifyCallback.Stub() {
                        @Override
                        public void onVerifyResult(int verifyResult, int reason) {
                            if (verifyResult == VerifyResult.ALLOW) {
                                try {
                                    getVerifier()
                                            .reportActivityLaunching(intent, "LauncherAppService: checked");
                                    XposedBridge.invokeOriginalMethod(finalStartShortcutMethod, param.thisObject, param.args);
                                } catch (Exception e) {
                                    Timber.wtf("Error@" + Log.getStackTraceString(e));
                                }
                            }
                        }
                    });
                    param.setResult(true);
                }
            });
            Timber.v("LauncherAppServiceSubModule hookStartShortcut OK:" + unHooks);
        } catch (Exception e) {
            Timber.v("LauncherAppServiceSubModule Fail hookStartShortcut: " + Log.getStackTraceString(e));
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookStartShortcut(lpparam);
            hookVerifyCallingPackage(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        // Noop.
    }
}
