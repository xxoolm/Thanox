/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.profile;
public interface IProfileManager extends android.os.IInterface
{
  /** Default implementation for IProfileManager. */
  public static class Default implements github.tornaco.android.thanos.core.profile.IProfileManager
  {
    @Override public void setAutoApplyForNewInstalledAppsEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isAutoApplyForNewInstalledAppsEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean addRule(java.lang.String ruleString, java.lang.String ruleId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void deleteRule(java.lang.String ruleId) throws android.os.RemoteException
    {
    }
    @Override public void setRuleEnabled(java.lang.String ruleId, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public void isRuleEnabled(java.lang.String ruleId) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.profile.IProfileManager
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.profile.IProfileManager";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.profile.IProfileManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.profile.IProfileManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.profile.IProfileManager))) {
        return ((github.tornaco.android.thanos.core.profile.IProfileManager)iin);
      }
      return new github.tornaco.android.thanos.core.profile.IProfileManager.Stub.Proxy(obj);
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
        case TRANSACTION_setAutoApplyForNewInstalledAppsEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setAutoApplyForNewInstalledAppsEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isAutoApplyForNewInstalledAppsEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isAutoApplyForNewInstalledAppsEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_addRule:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          boolean _result = this.addRule(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_deleteRule:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.deleteRule(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setRuleEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setRuleEnabled(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isRuleEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.isRuleEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.profile.IProfileManager
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
      @Override public void setAutoApplyForNewInstalledAppsEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setAutoApplyForNewInstalledAppsEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setAutoApplyForNewInstalledAppsEnabled(enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isAutoApplyForNewInstalledAppsEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isAutoApplyForNewInstalledAppsEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isAutoApplyForNewInstalledAppsEnabled();
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
      @Override public boolean addRule(java.lang.String ruleString, java.lang.String ruleId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleString);
          _data.writeString(ruleId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addRule, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().addRule(ruleString, ruleId);
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
      @Override public void deleteRule(java.lang.String ruleId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_deleteRule, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().deleteRule(ruleId);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setRuleEnabled(java.lang.String ruleId, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleId);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setRuleEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setRuleEnabled(ruleId, enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void isRuleEnabled(java.lang.String ruleId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isRuleEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().isRuleEnabled(ruleId);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.profile.IProfileManager sDefaultImpl;
    }
    static final int TRANSACTION_setAutoApplyForNewInstalledAppsEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_isAutoApplyForNewInstalledAppsEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_addRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_deleteRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_setRuleEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_isRuleEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.profile.IProfileManager impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static github.tornaco.android.thanos.core.profile.IProfileManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void setAutoApplyForNewInstalledAppsEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isAutoApplyForNewInstalledAppsEnabled() throws android.os.RemoteException;
  public boolean addRule(java.lang.String ruleString, java.lang.String ruleId) throws android.os.RemoteException;
  public void deleteRule(java.lang.String ruleId) throws android.os.RemoteException;
  public void setRuleEnabled(java.lang.String ruleId, boolean enable) throws android.os.RemoteException;
  public void isRuleEnabled(java.lang.String ruleId) throws android.os.RemoteException;
}
