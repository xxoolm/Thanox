package github.tornaco.android.thanos.core.n;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class NotificationManager {
    private final INotificationManager service;

    @SneakyThrows
    public NotificationRecord[] getNotificationRecordsForPackage(String packageName) {
        return service.getNotificationRecordsForPackage(packageName);
    }

    @SneakyThrows
    public boolean hasNotificationRecordsForPackage(String packageName) {
        return service.hasNotificationRecordsForPackage(packageName);
    }

    @SneakyThrows
    public void registerObserver(INotificationObserver obs) {
        service.registerObserver(obs);
    }

    @SneakyThrows
    public void unRegisterObserver(INotificationObserver obs) {
        service.unRegisterObserver(obs);
    }

    @SneakyThrows
    public void setScreenOnNotificationEnabledForPkg(String pkg, boolean enable) {
        service.setScreenOnNotificationEnabledForPkg(pkg, enable);
    }

    @SneakyThrows
    public boolean isScreenOnNotificationEnabledForPkg(String pkg) {
        return service.isScreenOnNotificationEnabledForPkg(pkg);
    }

    @SneakyThrows
    public void setScreenOnNotificationEnabled(boolean enable) {
        service.setScreenOnNotificationEnabled(enable);
    }

    @SneakyThrows
    public boolean isScreenOnNotificationEnabled() {
        return service.isScreenOnNotificationEnabled();
    }
}
