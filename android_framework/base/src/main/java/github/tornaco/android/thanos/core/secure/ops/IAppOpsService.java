/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.secure.ops;
public interface IAppOpsService extends android.os.IInterface
{
  /** Default implementation for IAppOpsService. */
  public static class Default implements github.tornaco.android.thanos.core.secure.ops.IAppOpsService
  {
    // Remaining methods are only used in Java.

    @Override public int checkPackage(int uid, java.lang.String packageName) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public java.util.List<android.app.AppOpsManager.PackageOps> getPackagesForOps(int[] ops) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<android.app.AppOpsManager.PackageOps> getOpsForPackage(int uid, java.lang.String packageName, int[] ops) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<android.app.AppOpsManager.PackageOps> getUidOps(int uid, int[] ops) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setUidMode(int code, int uid, int mode) throws android.os.RemoteException
    {
    }
    @Override public void setMode(int code, int uid, java.lang.String packageName, int mode) throws android.os.RemoteException
    {
    }
    @Override public void resetAllModes(int reqUserId, java.lang.String reqPackageName) throws android.os.RemoteException
    {
    }
    @Override public int checkOperation(int code, int uid, java.lang.String packageName) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public void setUserRestrictions(android.os.Bundle restrictions, android.os.IBinder token, int userHandle) throws android.os.RemoteException
    {
    }
    @Override public void setUserRestriction(int code, boolean restricted, android.os.IBinder token, int userHandle, java.lang.String[] exceptionPackages) throws android.os.RemoteException
    {
    }
    @Override public void removeUser(int userHandle) throws android.os.RemoteException
    {
    }
    @Override public boolean isOpsEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setOpsEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public void onStartOp(android.os.IBinder token, int code, int uid, java.lang.String packageName) throws android.os.RemoteException
    {
    }
    @Override public void onFinishOp(android.os.IBinder token, int code, int uid, java.lang.String packageName) throws android.os.RemoteException
    {
    }
    @Override public void setOpRemindEnable(int code, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isOpRemindEnabled(int code) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setPkgOpRemindEnable(java.lang.String pkg, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isPkgOpRemindEnable(java.lang.String pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.secure.ops.IAppOpsService
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.secure.ops.IAppOpsService";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.secure.ops.IAppOpsService interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.secure.ops.IAppOpsService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.secure.ops.IAppOpsService))) {
        return ((github.tornaco.android.thanos.core.secure.ops.IAppOpsService)iin);
      }
      return new github.tornaco.android.thanos.core.secure.ops.IAppOpsService.Stub.Proxy(obj);
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
        case TRANSACTION_checkPackage:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _result = this.checkPackage(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_getPackagesForOps:
        {
          data.enforceInterface(descriptor);
          int[] _arg0;
          _arg0 = data.createIntArray();
          java.util.List<android.app.AppOpsManager.PackageOps> _result = this.getPackagesForOps(_arg0);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_getOpsForPackage:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          int[] _arg2;
          _arg2 = data.createIntArray();
          java.util.List<android.app.AppOpsManager.PackageOps> _result = this.getOpsForPackage(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_getUidOps:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int[] _arg1;
          _arg1 = data.createIntArray();
          java.util.List<android.app.AppOpsManager.PackageOps> _result = this.getUidOps(_arg0, _arg1);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_setUidMode:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          this.setUidMode(_arg0, _arg1, _arg2);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setMode:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String _arg2;
          _arg2 = data.readString();
          int _arg3;
          _arg3 = data.readInt();
          this.setMode(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_resetAllModes:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.resetAllModes(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_checkOperation:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String _arg2;
          _arg2 = data.readString();
          int _result = this.checkOperation(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_setUserRestrictions:
        {
          data.enforceInterface(descriptor);
          android.os.Bundle _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.os.Bundle.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          android.os.IBinder _arg1;
          _arg1 = data.readStrongBinder();
          int _arg2;
          _arg2 = data.readInt();
          this.setUserRestrictions(_arg0, _arg1, _arg2);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setUserRestriction:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          android.os.IBinder _arg2;
          _arg2 = data.readStrongBinder();
          int _arg3;
          _arg3 = data.readInt();
          java.lang.String[] _arg4;
          _arg4 = data.createStringArray();
          this.setUserRestriction(_arg0, _arg1, _arg2, _arg3, _arg4);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_removeUser:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          this.removeUser(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isOpsEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isOpsEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setOpsEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setOpsEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onStartOp:
        {
          data.enforceInterface(descriptor);
          android.os.IBinder _arg0;
          _arg0 = data.readStrongBinder();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          java.lang.String _arg3;
          _arg3 = data.readString();
          this.onStartOp(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onFinishOp:
        {
          data.enforceInterface(descriptor);
          android.os.IBinder _arg0;
          _arg0 = data.readStrongBinder();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          java.lang.String _arg3;
          _arg3 = data.readString();
          this.onFinishOp(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setOpRemindEnable:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setOpRemindEnable(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isOpRemindEnabled:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          boolean _result = this.isOpRemindEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setPkgOpRemindEnable:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPkgOpRemindEnable(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isPkgOpRemindEnable:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPkgOpRemindEnable(_arg0);
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
    private static class Proxy implements github.tornaco.android.thanos.core.secure.ops.IAppOpsService
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
      // Remaining methods are only used in Java.

      @Override public int checkPackage(int uid, java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(uid);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkPackage, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().checkPackage(uid, packageName);
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
      @Override public java.util.List<android.app.AppOpsManager.PackageOps> getPackagesForOps(int[] ops) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.app.AppOpsManager.PackageOps> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeIntArray(ops);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPackagesForOps, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getPackagesForOps(ops);
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(android.app.AppOpsManager.PackageOps.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<android.app.AppOpsManager.PackageOps> getOpsForPackage(int uid, java.lang.String packageName, int[] ops) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.app.AppOpsManager.PackageOps> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(uid);
          _data.writeString(packageName);
          _data.writeIntArray(ops);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getOpsForPackage, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getOpsForPackage(uid, packageName, ops);
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(android.app.AppOpsManager.PackageOps.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<android.app.AppOpsManager.PackageOps> getUidOps(int uid, int[] ops) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.app.AppOpsManager.PackageOps> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(uid);
          _data.writeIntArray(ops);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getUidOps, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getUidOps(uid, ops);
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(android.app.AppOpsManager.PackageOps.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setUidMode(int code, int uid, int mode) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(code);
          _data.writeInt(uid);
          _data.writeInt(mode);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setUidMode, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setUidMode(code, uid, mode);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setMode(int code, int uid, java.lang.String packageName, int mode) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(code);
          _data.writeInt(uid);
          _data.writeString(packageName);
          _data.writeInt(mode);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setMode, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setMode(code, uid, packageName, mode);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void resetAllModes(int reqUserId, java.lang.String reqPackageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(reqUserId);
          _data.writeString(reqPackageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_resetAllModes, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().resetAllModes(reqUserId, reqPackageName);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public int checkOperation(int code, int uid, java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(code);
          _data.writeInt(uid);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkOperation, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().checkOperation(code, uid, packageName);
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
      @Override public void setUserRestrictions(android.os.Bundle restrictions, android.os.IBinder token, int userHandle) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((restrictions!=null)) {
            _data.writeInt(1);
            restrictions.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeStrongBinder(token);
          _data.writeInt(userHandle);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setUserRestrictions, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setUserRestrictions(restrictions, token, userHandle);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setUserRestriction(int code, boolean restricted, android.os.IBinder token, int userHandle, java.lang.String[] exceptionPackages) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(code);
          _data.writeInt(((restricted)?(1):(0)));
          _data.writeStrongBinder(token);
          _data.writeInt(userHandle);
          _data.writeStringArray(exceptionPackages);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setUserRestriction, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setUserRestriction(code, restricted, token, userHandle, exceptionPackages);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void removeUser(int userHandle) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userHandle);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removeUser, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().removeUser(userHandle);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isOpsEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isOpsEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isOpsEnabled();
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
      @Override public void setOpsEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setOpsEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setOpsEnabled(enabled);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void onStartOp(android.os.IBinder token, int code, int uid, java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder(token);
          _data.writeInt(code);
          _data.writeInt(uid);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onStartOp, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onStartOp(token, code, uid, packageName);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void onFinishOp(android.os.IBinder token, int code, int uid, java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder(token);
          _data.writeInt(code);
          _data.writeInt(uid);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onFinishOp, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onFinishOp(token, code, uid, packageName);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setOpRemindEnable(int code, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(code);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setOpRemindEnable, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setOpRemindEnable(code, enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isOpRemindEnabled(int code) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(code);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isOpRemindEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isOpRemindEnabled(code);
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
      @Override public void setPkgOpRemindEnable(java.lang.String pkg, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgOpRemindEnable, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setPkgOpRemindEnable(pkg, enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isPkgOpRemindEnable(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgOpRemindEnable, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPkgOpRemindEnable(pkg);
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
      public static github.tornaco.android.thanos.core.secure.ops.IAppOpsService sDefaultImpl;
    }
    static final int TRANSACTION_checkPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_getPackagesForOps = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_getOpsForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_getUidOps = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_setUidMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_setMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_resetAllModes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_checkOperation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_setUserRestrictions = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_setUserRestriction = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_removeUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_isOpsEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_setOpsEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_onStartOp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_onFinishOp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_setOpRemindEnable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_isOpRemindEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_setPkgOpRemindEnable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    static final int TRANSACTION_isPkgOpRemindEnable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.secure.ops.IAppOpsService impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static github.tornaco.android.thanos.core.secure.ops.IAppOpsService getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  // Remaining methods are only used in Java.

  public int checkPackage(int uid, java.lang.String packageName) throws android.os.RemoteException;
  public java.util.List<android.app.AppOpsManager.PackageOps> getPackagesForOps(int[] ops) throws android.os.RemoteException;
  public java.util.List<android.app.AppOpsManager.PackageOps> getOpsForPackage(int uid, java.lang.String packageName, int[] ops) throws android.os.RemoteException;
  public java.util.List<android.app.AppOpsManager.PackageOps> getUidOps(int uid, int[] ops) throws android.os.RemoteException;
  public void setUidMode(int code, int uid, int mode) throws android.os.RemoteException;
  public void setMode(int code, int uid, java.lang.String packageName, int mode) throws android.os.RemoteException;
  public void resetAllModes(int reqUserId, java.lang.String reqPackageName) throws android.os.RemoteException;
  public int checkOperation(int code, int uid, java.lang.String packageName) throws android.os.RemoteException;
  public void setUserRestrictions(android.os.Bundle restrictions, android.os.IBinder token, int userHandle) throws android.os.RemoteException;
  public void setUserRestriction(int code, boolean restricted, android.os.IBinder token, int userHandle, java.lang.String[] exceptionPackages) throws android.os.RemoteException;
  public void removeUser(int userHandle) throws android.os.RemoteException;
  public boolean isOpsEnabled() throws android.os.RemoteException;
  public void setOpsEnabled(boolean enabled) throws android.os.RemoteException;
  public void onStartOp(android.os.IBinder token, int code, int uid, java.lang.String packageName) throws android.os.RemoteException;
  public void onFinishOp(android.os.IBinder token, int code, int uid, java.lang.String packageName) throws android.os.RemoteException;
  public void setOpRemindEnable(int code, boolean enable) throws android.os.RemoteException;
  public boolean isOpRemindEnabled(int code) throws android.os.RemoteException;
  public void setPkgOpRemindEnable(java.lang.String pkg, boolean enable) throws android.os.RemoteException;
  public boolean isPkgOpRemindEnable(java.lang.String pkg) throws android.os.RemoteException;
}
