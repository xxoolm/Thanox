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
    @Override public void deleteRule(java.lang.String ruleName) throws android.os.RemoteException
    {
    }
    @Override public boolean enableRule(java.lang.String ruleName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean disableRule(java.lang.String ruleName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean isRuleEnabled(java.lang.String ruleName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean isRuleExists(java.lang.String ruleName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void checkRule(java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleCheckCallback callback, int format) throws android.os.RemoteException
    {
    }
    @Override public github.tornaco.android.thanos.core.profile.RuleInfo[] getAllRules() throws android.os.RemoteException
    {
      return null;
    }
    @Override public github.tornaco.android.thanos.core.profile.RuleInfo[] getEnabledRules() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setProfileEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isProfileEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean addGlobalRuleVar(java.lang.String varName, java.lang.String[] varArray) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean appendGlobalRuleVar(java.lang.String varName, java.lang.String[] varArray) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean removeGlobalRuleVar(java.lang.String varName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.lang.String[] getAllGlobalRuleVarNames() throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String[] getGlobalRuleVarByName(java.lang.String varName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public github.tornaco.android.thanos.core.profile.GlobalVar[] getAllGlobalRuleVar() throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean isGlobalRuleVarByNameExists(java.lang.String varName) throws android.os.RemoteException
    {
      return false;
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
        case TRANSACTION_isRuleExists:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isRuleExists(_arg0);
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
          github.tornaco.android.thanos.core.profile.RuleInfo[] _result = this.getAllRules();
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          return true;
        }
        case TRANSACTION_getEnabledRules:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.profile.RuleInfo[] _result = this.getEnabledRules();
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          return true;
        }
        case TRANSACTION_setProfileEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setProfileEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isProfileEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isProfileEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_addGlobalRuleVar:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String[] _arg1;
          _arg1 = data.createStringArray();
          boolean _result = this.addGlobalRuleVar(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_appendGlobalRuleVar:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String[] _arg1;
          _arg1 = data.createStringArray();
          boolean _result = this.appendGlobalRuleVar(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_removeGlobalRuleVar:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.removeGlobalRuleVar(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_getAllGlobalRuleVarNames:
        {
          data.enforceInterface(descriptor);
          java.lang.String[] _result = this.getAllGlobalRuleVarNames();
          reply.writeNoException();
          reply.writeStringArray(_result);
          return true;
        }
        case TRANSACTION_getGlobalRuleVarByName:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String[] _result = this.getGlobalRuleVarByName(_arg0);
          reply.writeNoException();
          reply.writeStringArray(_result);
          return true;
        }
        case TRANSACTION_getAllGlobalRuleVar:
        {
          data.enforceInterface(descriptor);
          github.tornaco.android.thanos.core.profile.GlobalVar[] _result = this.getAllGlobalRuleVar();
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          return true;
        }
        case TRANSACTION_isGlobalRuleVarByNameExists:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isGlobalRuleVarByNameExists(_arg0);
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
      @Override public void deleteRule(java.lang.String ruleName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_deleteRule, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().deleteRule(ruleName);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean enableRule(java.lang.String ruleName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_enableRule, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().enableRule(ruleName);
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
      @Override public boolean disableRule(java.lang.String ruleName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_disableRule, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().disableRule(ruleName);
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
      @Override public boolean isRuleEnabled(java.lang.String ruleName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isRuleEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isRuleEnabled(ruleName);
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
      @Override public boolean isRuleExists(java.lang.String ruleName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isRuleExists, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isRuleExists(ruleName);
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
      @Override public github.tornaco.android.thanos.core.profile.RuleInfo[] getAllRules() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.profile.RuleInfo[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllRules, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getAllRules();
          }
          _reply.readException();
          _result = _reply.createTypedArray(github.tornaco.android.thanos.core.profile.RuleInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public github.tornaco.android.thanos.core.profile.RuleInfo[] getEnabledRules() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.profile.RuleInfo[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getEnabledRules, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getEnabledRules();
          }
          _reply.readException();
          _result = _reply.createTypedArray(github.tornaco.android.thanos.core.profile.RuleInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setProfileEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setProfileEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setProfileEnabled(enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isProfileEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isProfileEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isProfileEnabled();
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
      @Override public boolean addGlobalRuleVar(java.lang.String varName, java.lang.String[] varArray) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(varName);
          _data.writeStringArray(varArray);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addGlobalRuleVar, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().addGlobalRuleVar(varName, varArray);
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
      @Override public boolean appendGlobalRuleVar(java.lang.String varName, java.lang.String[] varArray) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(varName);
          _data.writeStringArray(varArray);
          boolean _status = mRemote.transact(Stub.TRANSACTION_appendGlobalRuleVar, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().appendGlobalRuleVar(varName, varArray);
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
      @Override public boolean removeGlobalRuleVar(java.lang.String varName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(varName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removeGlobalRuleVar, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().removeGlobalRuleVar(varName);
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
      @Override public java.lang.String[] getAllGlobalRuleVarNames() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllGlobalRuleVarNames, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getAllGlobalRuleVarNames();
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
      @Override public java.lang.String[] getGlobalRuleVarByName(java.lang.String varName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(varName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getGlobalRuleVarByName, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getGlobalRuleVarByName(varName);
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
      @Override public github.tornaco.android.thanos.core.profile.GlobalVar[] getAllGlobalRuleVar() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.profile.GlobalVar[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllGlobalRuleVar, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getAllGlobalRuleVar();
          }
          _reply.readException();
          _result = _reply.createTypedArray(github.tornaco.android.thanos.core.profile.GlobalVar.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isGlobalRuleVarByNameExists(java.lang.String varName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(varName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isGlobalRuleVarByNameExists, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isGlobalRuleVarByNameExists(varName);
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
      public static github.tornaco.android.thanos.core.profile.IProfileManager sDefaultImpl;
    }
    static final int TRANSACTION_setAutoApplyForNewInstalledAppsEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_isAutoApplyForNewInstalledAppsEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_addRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_deleteRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_enableRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_disableRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_isRuleEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_isRuleExists = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_checkRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_getAllRules = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_getEnabledRules = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_setProfileEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_isProfileEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_addGlobalRuleVar = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_appendGlobalRuleVar = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_removeGlobalRuleVar = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_getAllGlobalRuleVarNames = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_getGlobalRuleVarByName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    static final int TRANSACTION_getAllGlobalRuleVar = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    static final int TRANSACTION_isGlobalRuleVarByNameExists = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
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
  public void deleteRule(java.lang.String ruleName) throws android.os.RemoteException;
  public boolean enableRule(java.lang.String ruleName) throws android.os.RemoteException;
  public boolean disableRule(java.lang.String ruleName) throws android.os.RemoteException;
  public boolean isRuleEnabled(java.lang.String ruleName) throws android.os.RemoteException;
  public boolean isRuleExists(java.lang.String ruleName) throws android.os.RemoteException;
  public void checkRule(java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleCheckCallback callback, int format) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.profile.RuleInfo[] getAllRules() throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.profile.RuleInfo[] getEnabledRules() throws android.os.RemoteException;
  public void setProfileEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isProfileEnabled() throws android.os.RemoteException;
  public boolean addGlobalRuleVar(java.lang.String varName, java.lang.String[] varArray) throws android.os.RemoteException;
  public boolean appendGlobalRuleVar(java.lang.String varName, java.lang.String[] varArray) throws android.os.RemoteException;
  public boolean removeGlobalRuleVar(java.lang.String varName) throws android.os.RemoteException;
  public java.lang.String[] getAllGlobalRuleVarNames() throws android.os.RemoteException;
  public java.lang.String[] getGlobalRuleVarByName(java.lang.String varName) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.profile.GlobalVar[] getAllGlobalRuleVar() throws android.os.RemoteException;
  public boolean isGlobalRuleVarByNameExists(java.lang.String varName) throws android.os.RemoteException;
}
