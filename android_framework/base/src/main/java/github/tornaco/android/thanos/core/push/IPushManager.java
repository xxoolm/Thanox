/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.push;
public interface IPushManager extends android.os.IInterface
{
  /** Default implementation for IPushManager. */
  public static class Default implements github.tornaco.android.thanos.core.push.IPushManager
  {
    @Override public void registerChannel(github.tornaco.android.thanos.core.push.PushChannel channel) throws android.os.RemoteException
    {
    }
    @Override public void unRegisterChannel(github.tornaco.android.thanos.core.push.PushChannel channel) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.push.IPushManager
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.push.IPushManager";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.push.IPushManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.push.IPushManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.push.IPushManager))) {
        return ((github.tornaco.android.thanos.core.push.IPushManager)iin);
      }
      return new github.tornaco.android.thanos.core.push.IPushManager.Stub.Proxy(obj);
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
        case TRANSACTION_registerChannel:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.push.PushChannel _arg0;
          if ((0!=data.readInt())) {
            _arg0 = github.tornaco.android.thanos.core.push.PushChannel.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.registerChannel(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_unRegisterChannel:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.push.PushChannel _arg0;
          if ((0!=data.readInt())) {
            _arg0 = github.tornaco.android.thanos.core.push.PushChannel.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.unRegisterChannel(_arg0);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.push.IPushManager
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
      @Override public void registerChannel(github.tornaco.android.thanos.core.push.PushChannel channel) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((channel!=null)) {
            _data.writeInt(1);
            channel.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerChannel, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().registerChannel(channel);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void unRegisterChannel(github.tornaco.android.thanos.core.push.PushChannel channel) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((channel!=null)) {
            _data.writeInt(1);
            channel.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_unRegisterChannel, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().unRegisterChannel(channel);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.push.IPushManager sDefaultImpl;
    }
    static final int TRANSACTION_registerChannel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_unRegisterChannel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.push.IPushManager impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static github.tornaco.android.thanos.core.push.IPushManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void registerChannel(github.tornaco.android.thanos.core.push.PushChannel channel) throws android.os.RemoteException;
  public void unRegisterChannel(github.tornaco.android.thanos.core.push.PushChannel channel) throws android.os.RemoteException;
}
