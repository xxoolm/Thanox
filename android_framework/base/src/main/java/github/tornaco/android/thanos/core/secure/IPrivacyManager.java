/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package github.tornaco.android.thanos.core.secure;
public interface IPrivacyManager extends android.os.IInterface
{
  /** Default implementation for IPrivacyManager. */
  public static class Default implements github.tornaco.android.thanos.core.secure.IPrivacyManager
  {
    @Override public boolean isPrivacyEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setPrivacyEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean isUidPrivacyDataCheat(int uid) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean isPkgPrivacyDataCheat(java.lang.String pkg) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setPkgPrivacyDataCheat(java.lang.String pkg, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public java.lang.String getCheatedDeviceIdForPkg(java.lang.String pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getCheatedLine1NumberForPkg(java.lang.String pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getCheatedSimSerialNumberForPkg(java.lang.String pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getCheatedAndroidIdForPkg(java.lang.String pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.location.Location getCheatedLocationForPkg(java.lang.String pkg, android.location.Location actual) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getOriginalDeviceId() throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getOriginalLine1Number() throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getOriginalSimSerialNumber() throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getOriginalAndroidId() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setCheatedDeviceIdForPkg(java.lang.String pkg, java.lang.String deviceId) throws android.os.RemoteException
    {
    }
    @Override public void setCheatedLine1NumberForPkg(java.lang.String pkg, java.lang.String num) throws android.os.RemoteException
    {
    }
    @Override public void setCheatedSimSerialNumberForPkg(java.lang.String pkg, java.lang.String num) throws android.os.RemoteException
    {
    }
    @Override public void setCheatedAndroidForPkg(java.lang.String pkg, java.lang.String id) throws android.os.RemoteException
    {
    }
    @Override public int getPrivacyDataCheatPkgCount() throws android.os.RemoteException
    {
      return 0;
    }
    @Override public long getPrivacyDataCheatRequestCount() throws android.os.RemoteException
    {
      return 0L;
    }
    @Override public boolean isPrivacyNotificationEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setPrivacyNotificationEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.secure.IPrivacyManager
  {
    private static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.secure.IPrivacyManager";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.secure.IPrivacyManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.secure.IPrivacyManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.secure.IPrivacyManager))) {
        return ((github.tornaco.android.thanos.core.secure.IPrivacyManager)iin);
      }
      return new github.tornaco.android.thanos.core.secure.IPrivacyManager.Stub.Proxy(obj);
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
        case TRANSACTION_isPrivacyEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isPrivacyEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setPrivacyEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setPrivacyEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_isUidPrivacyDataCheat:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          boolean _result = this.isUidPrivacyDataCheat(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_isPkgPrivacyDataCheat:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isPkgPrivacyDataCheat(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setPkgPrivacyDataCheat:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setPkgPrivacyDataCheat(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getCheatedDeviceIdForPkg:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _result = this.getCheatedDeviceIdForPkg(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getCheatedLine1NumberForPkg:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _result = this.getCheatedLine1NumberForPkg(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getCheatedSimSerialNumberForPkg:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _result = this.getCheatedSimSerialNumberForPkg(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getCheatedAndroidIdForPkg:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _result = this.getCheatedAndroidIdForPkg(_arg0);
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getCheatedLocationForPkg:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          android.location.Location _arg1;
          if ((0!=data.readInt())) {
            _arg1 = android.location.Location.CREATOR.createFromParcel(data);
          }
          else {
            _arg1 = null;
          }
          android.location.Location _result = this.getCheatedLocationForPkg(_arg0, _arg1);
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
        case TRANSACTION_getOriginalDeviceId:
        {
          data.enforceInterface(descriptor);
          java.lang.String _result = this.getOriginalDeviceId();
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getOriginalLine1Number:
        {
          data.enforceInterface(descriptor);
          java.lang.String _result = this.getOriginalLine1Number();
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getOriginalSimSerialNumber:
        {
          data.enforceInterface(descriptor);
          java.lang.String _result = this.getOriginalSimSerialNumber();
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_getOriginalAndroidId:
        {
          data.enforceInterface(descriptor);
          java.lang.String _result = this.getOriginalAndroidId();
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_setCheatedDeviceIdForPkg:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.setCheatedDeviceIdForPkg(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setCheatedLine1NumberForPkg:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.setCheatedLine1NumberForPkg(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setCheatedSimSerialNumberForPkg:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.setCheatedSimSerialNumberForPkg(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_setCheatedAndroidForPkg:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.setCheatedAndroidForPkg(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getPrivacyDataCheatPkgCount:
        {
          data.enforceInterface(descriptor);
          int _result = this.getPrivacyDataCheatPkgCount();
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_getPrivacyDataCheatRequestCount:
        {
          data.enforceInterface(descriptor);
          long _result = this.getPrivacyDataCheatRequestCount();
          reply.writeNoException();
          reply.writeLong(_result);
          return true;
        }
        case TRANSACTION_isPrivacyNotificationEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _result = this.isPrivacyNotificationEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        case TRANSACTION_setPrivacyNotificationEnabled:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setPrivacyNotificationEnabled(_arg0);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements github.tornaco.android.thanos.core.secure.IPrivacyManager
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
      @Override public boolean isPrivacyEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPrivacyEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPrivacyEnabled();
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
      @Override public void setPrivacyEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPrivacyEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setPrivacyEnabled(enabled);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isUidPrivacyDataCheat(int uid) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(uid);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isUidPrivacyDataCheat, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isUidPrivacyDataCheat(uid);
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
      @Override public boolean isPkgPrivacyDataCheat(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPkgPrivacyDataCheat, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPkgPrivacyDataCheat(pkg);
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
      @Override public void setPkgPrivacyDataCheat(java.lang.String pkg, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPkgPrivacyDataCheat, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setPkgPrivacyDataCheat(pkg, enable);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.lang.String getCheatedDeviceIdForPkg(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getCheatedDeviceIdForPkg, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getCheatedDeviceIdForPkg(pkg);
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
      @Override public java.lang.String getCheatedLine1NumberForPkg(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getCheatedLine1NumberForPkg, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getCheatedLine1NumberForPkg(pkg);
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
      @Override public java.lang.String getCheatedSimSerialNumberForPkg(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getCheatedSimSerialNumberForPkg, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getCheatedSimSerialNumberForPkg(pkg);
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
      @Override public java.lang.String getCheatedAndroidIdForPkg(java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getCheatedAndroidIdForPkg, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getCheatedAndroidIdForPkg(pkg);
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
      @Override public android.location.Location getCheatedLocationForPkg(java.lang.String pkg, android.location.Location actual) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.location.Location _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          if ((actual!=null)) {
            _data.writeInt(1);
            actual.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_getCheatedLocationForPkg, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getCheatedLocationForPkg(pkg, actual);
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = android.location.Location.CREATOR.createFromParcel(_reply);
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
      @Override public java.lang.String getOriginalDeviceId() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getOriginalDeviceId, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getOriginalDeviceId();
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
      @Override public java.lang.String getOriginalLine1Number() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getOriginalLine1Number, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getOriginalLine1Number();
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
      @Override public java.lang.String getOriginalSimSerialNumber() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getOriginalSimSerialNumber, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getOriginalSimSerialNumber();
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
      @Override public java.lang.String getOriginalAndroidId() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getOriginalAndroidId, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getOriginalAndroidId();
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
      @Override public void setCheatedDeviceIdForPkg(java.lang.String pkg, java.lang.String deviceId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          _data.writeString(deviceId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setCheatedDeviceIdForPkg, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setCheatedDeviceIdForPkg(pkg, deviceId);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setCheatedLine1NumberForPkg(java.lang.String pkg, java.lang.String num) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          _data.writeString(num);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setCheatedLine1NumberForPkg, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setCheatedLine1NumberForPkg(pkg, num);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setCheatedSimSerialNumberForPkg(java.lang.String pkg, java.lang.String num) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          _data.writeString(num);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setCheatedSimSerialNumberForPkg, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setCheatedSimSerialNumberForPkg(pkg, num);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setCheatedAndroidForPkg(java.lang.String pkg, java.lang.String id) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(pkg);
          _data.writeString(id);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setCheatedAndroidForPkg, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setCheatedAndroidForPkg(pkg, id);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public int getPrivacyDataCheatPkgCount() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPrivacyDataCheatPkgCount, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getPrivacyDataCheatPkgCount();
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
      @Override public long getPrivacyDataCheatRequestCount() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        long _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPrivacyDataCheatRequestCount, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getPrivacyDataCheatRequestCount();
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
      @Override public boolean isPrivacyNotificationEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPrivacyNotificationEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().isPrivacyNotificationEnabled();
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
      @Override public void setPrivacyNotificationEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPrivacyNotificationEnabled, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setPrivacyNotificationEnabled(enabled);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static github.tornaco.android.thanos.core.secure.IPrivacyManager sDefaultImpl;
    }
    static final int TRANSACTION_isPrivacyEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_setPrivacyEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_isUidPrivacyDataCheat = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_isPkgPrivacyDataCheat = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_setPkgPrivacyDataCheat = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_getCheatedDeviceIdForPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_getCheatedLine1NumberForPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_getCheatedSimSerialNumberForPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_getCheatedAndroidIdForPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_getCheatedLocationForPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_getOriginalDeviceId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_getOriginalLine1Number = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_getOriginalSimSerialNumber = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_getOriginalAndroidId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_setCheatedDeviceIdForPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_setCheatedLine1NumberForPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_setCheatedSimSerialNumberForPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_setCheatedAndroidForPkg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    static final int TRANSACTION_getPrivacyDataCheatPkgCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    static final int TRANSACTION_getPrivacyDataCheatRequestCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
    static final int TRANSACTION_isPrivacyNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
    static final int TRANSACTION_setPrivacyNotificationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
    public static boolean setDefaultImpl(github.tornaco.android.thanos.core.secure.IPrivacyManager impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static github.tornaco.android.thanos.core.secure.IPrivacyManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public boolean isPrivacyEnabled() throws android.os.RemoteException;
  public void setPrivacyEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean isUidPrivacyDataCheat(int uid) throws android.os.RemoteException;
  public boolean isPkgPrivacyDataCheat(java.lang.String pkg) throws android.os.RemoteException;
  public void setPkgPrivacyDataCheat(java.lang.String pkg, boolean enable) throws android.os.RemoteException;
  public java.lang.String getCheatedDeviceIdForPkg(java.lang.String pkg) throws android.os.RemoteException;
  public java.lang.String getCheatedLine1NumberForPkg(java.lang.String pkg) throws android.os.RemoteException;
  public java.lang.String getCheatedSimSerialNumberForPkg(java.lang.String pkg) throws android.os.RemoteException;
  public java.lang.String getCheatedAndroidIdForPkg(java.lang.String pkg) throws android.os.RemoteException;
  public android.location.Location getCheatedLocationForPkg(java.lang.String pkg, android.location.Location actual) throws android.os.RemoteException;
  public java.lang.String getOriginalDeviceId() throws android.os.RemoteException;
  public java.lang.String getOriginalLine1Number() throws android.os.RemoteException;
  public java.lang.String getOriginalSimSerialNumber() throws android.os.RemoteException;
  public java.lang.String getOriginalAndroidId() throws android.os.RemoteException;
  public void setCheatedDeviceIdForPkg(java.lang.String pkg, java.lang.String deviceId) throws android.os.RemoteException;
  public void setCheatedLine1NumberForPkg(java.lang.String pkg, java.lang.String num) throws android.os.RemoteException;
  public void setCheatedSimSerialNumberForPkg(java.lang.String pkg, java.lang.String num) throws android.os.RemoteException;
  public void setCheatedAndroidForPkg(java.lang.String pkg, java.lang.String id) throws android.os.RemoteException;
  public int getPrivacyDataCheatPkgCount() throws android.os.RemoteException;
  public long getPrivacyDataCheatRequestCount() throws android.os.RemoteException;
  public boolean isPrivacyNotificationEnabled() throws android.os.RemoteException;
  public void setPrivacyNotificationEnabled(boolean enabled) throws android.os.RemoteException;
}
