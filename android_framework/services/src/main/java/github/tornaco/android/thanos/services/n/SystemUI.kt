package github.tornaco.android.thanos.services.n

import android.app.Notification
import android.content.Context
import android.os.Bundle
import github.tornaco.android.thanos.core.compat.NotificationCompat

object SystemUI {

    @JvmStatic
    fun overrideNotificationAppName(context: Context, n: Notification.Builder, name: String?) {
        val extras = Bundle()
        extras.putString(
            Notification.EXTRA_SUBSTITUTE_APP_NAME,
            name ?: context.getString(com.android.internal.R.string.android_system_label)
        )
        n.addExtras(extras)
    }

    @JvmStatic
    fun overrideNotificationAppName(context: Context, n: NotificationCompat.Builder, name: String?) {
        val extras = Bundle()
        extras.putString(
            Notification.EXTRA_SUBSTITUTE_APP_NAME,
            name ?: context.getString(com.android.internal.R.string.android_system_label)
        )
        n.addExtras(extras)
    }
}