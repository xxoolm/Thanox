package github.tornaco.android.thanos.services.util;

import android.service.notification.StatusBarNotification;
import de.robv.android.xposed.XposedHelpers;
import github.tornaco.android.thanos.core.n.NotificationRecord;

public class NotificationRecordUtils {
    private NotificationRecordUtils() {
    }

    public static NotificationRecord fromLegacy(Object legacy) {
        StatusBarNotification sbn = (StatusBarNotification) XposedHelpers
                .getObjectField(legacy, "sbn");
        return new NotificationRecord(sbn.getPackageName(), sbn.getNotification());
    }
}
