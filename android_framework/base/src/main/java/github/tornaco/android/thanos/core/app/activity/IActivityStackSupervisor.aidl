package github.tornaco.android.thanos.core.app.activity;

import github.tornaco.android.thanos.core.app.activity.IVerifyCallback;
import github.tornaco.android.thanos.core.app.component.ComponentReplacement;

interface IActivityStackSupervisor {
    boolean checkActivity(in ComponentName componentName);

    Intent replaceActivityStartingIntent(in Intent intent);

    // 是否需要验证该组件，如果没有组件参数的话，需要传入包名
    boolean shouldVerifyActivityStarting(in ComponentName componentName, String pkg, String source);

    void verifyActivityStarting(in Bundle options, String pkg, in ComponentName componentName,
                    int uid, int pid, in IVerifyCallback callback);

    void reportActivityLaunching(in Intent intent, String reason);

    String getCurrentFrontApp();

    void setAppLockEnabled(boolean enabled);
    boolean isAppLockEnabled();

    boolean isPackageLocked(String pkg);
    void setPackageLocked(String pkg, boolean locked);

    void setVerifyResult(int request, int result, int reason);

    void setLockerMethod(int method);
    int getLockerMethod();

    void setLockerKey(int method, String key);
    boolean isLockerKeyValid(int method, String key);
    boolean isLockerKeySet(int method);

    boolean isFingerPrintEnabled();
    void setFingerPrintEnabled(boolean enable);

    void addComponentReplacement(in ComponentReplacement replacement);
    void removeComponentReplacement(in ComponentReplacement replacement);

    ComponentReplacement[] getComponentReplacements();

    void setActivityTrampolineEnabled(boolean enabled);
    boolean isActivityTrampolineEnabled();
}