/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.pm;
public interface IPkgManager extends android.os.IInterface
{
  /** Default implementation for IPkgManager. */
  public static class Default implements github.tornaco.android.thanos.core.pm.IPkgManager
  {
    @Override public java.lang.String[] getPkgNameForUid(int uid) throws android.os.RemoteException
    {
      return null;
    }
    @Override public int getUidForPkgName(java.lang.String pkgName) throws android.os.RemoteException
    {
      return 0;
    }
    // ApplicationInfo

    @Override public github.tornaco.android.thanos.core.pm.AppInfo[] getInstalledPkgs(int flags) throws android.os.RemoteException
    {
      return null;
    }
    @Override public github.tornaco.android.thanos.core.pm.AppInfo getAppInfo(java.lang.String pkgName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String[] getWhiteListPkgs() throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean isPkgInWhiteList(java.lang.String pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setComponentEnabledSetting(android.content.ComponentName componentName, int newState, int flags) throws android.os.RemoteException
    {
    }
    @Override public int getComponentEnabledSetting(android.content.ComponentName componentName) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public int getApplicationEnabledSetting(java.lang.String packageName) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public void setApplicationEnabledSetting(java.lang.String packageName, int newState, int flags, boolean tmp) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.pm.IPkgManager
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.pm.IPkgManager";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.pm.IPkgManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.pm.IPkgManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.pm.IPkgManager))) {
        return ((github.tornaco.android.thanos.core.pm.IPkgManager)iin);
      }
      return new github.tornaco.android.thanos.core.pm.IPkgManager.Stub.Proxy(obj);
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
        case TRANSACTION_getPkgNameForUid:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String[] _result = this.getPkgNameForUid(_arg0);
          reply.writeNoException();
          reply.writeStringArray(_result);
          return true;
        }
        case TRANSACTION_getUidForPkgName:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _result = this.getUidForPkgName(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_getInstalledPkgs:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          github.tornaco.android.thanos.core.pm.AppInfo[] _result = this.getInstalledPkgs(_arg0);
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          return true;
        }
        case TRANSACTION_getAppInfo:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.pm.AppInfo _result = this.getAppInfo(_arg0);
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
        case TRANSACTION_getWhiteListPkgs:
        {
          data.enforceInterface(descriptor);
          java.lang.String[] _result = this.getWhiteListPkgs();
          reply.writeNoException();
          reply.writeStringArray(_result);
          return true;
        }
        case TRANSACTION_isPkgInWhiteList:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPkgInWhiteList(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setComponentEnabledSetting:
        {
          data.enforceInterface(descriptor);
          android.content.ComponentName _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.content.ComponentName.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          this.setComponentEnabledSetting(_arg0, _arg1, _arg2);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getComponentEnabledSetting:
        {
          data.enforceInterface(descriptor);
          android.content.ComponentName _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.content.ComponentName.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          int _result = this.getComponentEnabledSetting(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_getApplicationEnabledSetting:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _result = this.getApplicationEnabledSetting(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_setApplicationEnabledSetting:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          boolean _arg3;
          _arg3 = (0!=data.readInt());
          this.setApplicationEnabledSetting(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.pm.IPkgManager
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
      @Override public java.lang.String[] getPkgNameForUid(int uid) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(uid);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPkgNameForUid, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getPkgNameForUid(uid);
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
      @Override public int getUidForPkgName(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getUidForPkgName, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getUidForPkgName(pkgName);
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
      // ApplicationInfo

      @Override public github.tornaco.android.thanos.core.pm.AppInfo[] getInstalledPkgs(int flags) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.pm.AppInfo[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(flags);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getInstalledPkgs, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getInstalledPkgs(flags);
          }
          _reply.readException();
          _result = _reply.createTypedArray(github.tornaco.android.thanos.core.pm.AppInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public github.tornaco.android.thanos.core.pm.AppInfo getAppInfo(java.lang.String pkgName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.pm.AppInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkgName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAppInfo, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getAppInfo(pkgName);
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = github.tornaco.android.thanos.core.pm.AppInfo.CREATOR.createFromParcel(_reply);
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
      @Override public java.lang.String[] getWhiteListPkgs() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getWhiteListPkgs, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getWhiteListPkgs();
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
      @Override public boolean isPkgInWhiteList(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgInWhiteList, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPkgInWhiteList(pkg);
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
      @Override public void setComponentEnabledSetting(android.content.ComponentName componentName, int newState, int flags) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((componentName!=null)) {
            _data.writeInt(1);
            componentName.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          _data.writeInt(newState);
          _data.writeInt(flags);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setComponentEnabledSetting, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setComponentEnabledSetting(componentName, newState, flags);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public int getComponentEnabledSetting(android.content.ComponentName componentName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((componentName!=null)) {
            _data.writeInt(1);
            componentName.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_getComponentEnabledSetting, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getComponentEnabledSetting(componentName);
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
      @Override public int getApplicationEnabledSetting(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getApplicationEnabledSetting, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getApplicationEnabledSetting(packageName);
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
      @Override public void setApplicationEnabledSetting(java.lang.String packageName, int newState, int flags, boolean tmp) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _data.writeInt(newState);
          _data.writeInt(flags);
          _data.writeInt(((tmp)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setApplicationEnabledSetting, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setApplicationEnabledSetting(packageName, newState, flags, tmp);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.pm.IPkgManager sDefaultImpl;
    }
    static final int TRANSACTION_getPkgNameForUid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_getUidForPkgName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_getInstalledPkgs = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_getAppInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_getWhiteListPkgs = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_isPkgInWhiteList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_setComponentEnabledSetting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_getComponentEnabledSetting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_getApplicationEnabledSetting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_setApplicationEnabledSetting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.pm.IPkgManager impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static github.tornaco.android.thanos.core.pm.IPkgManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public java.lang.String[] getPkgNameForUid(int uid) throws android.os.RemoteException;
  public int getUidForPkgName(java.lang.String pkgName) throws android.os.RemoteException;
  // ApplicationInfo

  public github.tornaco.android.thanos.core.pm.AppInfo[] getInstalledPkgs(int flags) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.pm.AppInfo getAppInfo(java.lang.String pkgName) throws android.os.RemoteException;
  public java.lang.String[] getWhiteListPkgs() throws android.os.RemoteException;
  public boolean isPkgInWhiteList(java.lang.String pkg) throws android.os.RemoteException;
  public void setComponentEnabledSetting(android.content.ComponentName componentName, int newState, int flags) throws android.os.RemoteException;
  public int getComponentEnabledSetting(android.content.ComponentName componentName) throws android.os.RemoteException;
  public int getApplicationEnabledSetting(java.lang.String packageName) throws android.os.RemoteException;
  public void setApplicationEnabledSetting(java.lang.String packageName, int newState, int flags, boolean tmp) throws android.os.RemoteException;
}
