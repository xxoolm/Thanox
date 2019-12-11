package github.tornaco.android.thanos.core.secure;


interface IPrivacyManager {
    boolean isPrivacyEnabled();
    void setPrivacyEnabled(boolean enabled);

    boolean isUidPrivacyDataCheat(int uid);
    boolean isPkgPrivacyDataCheat(String pkg);
    void setPkgPrivacyDataCheat(String pkg, boolean enable);

    String getCheatedDeviceIdForPkg(String pkg);
    String getCheatedLine1NumberForPkg(String pkg);
    String getCheatedSimSerialNumberForPkg(String pkg);
    String getCheatedAndroidIdForPkg(String pkg);
    Location getCheatedLocationForPkg(String pkg, in Location actual);

    String getOriginalDeviceId();
    String getOriginalLine1Number();
    String getOriginalSimSerialNumber();
    String getOriginalAndroidId();

    void setCheatedDeviceIdForPkg(String pkg, String deviceId);
    void setCheatedLine1NumberForPkg(String pkg, String num);
    void setCheatedSimSerialNumberForPkg(String pkg, String num);
    void setCheatedAndroidForPkg(String pkg, String id);

    int getPrivacyDataCheatPkgCount();
    long getPrivacyDataCheatRequestCount();

    boolean isPrivacyNotificationEnabled();
    void setPrivacyNotificationEnabled(boolean enabled);
}