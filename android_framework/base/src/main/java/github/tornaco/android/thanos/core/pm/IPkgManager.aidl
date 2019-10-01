package github.tornaco.android.thanos.core.pm;


interface IPkgManager {
    String[] getPkgNameForUid(int uid);
    int getUidForPkgName(String pkgName);

    // ApplicationInfo
    AppInfo[] getInstalledPkgs(int flags);
    AppInfo getAppInfo(String pkgName);

    String[] getWhiteListPkgs();
    boolean isPkgInWhiteList(String pkg);

    void setComponentEnabledSetting(in ComponentName componentName, int newState, int flags);
    int getComponentEnabledSetting(in ComponentName componentName);

    int getApplicationEnabledSetting(String packageName);
    void setApplicationEnabledSetting(String packageName, int newState, int flags, boolean tmp);

}