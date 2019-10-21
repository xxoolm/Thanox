package github.tornaco.android.thanos.core.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import github.tornaco.android.thanos.core.app.start.StartRecord;
import github.tornaco.android.thanos.core.process.ProcessRecord;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;

@AllArgsConstructor
public class ActivityManager {
    private IActivityManager server;

    @SneakyThrows
    public String getCurrentFrontApp() {
        return server.getCurrentFrontApp();
    }

    @SneakyThrows
    public void forceStopPackage(String packageName) {
        server.forceStopPackage(packageName);
    }

    @SneakyThrows
    public void idlePackage(String packageName) {
        server.idlePackage(packageName);
    }

    @SneakyThrows
    public boolean checkBroadcastingIntent(Intent intent) {
        return server.checkBroadcastingIntent(intent);
    }

    @SneakyThrows
    public boolean checkService(Intent intent, ComponentName service, int callerUid) {
        return server.checkService(intent, service, callerUid);
    }

    @SneakyThrows
    public boolean checkRestartService(String packageName, ComponentName componentName) {
        return server.checkRestartService(packageName, componentName);
    }

    @SneakyThrows
    public boolean checkBroadcast(Intent intent, int receiverUid, int callerUid) {
        return server.checkBroadcast(intent, receiverUid, callerUid);
    }

    @SneakyThrows
    public boolean checkStartProcess(ApplicationInfo applicationInfo, String hostType, String hostName) {
        return server.checkStartProcess(applicationInfo, hostType, hostName);
    }

    @SneakyThrows
    public void onStartProcessLocked(ProcessRecord processRecord) {
        server.onStartProcessLocked(processRecord);
    }

    @SneakyThrows
    public void removeProcessNameLocked(ProcessRecord processRecord) {
        server.removeProcessNameLocked(processRecord);
    }

    @SneakyThrows
    public void addProcessNameLocked(ProcessRecord processRecord) {
        server.addProcessNameLocked(processRecord);
    }

    @SneakyThrows
    public ProcessRecord[] getRunningAppProcess() {
        return server.getRunningAppProcess();
    }

    @SneakyThrows
    public String[] getRunningAppPackages() {
        return server.getRunningAppPackages();
    }

    @SneakyThrows
    public List<android.app.ActivityManager.RunningServiceInfo> getRunningServiceLegacy(int max) {
        return server.getRunningServiceLegacy(max);
    }

    @SneakyThrows
    public List<android.app.ActivityManager.RunningAppProcessInfo> getRunningAppProcessLegacy() {
        return server.getRunningAppProcessLegacy();
    }

    @SneakyThrows
    public int getRunningAppsCount() {
        return server.getRunningAppsCount();
    }

    @SneakyThrows
    public ProcessRecord[] getRunningAppProcessForPackage(String pkgName) {
        return server.getRunningAppProcessForPackage(pkgName);
    }

    @SneakyThrows
    public boolean isPackageRunning(String pkgName) {
        return server.isPackageRunning(pkgName);
    }

    @SneakyThrows
    public StartRecord[] getStartRecordsByPackageName(String pkgName) {
        return server.getStartRecordsByPackageName(pkgName);
    }

    @SneakyThrows
    public long getStartRecordsBlockedCount() {
        return server.getStartRecordsBlockedCount();
    }

    @SneakyThrows
    public void setPkgStartBlockEnabled(String pkgName, boolean enable) {
        server.setPkgStartBlockEnabled(pkgName, enable);
    }

    @SneakyThrows
    public boolean isPkgStartBlocking(String pkgName) {
        return server.isPkgStartBlocking(pkgName);
    }

    @SneakyThrows
    public void setPkgBgRestrictEnabled(String pkgName, boolean enable) {
        server.setPkgBgRestrictEnabled(pkgName, enable);
    }

    @SneakyThrows
    public boolean isPkgBgRestricted(String pkgName) {
        return server.isPkgBgRestricted(pkgName);
    }

    @SneakyThrows
    public void setBgTaskCleanUpDelayTimeMills(long delayMills) {
        server.setBgTaskCleanUpDelayTimeMills(delayMills);
    }

    @SneakyThrows
    public long getBgTaskCleanUpDelayTimeMills() {
        return server.getBgTaskCleanUpDelayTimeMills();
    }

    @SneakyThrows
    public void onTaskRemoving(int callingUid, int taskId) {
        server.onTaskRemoving(callingUid, taskId);
    }

    @SneakyThrows
    public void notifyTaskCreated(int taskId, ComponentName componentName) {
        server.notifyTaskCreated(taskId, componentName);
    }

    @SneakyThrows
    public android.app.ActivityManager.MemoryInfo getMemoryInfo() {
        return server.getMemoryInfo();
    }

    @SneakyThrows
    public long[] getProcessPss(int[] pids) {
        return server.getProcessPss(pids);
    }

    @SneakyThrows
    public boolean isStartBlockEnabled() {
        return server.isStartBlockEnabled();
    }

    @SneakyThrows
    public void setStartBlockEnabled(boolean enable) {
        server.setStartBlockEnabled(enable);
    }

    @SneakyThrows
    public boolean isBgRestrictEnabled() {
        return server.isBgRestrictEnabled();
    }

    @SneakyThrows
    public void setBgRestrictEnabled(boolean enable) {
        server.setBgRestrictEnabled(enable);
    }

    @SneakyThrows
    public boolean isCleanUpOnTaskRemovalEnabled() {
        return server.isCleanUpOnTaskRemovalEnabled();
    }

    @SneakyThrows
    public void setCleanUpOnTaskRemovalEnabled(boolean enable) {
        server.setCleanUpOnTaskRemovalEnabled(enable);
    }

    @SneakyThrows
    public void setPkgCleanUpOnTaskRemovalEnabled(String pkgName, boolean enable) {
        server.setPkgCleanUpOnTaskRemovalEnabled(pkgName, enable);
    }

    @SneakyThrows
    public boolean isPkgCleanUpOnTaskRemovalEnabled(String pkgName) {
        return server.isPkgCleanUpOnTaskRemovalEnabled(pkgName);
    }

    @SneakyThrows
    public boolean isBgTaskCleanUpSkipAudioFocusedAppEnabled() {
        return server.isBgTaskCleanUpSkipAudioFocusedAppEnabled();
    }

    @SneakyThrows
    public void setBgTaskCleanUpSkipAudioFocusedAppEnabled(boolean enable) {
        server.setBgTaskCleanUpSkipAudioFocusedAppEnabled(enable);
    }

    @SneakyThrows
    public boolean isBgTaskCleanUpSkipWhichHasNotificationEnabled() {
        return server.isBgTaskCleanUpSkipWhichHasNotificationEnabled();
    }

    @SneakyThrows
    public void setBgTaskCleanUpSkipWhichHasNotificationEnabled(boolean enable) {
        server.setBgTaskCleanUpSkipWhichHasNotificationEnabled(enable);
    }

    @SneakyThrows
    public boolean isRecentTaskBlurEnabled() {
        return server.isRecentTaskBlurEnabled();
    }

    @SneakyThrows
    public void setRecentTaskBlurEnabled(boolean enable) {
        server.setRecentTaskBlurEnabled(enable);
    }

    @SneakyThrows
    public void setPkgRecentTaskBlurEnabled(String pkgName, boolean enable) {
        server.setPkgRecentTaskBlurEnabled(pkgName, enable);
    }

    @SneakyThrows
    public boolean isPkgRecentTaskBlurEnabled(String pkgName) {
        return server.isPkgRecentTaskBlurEnabled(pkgName);
    }
}
