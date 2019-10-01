package github.tornaco.android.thanos.services.secure.ops

import android.content.Context
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.core.Res
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.app.AppResources
import github.tornaco.android.thanos.core.compat.NotificationCompat
import github.tornaco.android.thanos.core.compat.NotificationCompat.VISIBILITY_PUBLIC
import github.tornaco.android.thanos.core.compat.NotificationManagerCompat
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager
import github.tornaco.android.thanos.core.util.OsUtils
import github.tornaco.android.thanos.core.util.Timber
import github.tornaco.android.thanos.services.S
import github.tornaco.android.thanos.services.n.NotificationHelper
import github.tornaco.android.thanos.services.n.NotificationIdFactory
import github.tornaco.android.thanos.services.n.SystemUI

class OpRemindNotificationHelper(private val context: Context, private val s: S) {

    fun remindOpStart(pkg: String, op: Int) {
        Timber.d("remindOpStart: %s %s", pkg, op)
        remindAsNotification(pkg, op)
    }

    fun remindOpFinish(pkg: String, op: Int) {
        Timber.d("remindOpFinish: %s %s", pkg, op)
        NotificationManagerCompat.from(context).cancel(constructNotificationId(pkg, op))
    }

    private fun constructNotificationId(pkg: String, op: Int): Int {
        return NotificationIdFactory.getIdByTag(String.format("%s-%s", pkg, op.toString()))
    }

    private fun remindAsNotification(pkg: String, op: Int) {
        val label = s.pkgManagerService.getAppInfo(pkg).appLabel
        // For oreo.
        NotificationHelper().createSilenceNotificationChannel(context)

        val appResource = AppResources(context, BuildProp.THANOS_APP_PKG_NAME)

        val builder = NotificationCompat.Builder(
            context,
            T.serviceSilenceNotificationChannel()
        )

        SystemUI.overrideNotificationAppName(
            context, builder,
            appResource.getString(Res.Strings.STRING_SERVICE_NOTIFICATION_OVERRIDE_THANOS)
        )

        val n = builder
            .setContentTitle(
                appResource.getString(
                    Res.Strings.STRING_SERVICE_NOTIFICATION_TITLE_OP_START_REMIND,
                    label,
                    appResource.getString(getLabelForOp(op))
                )
            )
            .setVisibility(VISIBILITY_PUBLIC)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .build()

        if (OsUtils.isMOrAbove()) {
            n.smallIcon = appResource.getIcon(getIconForOp(op))
        }

        NotificationManagerCompat.from(context)
            .notify(constructNotificationId(pkg, op), n)
    }

    private fun getIconForOp(op: Int): String {
        return when (op) {
            AppOpsManager.OP_RECORD_AUDIO -> Res.Drawables.DRAWABLE_MIC_FILL
            AppOpsManager.OP_CAMERA -> Res.Drawables.DRAWABLE_CAMERA_FILL
            AppOpsManager.OP_PLAY_AUDIO -> Res.Drawables.DRAWABLE_MUSIC_FILL
            else -> "Missing..."
        }
    }

    private fun getLabelForOp(op: Int): String {
        return when (op) {
            AppOpsManager.OP_RECORD_AUDIO -> Res.Strings.STRING_SERVICE_OP_LABEL_RECORD_AUDIO
            AppOpsManager.OP_CAMERA -> Res.Strings.STRING_SERVICE_OP_LABEL_CAMERA
            AppOpsManager.OP_PLAY_AUDIO -> Res.Strings.STRING_SERVICE_OP_LABEL_PLAY_AUDIO
            else -> "Missing..."
        }
    }
}
