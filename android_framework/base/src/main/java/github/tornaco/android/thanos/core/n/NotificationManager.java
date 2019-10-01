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
}
