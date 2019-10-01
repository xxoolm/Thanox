package github.tornaco.android.thanos.services.xposed.hooks;

import android.content.pm.ApplicationInfo;
import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.process.ProcessRecord;
import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.android.thanos.services.util.ProcessRecordUtils;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.annotation.XposedHook;

import java.util.Set;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions.*;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

// Listen for app process added.
@XposedHook(targetSdkVersion = {_21, _22, _23, _24, _25, _26, _27, _28, _29})
@Beta
public class AMSStartProcessLockedRegistry implements IXposedHook {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookStartProcessLocked(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Noop.
    }

    private void hookStartProcessLocked(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.i("hookStartProcessLocked...");
        try {
            Class ams = XposedHelpers.findClass("com.android.server.am.ActivityManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams, "startProcessLocked",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            Object processRecord = param.args[0];
                            // We only hook the method with ProcessRecord param class.
                            if (processRecord instanceof String) return;
                            if (!processRecord.getClass().getName().contains("ProcessRecord")) {
                                return;
                            }
                            // Check args.
                            // Multiple method exists.
                            // This is the fastest way to make it as compat on all platform.
                            // I have no time to check M/O/L, this is based on N.
                            // Sorry:(
                            if (param.args.length <= 4) {
                                return;
                            }
                            if (processRecord != null) {
                                ApplicationInfo info = (ApplicationInfo) XposedHelpers.getObjectField(processRecord, "info");
                                String hostType = (String) param.args[1];
                                String hostName = (String) param.args[2];
                                if (info != null) {
                                    boolean res = BootStrap.THANOS_X.getActivityManagerService()
                                            .checkStartProcess(info, hostType, hostName);
                                    if (!res) {
                                        // P require a boolean value
                                        // @return {@code true} if process start is successful, false otherwise.
                                        param.setResult(OsUtils.isPOrAbove() ? false : null);
                                    } else {
                                        ProcessRecord pr = ProcessRecordUtils.fromLegacy(processRecord);
                                        if (processRecord == null) {
                                            return;
                                        }
                                        BootStrap.THANOS_X.getActivityManagerService().onStartProcessLocked(pr);
                                    }
                                }
                            }
                        }
                    });
            Timber.i("hookStartProcessLocked OK:" + unHooks);
        } catch (Exception e) {
            Timber.i("Fail hookStartProcessLocked: " + Log.getStackTraceString(e));
        }
    }


}
