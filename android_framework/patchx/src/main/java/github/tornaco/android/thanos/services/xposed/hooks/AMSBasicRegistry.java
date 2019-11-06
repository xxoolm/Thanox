package github.tornaco.android.thanos.services.xposed.hooks;

import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.app.IApplicationThread;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.annotation.Nullable;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.process.ProcessRecord;
import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.core.util.obs.ListProxy;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.util.ProcessRecordUtils;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.annotation.XposedHook;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import lombok.AllArgsConstructor;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._21;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._22;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._23;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._24;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._25;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._26;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._27;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._28;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._29;

@AllArgsConstructor
@XposedHook(targetSdkVersion = {_21, _22, _23, _24, _25, _26, _27, _28, _29})
public class AMSBasicRegistry implements IXposedHook {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookAMSStart(lpparam);
            hookAMSSystemReady(lpparam);
            hookAMSShutdown(lpparam);
            hookBroadcastIntent(lpparam);
            hookHandleApplicationCrash(lpparam);
        }
    }

    private void hookAMSStart(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class ams = XposedHelpers.findClass("com.android.server.am.ActivityManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams, "start", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    // Install list proxy.
                    List mLruProcesses =
                            OsUtils.isQOrAbove()
                                    // Q: Moved to ProcessList.java mProcessList
                                    ? ((List) XposedHelpers.getObjectField(XposedHelpers.getObjectField(param.thisObject, "mProcessList"), "mLruProcesses"))
                                    : (List) XposedHelpers.getObjectField(param.thisObject, "mLruProcesses");
                    Timber.d("mLruProcesses: " + mLruProcesses);
                    @SuppressWarnings("unchecked")
                    List proxyList = AMSLruProcessListProxy.newProxy(mLruProcesses);
                    XposedHelpers.setObjectField(OsUtils.isQOrAbove()
                                    ? XposedHelpers.getObjectField(param.thisObject, "mProcessList")
                                    : param.thisObject,
                            "mLruProcesses", proxyList);

                    Context context = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
                    BootStrap.start(context);
                }
            });
            Timber.i("hookAMSStart, unhooks %s", unHooks);
        } catch (Throwable e) {
            Timber.i("hookAMSStart error %s", Log.getStackTraceString(e));
        }
    }

    private void hookAMSSystemReady(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class ams = XposedHelpers.findClass("com.android.server.am.ActivityManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams, "systemReady", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    BootStrap.ready();
                }
            });
            Timber.i("hookAMSSystemReady, unhooks %s", unHooks);
        } catch (Throwable e) {
            Timber.i("hookAMSSystemReady error %s", Log.getStackTraceString(e));
        }
    }

    private void hookAMSShutdown(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class ams = XposedHelpers.findClass("com.android.server.am.ActivityManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams, "shutdown", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    BootStrap.shutdown();
                }
            });
            Timber.i("hookAMSShutdown, unhooks %s", unHooks);
        } catch (Throwable e) {
            Timber.i("hookAMSShutdown error %s", Log.getStackTraceString(e));
        }
    }

    private void hookBroadcastIntent(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class ams = XposedHelpers.findClass("com.android.server.am.ActivityManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams, "broadcastIntent",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            IApplicationThread applicationThread = (IApplicationThread) param.args[0];
                            Intent intent = (Intent) param.args[1];
                            if (intent == null) return;
                            boolean allow = BootStrap.THANOS_X.getActivityManagerService().checkBroadcastingIntent(intent);
                            if (!allow) {
                                param.setResult(ActivityManager.BROADCAST_SUCCESS);
                            }
                        }
                    });
            Timber.i("hookBroadcastIntent, unhooks %s", unHooks);
        } catch (Exception e) {
            Timber.i("hookBroadcastIntent error %s", Log.getStackTraceString(e));
        }
    }

    private void hookHandleApplicationCrash(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class ams = XposedHelpers.findClass("com.android.server.am.ActivityManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams, "handleApplicationCrashInner",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            // String eventType, ProcessRecord r, String processName, CrashInfo crashInfo
                            // crash or native_crash
                            ApplicationErrorReport.CrashInfo crashInfo = (ApplicationErrorReport.CrashInfo) param.args[3];
                            String stackTrace = crashInfo.stackTrace;
                            String eventType = (String) param.args[0];
                            String processName = (String) param.args[2];
                            ProcessRecord processRecord = ProcessRecordUtils.fromLegacy(param.args[1]);
                            BootStrap.THANOS_X
                                    .getActivityManagerService()
                                    .onApplicationCrashing(eventType, processName, processRecord, stackTrace);
                        }
                    });
            Timber.i("handleApplicationCrashInner, unhooks %s", unHooks);
        } catch (Exception e) {
            Timber.i("handleApplicationCrashInner error %s", Log.getStackTraceString(e));
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Nothing.
    }

    private static class AMSLruProcessListProxy<T> extends ListProxy<T> {

        AMSLruProcessListProxy(List<T> orig) {
            super(orig);
        }

        static <T> AMSLruProcessListProxy<T> newProxy(List<T> orig) {
            return new AMSLruProcessListProxy<>(orig);
        }

        @Override
        public T get(int i) {
            return getSafety(i);
        }

        @Nullable
        private T getSafety(int i) {
            int size = size();
            if (i >= size) {
                Timber.e("Trying to get of index %s, but size is %s", i, size);
                return null;
            }
            return super.get(i);
        }

        @Override
        public void add(int i, T e) {
            super.add(i, e);
            Completable.fromRunnable(() -> {
                ProcessRecord processRecord = ProcessRecordUtils.fromLegacy(e);
                if (processRecord == null) {
                    return;
                }
                BootStrap.THANOS_X
                        .getActivityManagerService()
                        .addProcessNameLocked(processRecord);
            }).subscribeOn(Schedulers.trampoline()).subscribe();
        }

        @Override
        public boolean add(T e) {
            Completable.fromRunnable(() -> {
                ProcessRecord processRecord = ProcessRecordUtils.fromLegacy(e);
                if (processRecord == null) {
                    return;
                }
                BootStrap.THANOS_X
                        .getActivityManagerService()
                        .addProcessNameLocked(processRecord);
            }).subscribeOn(Schedulers.trampoline()).subscribe();
            return super.add(e);
        }

        @Override
        public boolean remove(Object object) {
            Completable.fromRunnable(() -> {
                ProcessRecord processRecord = ProcessRecordUtils.fromLegacy(object);
                if (processRecord == null) {
                    return;
                }
                BootStrap.THANOS_X
                        .getActivityManagerService()
                        .removeProcessNameLocked(processRecord);
            }).subscribeOn(Schedulers.trampoline()).subscribe();
            return super.remove(object);
        }

        @Override
        public T remove(int i) {
            T removed = super.remove(i);
            Completable.fromRunnable(() -> {
                ProcessRecord processRecord = ProcessRecordUtils.fromLegacy(removed);
                if (processRecord == null) {
                    return;
                }
                BootStrap.THANOS_X
                        .getActivityManagerService()
                        .removeProcessNameLocked(processRecord);
            }).subscribeOn(Schedulers.trampoline()).subscribe();
            return removed;
        }
    }
}
