package github.tornaco.android.thanos.core.app;

interface IActivityManager {
    String getCurrentFrontApp();

    void forceStopPackage(String packageName);

    void idlePackage(String packageName);
    boolean isPackageIdle(String packageName);

    boolean checkBroadcastingIntent(in Intent intent);

    boolean checkService(in Intent intent, in ComponentName service, int callerUid);

    boolean checkRestartService(String packageName, in ComponentName componentName);

    boolean checkBroadcast(in Intent intent, int receiverUid, int callerUid);

    boolean checkStartProcess(in ApplicationInfo applicationInfo, String hostType, String hostName);

    void onStartProcessLocked(in ProcessRecord processRecord);

    ProcessRecord[] getRunningAppProcess();
    String[] getRunningAppPackages();

    List<RunningServiceInfo> getRunningServiceLegacy(int max);
    List<RunningAppProcessInfo> getRunningAppProcessLegacy();

    int getRunningAppsCount();

    ProcessRecord[] getRunningAppProcessForPackage(String pkgName);
    boolean isPackageRunning(String pkgName);

    StartRecord[] getStartRecordsByPackageName(String pkgName);
    String[] getStartRecordBlockedPackages();

    long getStartRecordsBlockedCount();
    long getStartRecordBlockedCountByPackageName(String pkgName);


    // 启动管理设置
    boolean isStartBlockEnabled();
    void setStartBlockEnabled(boolean enable);
    void setPkgStartBlockEnabled(String pkgName, boolean enable);
    boolean isPkgStartBlocking(String pkgName);

    // Task removal
    boolean isCleanUpOnTaskRemovalEnabled();
    void setCleanUpOnTaskRemovalEnabled(boolean enable);
    void setPkgCleanUpOnTaskRemovalEnabled(String pkgName, boolean enable);
    boolean isPkgCleanUpOnTaskRemovalEnabled(String pkgName);

    // 后台运行设置
    boolean isBgRestrictEnabled();
    void setBgRestrictEnabled(boolean enable);
    void setPkgBgRestrictEnabled(String pkgName, boolean enable);
    boolean isPkgBgRestricted(String pkgName);

    // Task blur
    boolean isRecentTaskBlurEnabled();
    void setRecentTaskBlurEnabled(boolean enable);
    void setPkgRecentTaskBlurEnabled(String pkgName, boolean enable);
    boolean isPkgRecentTaskBlurEnabled(String pkgName);

    // Audio focused app.
    boolean isBgTaskCleanUpSkipAudioFocusedAppEnabled();
    void setBgTaskCleanUpSkipAudioFocusedAppEnabled(boolean enable);

    // Notification record app.
    boolean isBgTaskCleanUpSkipWhichHasNotificationEnabled();
    void setBgTaskCleanUpSkipWhichHasNotificationEnabled(boolean enable);

    // 后台运行锁屏清理延迟
    void setBgTaskCleanUpDelayTimeMills(long delayMills);
    long getBgTaskCleanUpDelayTimeMills();

    void notifyTaskCreated(int taskId, in ComponentName componentName);

    MemoryInfo getMemoryInfo();
    long[] getProcessPss(in int[] pids);

    void onApplicationCrashing(String eventType, String processName, in ProcessRecord process, String stackTrace);

    String getPackageNameForTaskId(int taskId);

    int isPlatformAppIdleEnabled();

    boolean isSmartStandByEnabled();
    void setSmartStandByEnabled(boolean enable);
    void setPkgSmartStandByEnabled(String pkgName, boolean enable);
    boolean isPkgSmartStandByEnabled(String pkgName);

    String[] getLastRecentUsedPackages(int count);

    int getRecentTaskExcludeSettingForPackage(String pkgName);
    void setRecentTaskExcludeSettingForPackage(String pkgName, int setting);
}