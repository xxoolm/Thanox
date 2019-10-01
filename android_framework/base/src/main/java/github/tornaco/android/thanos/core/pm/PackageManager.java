package github.tornaco.android.thanos.core.pm;

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
}
