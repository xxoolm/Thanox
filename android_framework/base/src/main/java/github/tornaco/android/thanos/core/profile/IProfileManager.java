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
    @Override public void addRule(java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleAddCallback callback, int format) throws android.os.RemoteException
    {
    }
    @Override public void deleteRule(java.lang.String ruleId) throws android.os.RemoteException
    {
    }
    @Override public boolean enableRule(java.lang.String ruleId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean disableRule(java.lang.String ruleId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean isRuleEnabled(java.lang.String ruleId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void checkRule(java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleCheckCallback callback, int format) throws android.os.RemoteException
    {
    }
    @Override public RuleInfo[] getAllRules() throws android.os.RemoteException
    {
      return null;
    }
    @Override public RuleInfo[] getEnabledRules() throws android.os.RemoteException
    {
      return null;
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
          github.tornaco.android.thanos.core.profile.IRuleAddCallback _arg1;
          _arg1 = github.tornaco.android.thanos.core.profile.IRuleAddCallback.Stub.asInterface(data.readStrongBinder());
          int _arg2;
          _arg2 = data.readInt();
          this.addRule(_arg0, _arg1, _arg2);
          reply.writeNoException();
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
        case TRANSACTION_enableRule:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.enableRule(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_disableRule:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.disableRule(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_isRuleEnabled:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isRuleEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_checkRule:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.profile.IRuleCheckCallback _arg1;
          _arg1 = github.tornaco.android.thanos.core.profile.IRuleCheckCallback.Stub.asInterface(data.readStrongBinder());
          int _arg2;
          _arg2 = data.readInt();
          this.checkRule(_arg0, _arg1, _arg2);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getAllRules:
        {
          data.enforceInterface(descriptor);
          RuleInfo[] _result = this.getAllRules();
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          return true;
        }
        case TRANSACTION_getEnabledRules:
        {
          data.enforceInterface(descriptor);
          RuleInfo[] _result = this.getEnabledRules();
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
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
      @Override public void addRule(java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleAddCallback callback, int format) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleJson);
          _data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
          _data.writeInt(format);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addRule, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().addRule(ruleJson, callback, format);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
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
      @Override public boolean enableRule(java.lang.String ruleId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_enableRule, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().enableRule(ruleId);
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
      @Override public boolean disableRule(java.lang.String ruleId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_disableRule, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().disableRule(ruleId);
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
      @Override public boolean isRuleEnabled(java.lang.String ruleId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isRuleEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isRuleEnabled(ruleId);
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
      @Override public void checkRule(java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleCheckCallback callback, int format) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleJson);
          _data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
          _data.writeInt(format);
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkRule, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().checkRule(ruleJson, callback, format);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public RuleInfo[] getAllRules() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        RuleInfo[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllRules, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getAllRules();
          }
          _reply.readException();
          _result = _reply.createTypedArray(RuleInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public RuleInfo[] getEnabledRules() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        RuleInfo[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getEnabledRules, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getEnabledRules();
          }
          _reply.readException();
          _result = _reply.createTypedArray(RuleInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      public static github.tornaco.android.thanos.core.profile.IProfileManager sDefaultImpl;
    }
    static final int TRANSACTION_setAutoApplyForNewInstalledAppsEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_isAutoApplyForNewInstalledAppsEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_addRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_deleteRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_enableRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_disableRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_isRuleEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_checkRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_getAllRules = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_getEnabledRules = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
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
  public void addRule(java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleAddCallback callback, int format) throws android.os.RemoteException;
  public void deleteRule(java.lang.String ruleId) throws android.os.RemoteException;
  public boolean enableRule(java.lang.String ruleId) throws android.os.RemoteException;
  public boolean disableRule(java.lang.String ruleId) throws android.os.RemoteException;
  public boolean isRuleEnabled(java.lang.String ruleId) throws android.os.RemoteException;
  public void checkRule(java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleCheckCallback callback, int format) throws android.os.RemoteException;
  public RuleInfo[] getAllRules() throws android.os.RemoteException;
  public RuleInfo[] getEnabledRules() throws android.os.RemoteException;
}
