/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.app.activity;
public interface IActivityStackSupervisor extends android.os.IInterface
{
  /** Default implementation for IActivityStackSupervisor. */
  public static class Default implements github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor
  {
    @Override public boolean checkActivity(android.content.ComponentName componentName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public android.content.Intent replaceActivityStartingIntent(android.content.Intent intent) throws android.os.RemoteException
    {
      return null;
    }
    // 是否需要验证该组件，如果没有组件参数的话，需要传入包名

    @Override public boolean shouldVerifyActivityStarting(android.content.ComponentName componentName, java.lang.String pkg, java.lang.String source) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void verifyActivityStarting(android.os.Bundle options, java.lang.String pkg, android.content.ComponentName componentName, int uid, int pid, github.tornaco.android.thanos.core.app.activity.IVerifyCallback callback) throws android.os.RemoteException
    {
    }
    @Override public void reportActivityLaunching(android.content.Intent intent, java.lang.String reason) throws android.os.RemoteException
    {
    }
    @Override public void reportActivityLaunched(android.content.Intent intent, java.lang.String reason) throws android.os.RemoteException
    {
    }
    @Override public java.lang.String getCurrentFrontApp() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setAppLockEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean isAppLockEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean isPackageLocked(java.lang.String pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setPackageLocked(java.lang.String pkg, boolean locked) throws android.os.RemoteException
    {
    }
    @Override public void setVerifyResult(int request, int result, int reason) throws android.os.RemoteException
    {
    }
    @Override public void setLockerMethod(int method) throws android.os.RemoteException
    {
    }
    @Override public int getLockerMethod() throws android.os.RemoteException
    {
      return 0;
    }
    @Override public void setLockerKey(int method, java.lang.String key) throws android.os.RemoteException
    {
    }
    @Override public boolean isLockerKeyValid(int method, java.lang.String key) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean isLockerKeySet(int method) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean isFingerPrintEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setFingerPrintEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void addComponentReplacement(github.tornaco.android.thanos.core.app.component.ComponentReplacement replacement) throws android.os.RemoteException
    {
    }
    @Override public void removeComponentReplacement(github.tornaco.android.thanos.core.app.component.ComponentReplacement replacement) throws android.os.RemoteException
    {
    }
    @Override public github.tornaco.android.thanos.core.app.component.ComponentReplacement[] getComponentReplacements() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setActivityTrampolineEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean isActivityTrampolineEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setShowCurrentComponentViewEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean isShowCurrentComponentViewEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor))) {
        return ((github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor)iin);
      }
      return new github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor.Stub.Proxy(obj);
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
        case TRANSACTION_checkActivity:
        {
          data.enforceInterface(descriptor);
          android.content.ComponentName _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.content.ComponentName.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          boolean _result = this.checkActivity(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_replaceActivityStartingIntent:
        {
          data.enforceInterface(descriptor);
          android.content.Intent _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.content.Intent.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          android.content.Intent _result = this.replaceActivityStartingIntent(_arg0);
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
        case TRANSACTION_shouldVerifyActivityStarting:
        {
          data.enforceInterface(descriptor);
          android.content.ComponentName _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.content.ComponentName.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          boolean _result = this.shouldVerifyActivityStarting(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_verifyActivityStarting:
        {
          data.enforceInterface(descriptor);
          android.os.Bundle _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.os.Bundle.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          java.lang.String _arg1;
          _arg1 = data.readString();
          android.content.ComponentName _arg2;
          if ((0!=data.readInt())) {
            _arg2 = android.content.ComponentName.CREATOR.createFromParcel(data);
          }
          else {
            _arg2 = null;
          }
          int _arg3;
          _arg3 = data.readInt();
          int _arg4;
          _arg4 = data.readInt();
          github.tornaco.android.thanos.core.app.activity.IVerifyCallback _arg5;
          _arg5 = github.tornaco.android.thanos.core.app.activity.IVerifyCallback.Stub.asInterface(data.readStrongBinder());
          this.verifyActivityStarting(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_reportActivityLaunching:
        {
          data.enforceInterface(descriptor);
          android.content.Intent _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.content.Intent.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.reportActivityLaunching(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_reportActivityLaunched:
        {
          data.enforceInterface(descriptor);
          android.content.Intent _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.content.Intent.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.reportActivityLaunched(_arg0, _arg1);
          reply.writeNoException();
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
        case TRANSACTION_setAppLockEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setAppLockEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isAppLockEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isAppLockEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_isPackageLocked:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPackageLocked(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setPackageLocked:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPackageLocked(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setVerifyResult:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          this.setVerifyResult(_arg0, _arg1, _arg2);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setLockerMethod:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          this.setLockerMethod(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getLockerMethod:
        {
          data.enforceInterface(descriptor);
          int _result = this.getLockerMethod();
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_setLockerKey:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.setLockerKey(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isLockerKeyValid:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          boolean _result = this.isLockerKeyValid(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_isLockerKeySet:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          boolean _result = this.isLockerKeySet(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_isFingerPrintEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isFingerPrintEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setFingerPrintEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setFingerPrintEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_addComponentReplacement:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.app.component.ComponentReplacement _arg0;
          if ((0!=data.readInt())) {
            _arg0 = github.tornaco.android.thanos.core.app.component.ComponentReplacement.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.addComponentReplacement(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_removeComponentReplacement:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.app.component.ComponentReplacement _arg0;
          if ((0!=data.readInt())) {
            _arg0 = github.tornaco.android.thanos.core.app.component.ComponentReplacement.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.removeComponentReplacement(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getComponentReplacements:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.app.component.ComponentReplacement[] _result = this.getComponentReplacements();
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          return true;
        }
        case TRANSACTION_setActivityTrampolineEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setActivityTrampolineEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isActivityTrampolineEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isActivityTrampolineEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setShowCurrentComponentViewEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setShowCurrentComponentViewEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isShowCurrentComponentViewEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isShowCurrentComponentViewEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor
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
      @Override public boolean checkActivity(android.content.ComponentName componentName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((componentName!=null)) {
            _data.writeInt(1);
            componentName.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkActivity, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().checkActivity(componentName);
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
      @Override public android.content.Intent replaceActivityStartingIntent(android.content.Intent intent) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.content.Intent _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((intent!=null)) {
            _data.writeInt(1);
            intent.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_replaceActivityStartingIntent, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().replaceActivityStartingIntent(intent);
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = android.content.Intent.CREATOR.createFromParcel(_reply);
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
      // 是否需要验证该组件，如果没有组件参数的话，需要传入包名

      @Override public boolean shouldVerifyActivityStarting(android.content.ComponentName componentName, java.lang.String pkg, java.lang.String source) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((componentName!=null)) {
            _data.writeInt(1);
            componentName.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeString(pkg);
          _data.writeString(source);
          boolean _status = mRemote.transact(Stub.TRANSACTION_shouldVerifyActivityStarting, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().shouldVerifyActivityStarting(componentName, pkg, source);
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
      @Override public void verifyActivityStarting(android.os.Bundle options, java.lang.String pkg, android.content.ComponentName componentName, int uid, int pid, github.tornaco.android.thanos.core.app.activity.IVerifyCallback callback) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((options!=null)) {
            _data.writeInt(1);
            options.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeString(pkg);
          if ((componentName!=null)) {
            _data.writeInt(1);
            componentName.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeInt(uid);
          _data.writeInt(pid);
          _data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_verifyActivityStarting, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().verifyActivityStarting(options, pkg, componentName, uid, pid, callback);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void reportActivityLaunching(android.content.Intent intent, java.lang.String reason) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((intent!=null)) {
            _data.writeInt(1);
            intent.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeString(reason);
          boolean _status = mRemote.transact(Stub.TRANSACTION_reportActivityLaunching, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().reportActivityLaunching(intent, reason);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void reportActivityLaunched(android.content.Intent intent, java.lang.String reason) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((intent!=null)) {
            _data.writeInt(1);
            intent.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeString(reason);
          boolean _status = mRemote.transact(Stub.TRANSACTION_reportActivityLaunched, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().reportActivityLaunched(intent, reason);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
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
      @Override public void setAppLockEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setAppLockEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setAppLockEnabled(enabled);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isAppLockEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isAppLockEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isAppLockEnabled();
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
      @Override public boolean isPackageLocked(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPackageLocked, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPackageLocked(pkg);
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
      @Override public void setPackageLocked(java.lang.String pkg, boolean locked) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          _data.writeInt(((locked)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPackageLocked, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setPackageLocked(pkg, locked);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setVerifyResult(int request, int result, int reason) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(request);
          _data.writeInt(result);
          _data.writeInt(reason);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setVerifyResult, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setVerifyResult(request, result, reason);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setLockerMethod(int method) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(method);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setLockerMethod, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setLockerMethod(method);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public int getLockerMethod() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getLockerMethod, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getLockerMethod();
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
      @Override public void setLockerKey(int method, java.lang.String key) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(method);
          _data.writeString(key);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setLockerKey, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setLockerKey(method, key);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isLockerKeyValid(int method, java.lang.String key) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(method);
          _data.writeString(key);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isLockerKeyValid, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isLockerKeyValid(method, key);
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
      @Override public boolean isLockerKeySet(int method) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(method);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isLockerKeySet, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isLockerKeySet(method);
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
      @Override public boolean isFingerPrintEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isFingerPrintEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isFingerPrintEnabled();
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
      @Override public void setFingerPrintEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setFingerPrintEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setFingerPrintEnabled(enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void addComponentReplacement(github.tornaco.android.thanos.core.app.component.ComponentReplacement replacement) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((replacement!=null)) {
            _data.writeInt(1);
            replacement.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_addComponentReplacement, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().addComponentReplacement(replacement);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void removeComponentReplacement(github.tornaco.android.thanos.core.app.component.ComponentReplacement replacement) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((replacement!=null)) {
            _data.writeInt(1);
            replacement.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_removeComponentReplacement, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().removeComponentReplacement(replacement);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public github.tornaco.android.thanos.core.app.component.ComponentReplacement[] getComponentReplacements() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.app.component.ComponentReplacement[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getComponentReplacements, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getComponentReplacements();
          }
          _reply.readException();
          _result = _reply.createTypedArray(github.tornaco.android.thanos.core.app.component.ComponentReplacement.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setActivityTrampolineEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setActivityTrampolineEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setActivityTrampolineEnabled(enabled);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isActivityTrampolineEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isActivityTrampolineEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isActivityTrampolineEnabled();
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
      @Override public void setShowCurrentComponentViewEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setShowCurrentComponentViewEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setShowCurrentComponentViewEnabled(enabled);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isShowCurrentComponentViewEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isShowCurrentComponentViewEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isShowCurrentComponentViewEnabled();
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
      public static github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor sDefaultImpl;
    }
    static final int TRANSACTION_checkActivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_replaceActivityStartingIntent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_shouldVerifyActivityStarting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_verifyActivityStarting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_reportActivityLaunching = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_reportActivityLaunched = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_getCurrentFrontApp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_setAppLockEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_isAppLockEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_isPackageLocked = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_setPackageLocked = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_setVerifyResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_setLockerMethod = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_getLockerMethod = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_setLockerKey = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_isLockerKeyValid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_isLockerKeySet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_isFingerPrintEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    static final int TRANSACTION_setFingerPrintEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    static final int TRANSACTION_addComponentReplacement = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
    static final int TRANSACTION_removeComponentReplacement = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
    static final int TRANSACTION_getComponentReplacements = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
    static final int TRANSACTION_setActivityTrampolineEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
    static final int TRANSACTION_isActivityTrampolineEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
    static final int TRANSACTION_setShowCurrentComponentViewEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
    static final int TRANSACTION_isShowCurrentComponentViewEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public boolean checkActivity(android.content.ComponentName componentName) throws android.os.RemoteException;
  public android.content.Intent replaceActivityStartingIntent(android.content.Intent intent) throws android.os.RemoteException;
  // 是否需要验证该组件，如果没有组件参数的话，需要传入包名

  public boolean shouldVerifyActivityStarting(android.content.ComponentName componentName, java.lang.String pkg, java.lang.String source) throws android.os.RemoteException;
  public void verifyActivityStarting(android.os.Bundle options, java.lang.String pkg, android.content.ComponentName componentName, int uid, int pid, github.tornaco.android.thanos.core.app.activity.IVerifyCallback callback) throws android.os.RemoteException;
  public void reportActivityLaunching(android.content.Intent intent, java.lang.String reason) throws android.os.RemoteException;
  public void reportActivityLaunched(android.content.Intent intent, java.lang.String reason) throws android.os.RemoteException;
  public java.lang.String getCurrentFrontApp() throws android.os.RemoteException;
  public void setAppLockEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean isAppLockEnabled() throws android.os.RemoteException;
  public boolean isPackageLocked(java.lang.String pkg) throws android.os.RemoteException;
  public void setPackageLocked(java.lang.String pkg, boolean locked) throws android.os.RemoteException;
  public void setVerifyResult(int request, int result, int reason) throws android.os.RemoteException;
  public void setLockerMethod(int method) throws android.os.RemoteException;
  public int getLockerMethod() throws android.os.RemoteException;
  public void setLockerKey(int method, java.lang.String key) throws android.os.RemoteException;
  public boolean isLockerKeyValid(int method, java.lang.String key) throws android.os.RemoteException;
  public boolean isLockerKeySet(int method) throws android.os.RemoteException;
  public boolean isFingerPrintEnabled() throws android.os.RemoteException;
  public void setFingerPrintEnabled(boolean enable) throws android.os.RemoteException;
  public void addComponentReplacement(github.tornaco.android.thanos.core.app.component.ComponentReplacement replacement) throws android.os.RemoteException;
  public void removeComponentReplacement(github.tornaco.android.thanos.core.app.component.ComponentReplacement replacement) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.app.component.ComponentReplacement[] getComponentReplacements() throws android.os.RemoteException;
  public void setActivityTrampolineEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean isActivityTrampolineEnabled() throws android.os.RemoteException;
  public void setShowCurrentComponentViewEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean isShowCurrentComponentViewEnabled() throws android.os.RemoteException;
}
