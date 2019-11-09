package github.tornaco.android.thanos.core.secure;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.location.Location;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class PrivacyManager {
    private IPrivacyManager server;

    @SneakyThrows
    public boolean isPrivacyEnabled() {
        return server.isPrivacyEnabled();
    }

    @SneakyThrows
    public void setPrivacyEnabled(boolean enabled) {
        server.setPrivacyEnabled(enabled);
    }

    @SneakyThrows
    public boolean isPkgPrivacyDataCheat(String pkg) {
        return server.isPkgPrivacyDataCheat(pkg);
    }

    @SneakyThrows
    public void setPkgPrivacyDataCheat(String pkg, boolean enable) {
        server.setPkgPrivacyDataCheat(pkg, enable);
    }

    @SneakyThrows
    public boolean isUidPrivacyDataCheat(int uid) {
        return server.isUidPrivacyDataCheat(uid);
    }

    @SneakyThrows
    public PackageInfo[] getCheatedInstalledPackagesForUid(int uid) {
        return server.getCheatedInstalledPackagesForUid(uid);
    }

    @SneakyThrows
    public ApplicationInfo[] getCheatedInstalledApplicationsUid(int uid) {
        return server.getCheatedInstalledApplicationsUid(uid);
    }

    @SneakyThrows
    public String getCheatedDeviceIdForPkg(String pkg) {
        return server.getCheatedDeviceIdForPkg(pkg);
    }

    @SneakyThrows
    public String getCheatedLine1NumberForPkg(String pkg) {
        return server.getCheatedLine1NumberForPkg(pkg);
    }

    @SneakyThrows
    public String getCheatedSimSerialNumberForPkg(String pkg) {
        return server.getCheatedSimSerialNumberForPkg(pkg);
    }

    @SneakyThrows
    public void setCheatedDeviceIdForPkg(String pkg, String deviceId) {
        server.setCheatedDeviceIdForPkg(pkg, deviceId);
    }

    @SneakyThrows
    public void setCheatedLine1NumberForPkg(String pkg, String num) {
        server.setCheatedLine1NumberForPkg(pkg, num);
    }

    @SneakyThrows
    public void setCheatedSimSerialNumberForPkg(String pkg, String num) {
        server.setCheatedSimSerialNumberForPkg(pkg, num);
    }

    @SneakyThrows
    public int getPrivacyDataCheatPkgCount() {
        return server.getPrivacyDataCheatPkgCount();
    }

    @SneakyThrows
    public long getPrivacyDataCheatRequestCount() {
        return server.getPrivacyDataCheatRequestCount();
    }

    @SneakyThrows
    public Location getCheatedLocationForPkg(String pkg, Location actual) {
        return server.getCheatedLocationForPkg(pkg, actual);
    }

    @SneakyThrows
    public String getOriginalDeviceId() {
        return server.getOriginalDeviceId();
    }

    @SneakyThrows
    public String getOriginalLine1Number() {
        return server.getOriginalLine1Number();
    }

    @SneakyThrows
    public String getOriginalSimSerialNumber() {
        return server.getOriginalSimSerialNumber();
    }

    @SneakyThrows
    public void setInstalledPackagesReturnEmptyEnableForPkg(String pkg, boolean enable) {
        server.setInstalledPackagesReturnEmptyEnableForPkg(pkg, enable);
    }

    @SneakyThrows
    public boolean isInstalledPackagesReturnEmptyEnableForPkg(String pkg) {
        return server.isInstalledPackagesReturnEmptyEnableForPkg(pkg);
    }

    @SneakyThrows
    public boolean isPrivacyNotificationEnabled() {
        return server.isPrivacyNotificationEnabled();
    }

    @SneakyThrows
    public void setPrivacyNotificationEnabled(boolean enabled) {
        server.setPrivacyNotificationEnabled(enabled);
    }
}
