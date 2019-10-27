/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.power;
public interface IPowerManager extends android.os.IInterface
{
  /** Default implementation for IPowerManager. */
  public static class Default implements github.tornaco.android.thanos.core.power.IPowerManager
  {
    @Override public void reboot() throws android.os.RemoteException
    {
    }
    @Override public void softReboot() throws android.os.RemoteException
    {
    }
    @Override public void goToSleep() throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.power.IPowerManager
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.power.IPowerManager";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.power.IPowerManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.power.IPowerManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.power.IPowerManager))) {
        return ((github.tornaco.android.thanos.core.power.IPowerManager)iin);
      }
      return new github.tornaco.android.thanos.core.power.IPowerManager.Stub.Proxy(obj);
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
        case TRANSACTION_reboot:
        {
          data.enforceInterface(descriptor);
          this.reboot();
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_softReboot:
        {
          data.enforceInterface(descriptor);
          this.softReboot();
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_goToSleep:
        {
          data.enforceInterface(descriptor);
          this.goToSleep();
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.power.IPowerManager
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
      @Override public void reboot() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_reboot, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().reboot();
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void softReboot() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_softReboot, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().softReboot();
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void goToSleep() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_goToSleep, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().goToSleep();
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.power.IPowerManager sDefaultImpl;
    }
    static final int TRANSACTION_reboot = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_softReboot = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_goToSleep = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.power.IPowerManager impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static github.tornaco.android.thanos.core.power.IPowerManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void reboot() throws android.os.RemoteException;
  public void softReboot() throws android.os.RemoteException;
  public void goToSleep() throws android.os.RemoteException;
}
