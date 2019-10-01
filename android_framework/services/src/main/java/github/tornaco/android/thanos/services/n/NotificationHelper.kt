package github.tornaco.android.thanos.services.n

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.core.Res
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.app.AppResources
import github.tornaco.android.thanos.core.util.OsUtils
import java.util.*

class NotificationHelper {

    fun createSilenceNotificationChannel(context: Context) {
        if (OsUtils.isOOrAbove()) {
            val appResources = AppResources(context, BuildProp.THANOS_APP_PKG_NAME)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val nc = notificationManager.getNotificationChannel(T.serviceSilenceNotificationChannel())
            if (nc != null) {
                return
            }
            val notificationChannel: NotificationChannel
            notificationChannel = NotificationChannel(
                T.serviceSilenceNotificationChannel(),
                appResources.getString(Res.Strings.STRING_SERVICE_SILENCE_NOTIFICATION_CHANNEL),
                NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.enableLights(false)
            notificationChannel.enableVibration(false)
            Objects.requireNonNull(notificationManager).createNotificationChannel(notificationChannel)
        }
    }
}