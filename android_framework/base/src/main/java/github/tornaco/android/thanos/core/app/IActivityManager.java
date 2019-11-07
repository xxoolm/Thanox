/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.app;
public interface IActivityManager extends android.os.IInterface
{
  /** Default implementation for IActivityManager. */
  public static class Default implements github.tornaco.android.thanos.core.app.IActivityManager
  {
    @Override public java.lang.String getCurrentFrontApp() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void forceStopPackage(java.lang.String packageName) throws android.os.RemoteException
    {
    }
    @Override public void idlePackage(java.lang.String packageName) throws android.os.RemoteException
    {
    }
    @Override public boolean isPackageIdle(java.lang.String packageName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean checkBroadcastingIntent(android.content.Intent intent) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean checkService(android.content.Intent intent, android.content.ComponentName service, int callerUid) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean checkRestartService(java.lang.String packageName, android.content.ComponentName componentName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean checkBroadcast(android.content.Intent intent, int receiverUid, int callerUid) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean checkStartProcess(android.content.pm.ApplicationInfo applicationInfo, java.lang.String hostType, java.lang.String hostName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void onStartProcessLocked(github.tornaco.android.thanos.core.process.ProcessRecord processRecord) throws android.os.RemoteException
    {
    }
    @Override public github.tornaco.android.thanos.core.process.ProcessRecord[] getRunningAppProcess() throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String[] getRunningAppPackages() throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<android.app.ActivityManager.RunningServiceInfo> getRunningServiceLegacy(int max) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<android.app.ActivityManager.RunningAppProcessInfo> getRunningAppProcessLegacy() throws android.os.RemoteException
    {
      return null;
    }
    @Override public int getRunningAppsCount() throws android.os.RemoteException
    {
      return 0;
    }
    @Override public github.tornaco.android.thanos.core.process.ProcessRecord[] getRunningAppProcessForPackage(java.lang.String pkgName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean isPackageRunning(java.lang.String pkgName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public github.tornaco.android.thanos.core.app.start.StartRecord[] getStartRecordsByPackageName(java.lang.String pkgName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public long getStartRecordsBlockedCount() throws android.os.RemoteException
    {
      return 0L;
    }
    // 启动管理设置

    @Override public boolean isStartBlockEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setStartBlockEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void setPkgStartBlockEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPkgStartBlocking(java.lang.String pkgName) throws android.os.RemoteException
    {
      return false;
    }
    // Task removal

    @Override public boolean isCleanUpOnTaskRemovalEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setCleanUpOnTaskRemovalEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void setPkgCleanUpOnTaskRemovalEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPkgCleanUpOnTaskRemovalEnabled(java.lang.String pkgName) throws android.os.RemoteException
    {
      return false;
    }
    // 后台运行设置

    @Override public boolean isBgRestrictEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setBgRestrictEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void setPkgBgRestrictEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPkgBgRestricted(java.lang.String pkgName) throws android.os.RemoteException
    {
      return false;
    }
    // Task blur

    @Override public boolean isRecentTaskBlurEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setRecentTaskBlurEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void setPkgRecentTaskBlurEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPkgRecentTaskBlurEnabled(java.lang.String pkgName) throws android.os.RemoteException
    {
      return false;
    }
    // Audio focused app.

    @Override public boolean isBgTaskCleanUpSkipAudioFocusedAppEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setBgTaskCleanUpSkipAudioFocusedAppEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    // Notification record app.

    @Override public boolean isBgTaskCleanUpSkipWhichHasNotificationEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setBgTaskCleanUpSkipWhichHasNotificationEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    // 后台运行锁屏清理延迟

    @Override public void setBgTaskCleanUpDelayTimeMills(long delayMills) throws android.os.RemoteException
    {
    }
    @Override public long getBgTaskCleanUpDelayTimeMills() throws android.os.RemoteException
    {
      return 0L;
    }
    @Override public void onTaskRemoving(int callingUid, int taskId) throws android.os.RemoteException
    {
    }
    @Override public void notifyTaskCreated(int taskId, android.content.ComponentName componentName) throws android.os.RemoteException
    {
    }
    @Override public android.app.ActivityManager.MemoryInfo getMemoryInfo() throws android.os.RemoteException
    {
      return null;
    }
    @Override public long[] getProcessPss(int[] pids) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void onApplicationCrashing(java.lang.String eventType, java.lang.String processName, github.tornaco.android.thanos.core.process.ProcessRecord process, java.lang.String stackTrace) throws android.os.RemoteException
    {
    }
    @Override public java.lang.String getPackageNameForTaskId(int taskId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public int isPlatformAppIdleEnabled() throws android.os.RemoteException
    {
      return 0;
    }
    @Override public boolean isSmartStandByEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setSmartStandByEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void setPkgSmartStandByEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPkgSmartStandByEnabled(java.lang.String pkgName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.lang.String[] getLastRecentUsedPackages(int count) throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.app.IActivityManager
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.app.IActivityManager";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.app.IActivityManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.app.IActivityManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.app.IActivityManager))) {
        return ((github.tornaco.android.thanos.core.app.IActivityManager)iin);
      }
      return new github.tornaco.android.thanos.core.app.IActivityManager.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_getCurrentFrontApp:
        {
          data.enforceInterface(descriptor);
          java.lang.String _result = this.getCurrentFrontApp();
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_forceStopPackage:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.forceStopPackage(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_idlePackage:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.idlePackage(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isPackageIdle:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPackageIdle(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_checkBroadcastingIntent:
        {
          data.enforceInterface(descriptor);
          android.content.Intent _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.content.Intent.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          boolean _result = this.checkBroadcastingIntent(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_checkService:
        {
          data.enforceInterface(descriptor);
          android.content.Intent _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.content.Intent.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          android.content.ComponentName _arg1;
          if ((0!=data.readInt())) {
            _arg1 = android.content.ComponentName.CREATOR.createFromParcel(data);
          }
          else {
            _arg1 = null;
          }
          int _arg2;
          _arg2 = data.readInt();
          boolean _result = this.checkService(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_checkRestartService:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          android.content.ComponentName _arg1;
          if ((0!=data.readInt())) {
            _arg1 = android.content.ComponentName.CREATOR.createFromParcel(data);
          }
          else {
            _arg1 = null;
          }
          boolean _result = this.checkRestartService(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_checkBroadcast:
        {
          data.enforceInterface(descriptor);
          android.content.Intent _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.content.Intent.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          boolean _result = this.checkBroadcast(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_checkStartProcess:
        {
          data.enforceInterface(descriptor);
          android.content.pm.ApplicationInfo _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.content.pm.ApplicationInfo.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          boolean _result = this.checkStartProcess(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_onStartProcessLocked:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.process.ProcessRecord _arg0;
          if ((0!=data.readInt())) {
            _arg0 = github.tornaco.android.thanos.core.process.ProcessRecord.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onStartProcessLocked(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getRunningAppProcess:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.process.ProcessRecord[] _result = this.getRunningAppProcess();
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          return true;
        }
        case TRANSACTION_getRunningAppPackages:
        {
          data.enforceInterface(descriptor);
          java.lang.String[] _result = this.getRunningAppPackages();
          reply.writeNoException();
          reply.writeStringArray(_result);
          return true;
        }
        case TRANSACTION_getRunningServiceLegacy:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.util.List<android.app.ActivityManager.RunningServiceInfo> _result = this.getRunningServiceLegacy(_arg0);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_getRunningAppProcessLegacy:
        {
          data.enforceInterface(descriptor);
          java.util.List<android.app.ActivityManager.RunningAppProcessInfo> _result = this.getRunningAppProcessLegacy();
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_getRunningAppsCount:
        {
          data.enforceInterface(descriptor);
          int _result = this.getRunningAppsCount();
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_getRunningAppProcessForPackage:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.process.ProcessRecord[] _result = this.getRunningAppProcessForPackage(_arg0);
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          return true;
        }
        case TRANSACTION_isPackageRunning:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPackageRunning(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_getStartRecordsByPackageName:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.app.start.StartRecord[] _result = this.getStartRecordsByPackageName(_arg0);
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          return true;
        }
        case TRANSACTION_getStartRecordsBlockedCount:
        {
          data.enforceInterface(descriptor);
          long _result = this.getStartRecordsBlockedCount();
          reply.writeNoException();
          reply.writeLong(_result);
          return true;
        }
        case TRANSACTION_isStartBlockEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isStartBlockEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setStartBlockEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setStartBlockEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setPkgStartBlockEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPkgStartBlockEnabled(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isPkgStartBlocking:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPkgStartBlocking(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_isCleanUpOnTaskRemovalEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isCleanUpOnTaskRemovalEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setCleanUpOnTaskRemovalEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setCleanUpOnTaskRemovalEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setPkgCleanUpOnTaskRemovalEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPkgCleanUpOnTaskRemovalEnabled(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isPkgCleanUpOnTaskRemovalEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPkgCleanUpOnTaskRemovalEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_isBgRestrictEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isBgRestrictEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setBgRestrictEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setBgRestrictEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setPkgBgRestrictEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPkgBgRestrictEnabled(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isPkgBgRestricted:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPkgBgRestricted(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_isRecentTaskBlurEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isRecentTaskBlurEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setRecentTaskBlurEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setRecentTaskBlurEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setPkgRecentTaskBlurEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPkgRecentTaskBlurEnabled(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isPkgRecentTaskBlurEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPkgRecentTaskBlurEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_isBgTaskCleanUpSkipAudioFocusedAppEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isBgTaskCleanUpSkipAudioFocusedAppEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setBgTaskCleanUpSkipAudioFocusedAppEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setBgTaskCleanUpSkipAudioFocusedAppEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isBgTaskCleanUpSkipWhichHasNotificationEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isBgTaskCleanUpSkipWhichHasNotificationEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setBgTaskCleanUpSkipWhichHasNotificationEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setBgTaskCleanUpSkipWhichHasNotificationEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setBgTaskCleanUpDelayTimeMills:
        {
          data.enforceInterface(descriptor);
          long _arg0;
          _arg0 = data.readLong();
          this.setBgTaskCleanUpDelayTimeMills(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getBgTaskCleanUpDelayTimeMills:
        {
          data.enforceInterface(descriptor);
          long _result = this.getBgTaskCleanUpDelayTimeMills();
          reply.writeNoException();
          reply.writeLong(_result);
          return true;
        }
        case TRANSACTION_onTaskRemoving:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          this.onTaskRemoving(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_notifyTaskCreated:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          android.content.ComponentName _arg1;
          if ((0!=data.readInt())) {
            _arg1 = android.content.ComponentName.CREATOR.createFromParcel(data);
          }
          else {
            _arg1 = null;
          }
          this.notifyTaskCreated(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getMemoryInfo:
        {
          data.enforceInterface(descriptor);
          android.app.ActivityManager.MemoryInfo _result = this.getMemoryInfo();
          reply.writeNoException();
          if ((_result!=null)) {
            reply.writeInt(1);
            _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          }
          else {
            reply.writeInt(0);
          }
          return true;
        }
        case TRANSACTION_getProcessPss:
        {
          data.enforceInterface(descriptor);
          int[] _arg0;
          _arg0 = data.createIntArray();
          long[] _result = this.getProcessPss(_arg0);
          reply.writeNoException();
          reply.writeLongArray(_result);
          return true;
        }
        case TRANSACTION_onApplicationCrashing:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          github.tornaco.android.thanos.core.process.ProcessRecord _arg2;
          if ((0!=data.readInt())) {
            _arg2 = github.tornaco.android.thanos.core.process.ProcessRecord.CREATOR.createFromParcel(data);
          }
          else {
            _arg2 = null;
          }
          java.lang.String _arg3;
          _arg3 = data.readString();
          this.onApplicationCrashing(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getPackageNameForTaskId:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _result = this.getPackageNameForTaskId(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_isPlatformAppIdleEnabled:
        {
          data.enforceInterface(descriptor);
          int _result = this.isPlatformAppIdleEnabled();
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_isSmartStandByEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isSmartStandByEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setSmartStandByEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setSmartStandByEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setPkgSmartStandByEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPkgSmartStandByEnabled(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isPkgSmartStandByEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPkgSmartStandByEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_getLastRecentUsedPackages:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String[] _result = this.getLastRecentUsedPackages(_arg0);
          reply.writeNoException();
          reply.writeStringArray(_result);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.app.IActivityManager
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public java.lang.String getCurrentFrontApp() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getCurrentFrontApp, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getCurrentFrontApp();
          }
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void forceStopPackage(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_forceStopPackage, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().forceStopPackage(packageName);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void idlePackage(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_idlePackage, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().idlePackage(packageName);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPackageIdle(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPackageIdle, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPackageIdle(packageName);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean checkBroadcastingIntent(android.content.Intent intent) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((intent!=null)) {
            _data.writeInt(1);
            intent.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkBroadcastingIntent, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().checkBroadcastingIntent(intent);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean checkService(android.content.Intent intent, android.content.ComponentName service, int callerUid) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((intent!=null)) {
            _data.writeInt(1);
            intent.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          if ((service!=null)) {
            _data.writeInt(1);
            service.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeInt(callerUid);
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkService, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().checkService(intent, service, callerUid);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean checkRestartService(java.lang.String packageName, android.content.ComponentName componentName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          if ((componentName!=null)) {
            _data.writeInt(1);
            componentName.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkRestartService, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().checkRestartService(packageName, componentName);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean checkBroadcast(android.content.Intent intent, int receiverUid, int callerUid) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((intent!=null)) {
            _data.writeInt(1);
            intent.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeInt(receiverUid);
          _data.writeInt(callerUid);
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkBroadcast, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().checkBroadcast(intent, receiverUid, callerUid);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean checkStartProcess(android.content.pm.ApplicationInfo applicationInfo, java.lang.String hostType, java.lang.String hostName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((applicationInfo!=null)) {
            _data.writeInt(1);
            applicationInfo.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeString(hostType);
          _data.writeString(hostName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkStartProcess, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().checkStartProcess(applicationInfo, hostType, hostName);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void onStartProcessLocked(github.tornaco.android.thanos.core.process.ProcessRecord processRecord) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((processRecord!=null)) {
            _data.writeInt(1);
            processRecord.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onStartProcessLocked, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onStartProcessLocked(processRecord);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public github.tornaco.android.thanos.core.process.ProcessRecord[] getRunningAppProcess() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.process.ProcessRecord[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getRunningAppProcess, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getRunningAppProcess();
          }
          _reply.readException();
          _result = _reply.createTypedArray(github.tornaco.android.thanos.core.process.ProcessRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.lang.String[] getRunningAppPackages() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getRunningAppPackages, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getRunningAppPackages();
          }
          _reply.readException();
          _result = _reply.createStringArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<android.app.ActivityManager.RunningServiceInfo> getRunningServiceLegacy(int max) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.app.ActivityManager.RunningServiceInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(max);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getRunningServiceLegacy, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getRunningServiceLegacy(max);
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(android.app.ActivityManager.RunningServiceInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<android.app.ActivityManager.RunningAppProcessInfo> getRunningAppProcessLegacy() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.app.ActivityManager.RunningAppProcessInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getRunningAppProcessLegacy, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getRunningAppProcessLegacy();
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(android.app.ActivityManager.RunningAppProcessInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int getRunningAppsCount() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getRunningAppsCount, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getRunningAppsCount();
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public github.tornaco.android.thanos.core.process.ProcessRecord[] getRunningAppProcessForPackage(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.process.ProcessRecord[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getRunningAppProcessForPackage, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getRunningAppProcessForPackage(pkgName);
          }
          _reply.readException();
          _result = _reply.createTypedArray(github.tornaco.android.thanos.core.process.ProcessRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isPackageRunning(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPackageRunning, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPackageRunning(pkgName);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public github.tornaco.android.thanos.core.app.start.StartRecord[] getStartRecordsByPackageName(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.app.start.StartRecord[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getStartRecordsByPackageName, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getStartRecordsByPackageName(pkgName);
          }
          _reply.readException();
          _result = _reply.createTypedArray(github.tornaco.android.thanos.core.app.start.StartRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public long getStartRecordsBlockedCount() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        long _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getStartRecordsBlockedCount, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getStartRecordsBlockedCount();
          }
          _reply.readException();
          _result = _reply.readLong();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      // 启动管理设置

      @Override public boolean isStartBlockEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isStartBlockEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isStartBlockEnabled();
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setStartBlockEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setStartBlockEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setStartBlockEnabled(enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setPkgStartBlockEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgStartBlockEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setPkgStartBlockEnabled(pkgName, enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPkgStartBlocking(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgStartBlocking, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPkgStartBlocking(pkgName);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      // Task removal

      @Override public boolean isCleanUpOnTaskRemovalEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isCleanUpOnTaskRemovalEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isCleanUpOnTaskRemovalEnabled();
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setCleanUpOnTaskRemovalEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setCleanUpOnTaskRemovalEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setCleanUpOnTaskRemovalEnabled(enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setPkgCleanUpOnTaskRemovalEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgCleanUpOnTaskRemovalEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setPkgCleanUpOnTaskRemovalEnabled(pkgName, enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPkgCleanUpOnTaskRemovalEnabled(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgCleanUpOnTaskRemovalEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPkgCleanUpOnTaskRemovalEnabled(pkgName);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      // 后台运行设置

      @Override public boolean isBgRestrictEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isBgRestrictEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isBgRestrictEnabled();
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setBgRestrictEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setBgRestrictEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setBgRestrictEnabled(enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setPkgBgRestrictEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgBgRestrictEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setPkgBgRestrictEnabled(pkgName, enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPkgBgRestricted(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgBgRestricted, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPkgBgRestricted(pkgName);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      // Task blur

      @Override public boolean isRecentTaskBlurEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isRecentTaskBlurEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isRecentTaskBlurEnabled();
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setRecentTaskBlurEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setRecentTaskBlurEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setRecentTaskBlurEnabled(enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setPkgRecentTaskBlurEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgRecentTaskBlurEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setPkgRecentTaskBlurEnabled(pkgName, enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPkgRecentTaskBlurEnabled(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgRecentTaskBlurEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPkgRecentTaskBlurEnabled(pkgName);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      // Audio focused app.

      @Override public boolean isBgTaskCleanUpSkipAudioFocusedAppEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isBgTaskCleanUpSkipAudioFocusedAppEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isBgTaskCleanUpSkipAudioFocusedAppEnabled();
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setBgTaskCleanUpSkipAudioFocusedAppEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setBgTaskCleanUpSkipAudioFocusedAppEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setBgTaskCleanUpSkipAudioFocusedAppEnabled(enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      // Notification record app.

      @Override public boolean isBgTaskCleanUpSkipWhichHasNotificationEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isBgTaskCleanUpSkipWhichHasNotificationEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isBgTaskCleanUpSkipWhichHasNotificationEnabled();
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setBgTaskCleanUpSkipWhichHasNotificationEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setBgTaskCleanUpSkipWhichHasNotificationEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setBgTaskCleanUpSkipWhichHasNotificationEnabled(enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      // 后台运行锁屏清理延迟

      @Override public void setBgTaskCleanUpDelayTimeMills(long delayMills) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeLong(delayMills);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setBgTaskCleanUpDelayTimeMills, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setBgTaskCleanUpDelayTimeMills(delayMills);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public long getBgTaskCleanUpDelayTimeMills() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        long _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getBgTaskCleanUpDelayTimeMills, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getBgTaskCleanUpDelayTimeMills();
          }
          _reply.readException();
          _result = _reply.readLong();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void onTaskRemoving(int callingUid, int taskId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(callingUid);
          _data.writeInt(taskId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onTaskRemoving, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onTaskRemoving(callingUid, taskId);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void notifyTaskCreated(int taskId, android.content.ComponentName componentName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(taskId);
          if ((componentName!=null)) {
            _data.writeInt(1);
            componentName.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_notifyTaskCreated, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().notifyTaskCreated(taskId, componentName);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public android.app.ActivityManager.MemoryInfo getMemoryInfo() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.app.ActivityManager.MemoryInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getMemoryInfo, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getMemoryInfo();
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = android.app.ActivityManager.MemoryInfo.CREATOR.createFromParcel(_reply);
          }
          else {
            _result = null;
          }
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public long[] getProcessPss(int[] pids) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        long[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeIntArray(pids);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getProcessPss, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getProcessPss(pids);
          }
          _reply.readException();
          _result = _reply.createLongArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void onApplicationCrashing(java.lang.String eventType, java.lang.String processName, github.tornaco.android.thanos.core.process.ProcessRecord process, java.lang.String stackTrace) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(eventType);
          _data.writeString(processName);
          if ((process!=null)) {
            _data.writeInt(1);
            process.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeString(stackTrace);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onApplicationCrashing, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onApplicationCrashing(eventType, processName, process, stackTrace);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.lang.String getPackageNameForTaskId(int taskId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(taskId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPackageNameForTaskId, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getPackageNameForTaskId(taskId);
          }
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int isPlatformAppIdleEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPlatformAppIdleEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPlatformAppIdleEnabled();
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isSmartStandByEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isSmartStandByEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isSmartStandByEnabled();
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setSmartStandByEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setSmartStandByEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setSmartStandByEnabled(enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setPkgSmartStandByEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgSmartStandByEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setPkgSmartStandByEnabled(pkgName, enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPkgSmartStandByEnabled(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgSmartStandByEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPkgSmartStandByEnabled(pkgName);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.lang.String[] getLastRecentUsedPackages(int count) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(count);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getLastRecentUsedPackages, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getLastRecentUsedPackages(count);
          }
          _reply.readException();
          _result = _reply.createStringArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      public static github.tornaco.android.thanos.core.app.IActivityManager sDefaultImpl;
    }
    static final int TRANSACTION_getCurrentFrontApp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_forceStopPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_idlePackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_isPackageIdle = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_checkBroadcastingIntent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_checkService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_checkRestartService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_checkBroadcast = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_checkStartProcess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_onStartProcessLocked = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_getRunningAppProcess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_getRunningAppPackages = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_getRunningServiceLegacy = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_getRunningAppProcessLegacy = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_getRunningAppsCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_getRunningAppProcessForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_isPackageRunning = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_getStartRecordsByPackageName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    static final int TRANSACTION_getStartRecordsBlockedCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    static final int TRANSACTION_isStartBlockEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
    static final int TRANSACTION_setStartBlockEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
    static final int TRANSACTION_setPkgStartBlockEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
    static final int TRANSACTION_isPkgStartBlocking = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
    static final int TRANSACTION_isCleanUpOnTaskRemovalEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
    static final int TRANSACTION_setCleanUpOnTaskRemovalEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
    static final int TRANSACTION_setPkgCleanUpOnTaskRemovalEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
    static final int TRANSACTION_isPkgCleanUpOnTaskRemovalEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
    static final int TRANSACTION_isBgRestrictEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
    static final int TRANSACTION_setBgRestrictEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
    static final int TRANSACTION_setPkgBgRestrictEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
    static final int TRANSACTION_isPkgBgRestricted = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
    static final int TRANSACTION_isRecentTaskBlurEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
    static final int TRANSACTION_setRecentTaskBlurEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 32);
    static final int TRANSACTION_setPkgRecentTaskBlurEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 33);
    static final int TRANSACTION_isPkgRecentTaskBlurEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 34);
    static final int TRANSACTION_isBgTaskCleanUpSkipAudioFocusedAppEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 35);
    static final int TRANSACTION_setBgTaskCleanUpSkipAudioFocusedAppEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 36);
    static final int TRANSACTION_isBgTaskCleanUpSkipWhichHasNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 37);
    static final int TRANSACTION_setBgTaskCleanUpSkipWhichHasNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 38);
    static final int TRANSACTION_setBgTaskCleanUpDelayTimeMills = (android.os.IBinder.FIRST_CALL_TRANSACTION + 39);
    static final int TRANSACTION_getBgTaskCleanUpDelayTimeMills = (android.os.IBinder.FIRST_CALL_TRANSACTION + 40);
    static final int TRANSACTION_onTaskRemoving = (android.os.IBinder.FIRST_CALL_TRANSACTION + 41);
    static final int TRANSACTION_notifyTaskCreated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 42);
    static final int TRANSACTION_getMemoryInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 43);
    static final int TRANSACTION_getProcessPss = (android.os.IBinder.FIRST_CALL_TRANSACTION + 44);
    static final int TRANSACTION_onApplicationCrashing = (android.os.IBinder.FIRST_CALL_TRANSACTION + 45);
    static final int TRANSACTION_getPackageNameForTaskId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 46);
    static final int TRANSACTION_isPlatformAppIdleEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 47);
    static final int TRANSACTION_isSmartStandByEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 48);
    static final int TRANSACTION_setSmartStandByEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 49);
    static final int TRANSACTION_setPkgSmartStandByEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 50);
    static final int TRANSACTION_isPkgSmartStandByEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 51);
    static final int TRANSACTION_getLastRecentUsedPackages = (android.os.IBinder.FIRST_CALL_TRANSACTION + 52);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.app.IActivityManager impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static github.tornaco.android.thanos.core.app.IActivityManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public java.lang.String getCurrentFrontApp() throws android.os.RemoteException;
  public void forceStopPackage(java.lang.String packageName) throws android.os.RemoteException;
  public void idlePackage(java.lang.String packageName) throws android.os.RemoteException;
  public boolean isPackageIdle(java.lang.String packageName) throws android.os.RemoteException;
  public boolean checkBroadcastingIntent(android.content.Intent intent) throws android.os.RemoteException;
  public boolean checkService(android.content.Intent intent, android.content.ComponentName service, int callerUid) throws android.os.RemoteException;
  public boolean checkRestartService(java.lang.String packageName, android.content.ComponentName componentName) throws android.os.RemoteException;
  public boolean checkBroadcast(android.content.Intent intent, int receiverUid, int callerUid) throws android.os.RemoteException;
  public boolean checkStartProcess(android.content.pm.ApplicationInfo applicationInfo, java.lang.String hostType, java.lang.String hostName) throws android.os.RemoteException;
  public void onStartProcessLocked(github.tornaco.android.thanos.core.process.ProcessRecord processRecord) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.process.ProcessRecord[] getRunningAppProcess() throws android.os.RemoteException;
  public java.lang.String[] getRunningAppPackages() throws android.os.RemoteException;
  public java.util.List<android.app.ActivityManager.RunningServiceInfo> getRunningServiceLegacy(int max) throws android.os.RemoteException;
  public java.util.List<android.app.ActivityManager.RunningAppProcessInfo> getRunningAppProcessLegacy() throws android.os.RemoteException;
  public int getRunningAppsCount() throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.process.ProcessRecord[] getRunningAppProcessForPackage(java.lang.String pkgName) throws android.os.RemoteException;
  public boolean isPackageRunning(java.lang.String pkgName) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.app.start.StartRecord[] getStartRecordsByPackageName(java.lang.String pkgName) throws android.os.RemoteException;
  public long getStartRecordsBlockedCount() throws android.os.RemoteException;
  // 启动管理设置

  public boolean isStartBlockEnabled() throws android.os.RemoteException;
  public void setStartBlockEnabled(boolean enable) throws android.os.RemoteException;
  public void setPkgStartBlockEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException;
  public boolean isPkgStartBlocking(java.lang.String pkgName) throws android.os.RemoteException;
  // Task removal

  public boolean isCleanUpOnTaskRemovalEnabled() throws android.os.RemoteException;
  public void setCleanUpOnTaskRemovalEnabled(boolean enable) throws android.os.RemoteException;
  public void setPkgCleanUpOnTaskRemovalEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException;
  public boolean isPkgCleanUpOnTaskRemovalEnabled(java.lang.String pkgName) throws android.os.RemoteException;
  // 后台运行设置

  public boolean isBgRestrictEnabled() throws android.os.RemoteException;
  public void setBgRestrictEnabled(boolean enable) throws android.os.RemoteException;
  public void setPkgBgRestrictEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException;
  public boolean isPkgBgRestricted(java.lang.String pkgName) throws android.os.RemoteException;
  // Task blur

  public boolean isRecentTaskBlurEnabled() throws android.os.RemoteException;
  public void setRecentTaskBlurEnabled(boolean enable) throws android.os.RemoteException;
  public void setPkgRecentTaskBlurEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException;
  public boolean isPkgRecentTaskBlurEnabled(java.lang.String pkgName) throws android.os.RemoteException;
  // Audio focused app.

  public boolean isBgTaskCleanUpSkipAudioFocusedAppEnabled() throws android.os.RemoteException;
  public void setBgTaskCleanUpSkipAudioFocusedAppEnabled(boolean enable) throws android.os.RemoteException;
  // Notification record app.

  public boolean isBgTaskCleanUpSkipWhichHasNotificationEnabled() throws android.os.RemoteException;
  public void setBgTaskCleanUpSkipWhichHasNotificationEnabled(boolean enable) throws android.os.RemoteException;
  // 后台运行锁屏清理延迟

  public void setBgTaskCleanUpDelayTimeMills(long delayMills) throws android.os.RemoteException;
  public long getBgTaskCleanUpDelayTimeMills() throws android.os.RemoteException;
  public void onTaskRemoving(int callingUid, int taskId) throws android.os.RemoteException;
  public void notifyTaskCreated(int taskId, android.content.ComponentName componentName) throws android.os.RemoteException;
  public android.app.ActivityManager.MemoryInfo getMemoryInfo() throws android.os.RemoteException;
  public long[] getProcessPss(int[] pids) throws android.os.RemoteException;
  public void onApplicationCrashing(java.lang.String eventType, java.lang.String processName, github.tornaco.android.thanos.core.process.ProcessRecord process, java.lang.String stackTrace) throws android.os.RemoteException;
  public java.lang.String getPackageNameForTaskId(int taskId) throws android.os.RemoteException;
  public int isPlatformAppIdleEnabled() throws android.os.RemoteException;
  public boolean isSmartStandByEnabled() throws android.os.RemoteException;
  public void setSmartStandByEnabled(boolean enable) throws android.os.RemoteException;
  public void setPkgSmartStandByEnabled(java.lang.String pkgName, boolean enable) throws android.os.RemoteException;
  public boolean isPkgSmartStandByEnabled(java.lang.String pkgName) throws android.os.RemoteException;
  public java.lang.String[] getLastRecentUsedPackages(int count) throws android.os.RemoteException;
}
