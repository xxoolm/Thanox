package github.tornaco.android.thanos.core.secure;


interface IPrivacyManager {
    boolean isPrivacyEnabled();
    void setPrivacyEnabled(boolean enabled);

    boolean isUidPrivacyDataCheat(int uid);
    boolean isPkgPrivacyDataCheat(String pkg);
    void setPkgPrivacyDataCheat(String pkg, boolean enable);

    PackageInfo[] getCheatedInstalledPackagesForUid(int uid);
    ApplicationInfo[] getCheatedInstalledApplicationsUid(int uid);

    String getCheatedDeviceIdForPkg(String pkg);
    String getCheatedLine1NumberForPkg(String pkg);
    String getCheatedSimSerialNumberForPkg(String pkg);
    Location getCheatedLocationForPkg(String pkg, in Location actual);

    String getOriginalDeviceId();
    String getOriginalLine1Number();
    String getOriginalSimSerialNumber();

    void setCheatedDeviceIdForPkg(String pkg, String deviceId);
    void setCheatedLine1NumberForPkg(String pkg, String num);
    void setCheatedSimSerialNumberForPkg(String pkg, String num);

    // Return am empty array while rquest instal apps.
    void setInstalledPackagesReturnEmptyEnableForPkg(String pkg, boolean enable);
    boolean isInstalledPackagesReturnEmptyEnableForPkg(String pkg);

    int getPrivacyDataCheatPkgCount();
    long getPrivacyDataCheatRequestCount();
}