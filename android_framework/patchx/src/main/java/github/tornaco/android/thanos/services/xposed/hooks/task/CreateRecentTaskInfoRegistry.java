package github.tornaco.android.thanos.services.xposed.hooks.task;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.annotation.Keep;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.android.thanos.services.xposed.hooks.ErrorReporter;
import github.tornaco.xposed.annotation.XposedHook;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._23;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._24;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._25;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._26;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._27;

@XposedHook(targetSdkVersion = {_23, _24, _25, _26, _27})
@Beta
@Keep
public class CreateRecentTaskInfoRegistry implements IXposedHook {

    protected Class clazzToHook(XC_LoadPackage.LoadPackageParam lpparam) {
        return XposedHelpers.findClass("com.android.server.am.ActivityManagerService",
                lpparam.classLoader);
    }

    protected String methodToHook() {
        return "createRecentTaskInfoFromTaskRecord";
    }

    private void hookCreateRecentTaskInfoFromTaskRecord(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.v("hookCreateRecentTaskInfoFromTaskRecord...");
        try {
            Class clazz = clazzToHook(lpparam);
            Timber.v("hookCreateRecentTaskInfoFromTaskRecord...class:" + clazz);
            Set unHooks = XposedBridge.hookAllMethods(clazz, methodToHook(),
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            ActivityManager.RecentTaskInfo recentTaskInfo = (ActivityManager.RecentTaskInfo) param.getResult();
                            if (recentTaskInfo == null) {
                                return;
                            }

                            Intent baseIntent = recentTaskInfo.baseIntent;
                            if (baseIntent == null) {
                                return;
                            }

                            ComponentName componentName = baseIntent.getComponent();
                            if (componentName == null) {
                                Timber.e("Null comp for base intent: " + baseIntent);
                                return;
                            }

                            int setting = BootStrap.THANOS_X.getActivityManagerService().getRecentTaskExcludeSetting(componentName);
                            if (setting == github.tornaco.android.thanos.core.app.ActivityManager.ExcludeRecentSetting.NONE) {
                                return;
                            }

                            int flags = baseIntent.getFlags();
                            boolean excludeRecent = (flags & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                                    == Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;
                            String pkgName = componentName.getPackageName();
                            if (TextUtils.isEmpty(pkgName)) {
                                return;
                            }

                            if (!BootStrap.IS_RELEASE_BUILD) {
                                Timber.v("-BEFORE createRecentTaskInfoFromTaskRecord excludeRecent: %s comp: %s, setting: %s",
                                        excludeRecent, componentName, setting);
                            }

                            if (setting == github.tornaco.android.thanos.core.app.ActivityManager.ExcludeRecentSetting.EXCLUDE && !excludeRecent) {
                                flags |= Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;
                            }

                            if (setting == github.tornaco.android.thanos.core.app.ActivityManager.ExcludeRecentSetting.INCLUDE && excludeRecent) {
                                flags &= Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;
                            }

                            baseIntent.setFlags(flags);
                            recentTaskInfo.baseIntent = baseIntent;
                            param.setResult(recentTaskInfo);

                            if (!BootStrap.IS_RELEASE_BUILD) {
                                excludeRecent = (flags & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                                        == Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;
                                Timber.v("-AFTER createRecentTaskInfoFromTaskRecord excludeRecent: %s comp: %s", excludeRecent, componentName);
                            }
                        }
                    });
            Timber.v("hookCreateRecentTaskInfoFromTaskRecord OK:" + unHooks);
        } catch (Exception e) {
            Timber.e("Fail hookCreateRecentTaskInfoFromTaskRecord: " + Log.getStackTraceString(e));
            ErrorReporter.report("hookCreateRecentTaskInfoFromTaskRecord", Log.getStackTraceString(e));
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookCreateRecentTaskInfoFromTaskRecord(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Noop
    }
}
