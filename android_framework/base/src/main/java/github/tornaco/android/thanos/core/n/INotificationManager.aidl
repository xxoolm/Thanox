package github.tornaco.android.thanos.core.n;

import github.tornaco.android.thanos.core.n.INotificationObserver;

interface INotificationManager {

    NotificationRecord[] getNotificationRecordsForPackage(in String packageName);

    boolean hasNotificationRecordsForPackage(in String packageName);

    void registerObserver(in INotificationObserver obs);

    void unRegisterObserver(in INotificationObserver obs);
}