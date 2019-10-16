/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.n;
public interface INotificationManager extends android.os.IInterface
{
  /** Default implementation for INotificationManager. */
  public static class Default implements github.tornaco.android.thanos.core.n.INotificationManager
  {
    @Override public github.tornaco.android.thanos.core.n.NotificationRecord[] getNotificationRecordsForPackage(java.lang.String packageName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean hasNotificationRecordsForPackage(java.lang.String packageName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void registerObserver(github.tornaco.android.thanos.core.n.INotificationObserver obs) throws android.os.RemoteException
    {
    }
    @Override public void unRegisterObserver(github.tornaco.android.thanos.core.n.INotificationObserver obs) throws android.os.RemoteException
    {
    }
    @Override public void setScreenOnNotificationEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isScreenOnNotificationEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setScreenOnNotificationEnabledForPkg(java.lang.String pkg, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isScreenOnNotificationEnabledForPkg(java.lang.String pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.n.INotificationManager
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.n.INotificationManager";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.n.INotificationManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.n.INotificationManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.n.INotificationManager))) {
        return ((github.tornaco.android.thanos.core.n.INotificationManager)iin);
      }
      return new github.tornaco.android.thanos.core.n.INotificationManager.Stub.Proxy(obj);
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
        case TRANSACTION_getNotificationRecordsForPackage:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.n.NotificationRecord[] _result = this.getNotificationRecordsForPackage(_arg0);
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          return true;
        }
        case TRANSACTION_hasNotificationRecordsForPackage:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.hasNotificationRecordsForPackage(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_registerObserver:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.n.INotificationObserver _arg0;
          _arg0 = github.tornaco.android.thanos.core.n.INotificationObserver.Stub.asInterface(data.readStrongBinder());
          this.registerObserver(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_unRegisterObserver:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.n.INotificationObserver _arg0;
          _arg0 = github.tornaco.android.thanos.core.n.INotificationObserver.Stub.asInterface(data.readStrongBinder());
          this.unRegisterObserver(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setScreenOnNotificationEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setScreenOnNotificationEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isScreenOnNotificationEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isScreenOnNotificationEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setScreenOnNotificationEnabledForPkg:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setScreenOnNotificationEnabledForPkg(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isScreenOnNotificationEnabledForPkg:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isScreenOnNotificationEnabledForPkg(_arg0);
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
    private static class Proxy implements github.tornaco.android.thanos.core.n.INotificationManager
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
      @Override public github.tornaco.android.thanos.core.n.NotificationRecord[] getNotificationRecordsForPackage(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.n.NotificationRecord[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getNotificationRecordsForPackage, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getNotificationRecordsForPackage(packageName);
          }
          _reply.readException();
          _result = _reply.createTypedArray(github.tornaco.android.thanos.core.n.NotificationRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean hasNotificationRecordsForPackage(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_hasNotificationRecordsForPackage, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().hasNotificationRecordsForPackage(packageName);
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
      @Override public void registerObserver(github.tornaco.android.thanos.core.n.INotificationObserver obs) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((obs!=null))?(obs.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerObserver, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().registerObserver(obs);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void unRegisterObserver(github.tornaco.android.thanos.core.n.INotificationObserver obs) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((obs!=null))?(obs.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_unRegisterObserver, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().unRegisterObserver(obs);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setScreenOnNotificationEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setScreenOnNotificationEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setScreenOnNotificationEnabled(enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isScreenOnNotificationEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isScreenOnNotificationEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isScreenOnNotificationEnabled();
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
      @Override public void setScreenOnNotificationEnabledForPkg(java.lang.String pkg, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setScreenOnNotificationEnabledForPkg, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setScreenOnNotificationEnabledForPkg(pkg, enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isScreenOnNotificationEnabledForPkg(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isScreenOnNotificationEnabledForPkg, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isScreenOnNotificationEnabledForPkg(pkg);
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
      public static github.tornaco.android.thanos.core.n.INotificationManager sDefaultImpl;
    }
    static final int TRANSACTION_getNotificationRecordsForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_hasNotificationRecordsForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_registerObserver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_unRegisterObserver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_setScreenOnNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_isScreenOnNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_setScreenOnNotificationEnabledForPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_isScreenOnNotificationEnabledForPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.n.INotificationManager impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static github.tornaco.android.thanos.core.n.INotificationManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public github.tornaco.android.thanos.core.n.NotificationRecord[] getNotificationRecordsForPackage(java.lang.String packageName) throws android.os.RemoteException;
  public boolean hasNotificationRecordsForPackage(java.lang.String packageName) throws android.os.RemoteException;
  public void registerObserver(github.tornaco.android.thanos.core.n.INotificationObserver obs) throws android.os.RemoteException;
  public void unRegisterObserver(github.tornaco.android.thanos.core.n.INotificationObserver obs) throws android.os.RemoteException;
  public void setScreenOnNotificationEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isScreenOnNotificationEnabled() throws android.os.RemoteException;
  public void setScreenOnNotificationEnabledForPkg(java.lang.String pkg, boolean enable) throws android.os.RemoteException;
  public boolean isScreenOnNotificationEnabledForPkg(java.lang.String pkg) throws android.os.RemoteException;
}
