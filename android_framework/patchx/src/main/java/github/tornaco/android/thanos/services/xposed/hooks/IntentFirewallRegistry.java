package github.tornaco.android.thanos.services.xposed.hooks;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.annotation.XposedHook;

import java.util.Set;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions.*;

/**
 * Created by guohao4 on 2017/11/9.
 * Email: Tornaco@163.com
 */

@XposedHook(targetSdkVersion = {_21, _22, _23, _24, _25, _26, _27, _28, _29})
public class IntentFirewallRegistry implements IXposedHook {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookIntentFireWall(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Noop.
    }

    private void hookIntentFireWall(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.i("hookIntentFireWall...");
        try {
            Class hookClass = XposedHelpers.findClass("com.android.server.firewall.IntentFirewall",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(hookClass, "checkService",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            Intent intent = (Intent) param.args[1];
                            ComponentName componentName = (ComponentName) param.args[0];
                            int callerID = (int) param.args[2];
                            boolean res =
                                    BootStrap.THANOS_X.getActivityManagerService()
                                            .checkService(intent, componentName, callerID);
                            if (!res) {
                                param.setResult(false);
                            }
                        }
                    });

            Timber.i("hookIntentFireWall checkService OK:" + unHooks);

            Set unHooks2 = XposedBridge.hookAllMethods(hookClass, "checkBroadcast", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    int callerUid = (int) param.args[1];
                    int recUid = (int) param.args[4];
                    Intent intent = (Intent) param.args[0];
                    if (intent == null) return;
                    boolean res = BootStrap.THANOS_X.getActivityManagerService()
                            .checkBroadcast(intent, recUid, callerUid);
                    if (!res) {
                        param.setResult(false);
                    }
                }
            });

            Timber.i("hookIntentFireWall checkBroadcast OK:" + unHooks2);

            Set unHooks3 = XposedBridge.hookAllMethods(hookClass, "checkIntent",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                        }
                    });
            Timber.i("hookIntentFireWall checkIntent OK:" + unHooks3);
        } catch (Exception e) {
            Timber.i("Fail hookIntentFireWall %s", Log.getStackTraceString(e));
        }
    }


}
