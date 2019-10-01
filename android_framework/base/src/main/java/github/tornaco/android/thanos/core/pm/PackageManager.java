package github.tornaco.android.thanos.core.pm;

import android.content.ComponentName;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class PackageManager {
    private IPkgManager pm;

    public static String packageNameOfAndroid() {
        return "android";
    }

    public static String packageNameOfTelecom() {
        return "com.android.server.telecom";
    }

    public static String packageNameOfPhone() {
        return "com.android.phone";
    }

    public static String sharedUserIdOfMedia() {
        return "android.media";
    }

    public static String sharedUserIdOfPhone() {
        return "android.uid.phone";
    }

    public static String sharedUserIdOfSystem() {
        return "android.uid.system";
    }

    @SneakyThrows
    public String[] getPkgNameForUid(int uid) {
        return pm.getPkgNameForUid(uid);
    }

    @SneakyThrows
    public int getUidForPkgName(String pkgName) {
        return pm.getUidForPkgName(pkgName);
    }

    @SneakyThrows
    public AppInfo[] getInstalledPkgs(int flags) {
        return pm.getInstalledPkgs(flags);
    }

    @SneakyThrows
    public AppInfo getAppInfo(String pkgName) {
        return pm.getAppInfo(pkgName);
    }

    @SneakyThrows
    public String[] getWhiteListPkgs() {
        return pm.getWhiteListPkgs();
    }

    @SneakyThrows
    public boolean isPkgInWhiteList(String pkg) {
        return pm.isPkgInWhiteList(pkg);
    }

    @SneakyThrows
    public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags) {
        pm.setComponentEnabledSetting(componentName, newState, flags);
    }

    @SneakyThrows
    public int getComponentEnabledSetting(ComponentName componentName) {
        return pm.getComponentEnabledSetting(componentName);
    }

    @SneakyThrows
    public int getApplicationEnabledSetting(String packageName) {
        return pm.getApplicationEnabledSetting(packageName);
    }

    @SneakyThrows
    public void setApplicationEnabledSetting(String packageName, int newState, int flags, boolean tmp) {
        pm.setApplicationEnabledSetting(packageName, newState, flags, tmp);
    }
}
