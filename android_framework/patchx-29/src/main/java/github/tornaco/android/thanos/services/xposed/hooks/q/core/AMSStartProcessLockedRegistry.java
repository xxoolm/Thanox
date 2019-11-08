package github.tornaco.android.thanos.services.xposed.hooks.q.core;

import android.content.pm.ApplicationInfo;
import android.util.Log;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.process.ProcessRecord;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.android.thanos.services.util.ProcessRecordUtils;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.annotation.XposedHook;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._29;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

// Listen for app process added.
@XposedHook(targetSdkVersion = {_29})
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

    // boolean startProcessLocked(ProcessRecord app, HostingRecord hostingRecord,
    //            boolean disableHiddenApiChecks, boolean mountExtStorageFull,
    //            String abiOverride) {
    private void hookStartProcessLocked(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.i("hookStartProcessLocked...");
        try {

            Class clazz = XposedHelpers.findClass("com.android.server.am.ProcessList",
                    lpparam.classLoader);
            Timber.d("clazz=%s", clazz);
            Class processRecordClazz = XposedHelpers.findClass("com.android.server.am.ProcessRecord",
                    lpparam.classLoader);
            Timber.d("processRecordClazz=%s", processRecordClazz);
            Class hostingRecordClazz = XposedHelpers.findClass("com.android.server.am.HostingRecord",
                    lpparam.classLoader);
            Timber.d("hostingRecordClazz=%s", hostingRecordClazz);

            Method method = XposedHelpers.findMethodExactIfExists(clazz, "startProcessLocked",
                    processRecordClazz,
                    hostingRecordClazz,
                    boolean.class,
                    boolean.class,
                    String.class);
            Timber.d("startProcessLocked=%s", method);

            if (method == null) {
                Timber.e("Method: startProcessLocked not found....");
                return;
            }

            XC_MethodHook.Unhook unHooks = XposedBridge.hookMethod(method, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object processRecord = param.args[0];
                    Object hostRecord = param.args[1];
                    if (processRecord != null && hostRecord != null) {
                        ApplicationInfo info = (ApplicationInfo) XposedHelpers.getObjectField(processRecord, "info");
                        String hostType = (String) XposedHelpers.getObjectField(hostRecord, "mHostingType");
                        String hostName = (String) XposedHelpers.getObjectField(hostRecord, "mHostingName");

                        if (info != null) {
                            boolean res = BootStrap.THANOS_X.getActivityManagerService()
                                    .checkStartProcess(info, hostType, hostName);
                            if (!res) {
                                param.setResult(false);
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
