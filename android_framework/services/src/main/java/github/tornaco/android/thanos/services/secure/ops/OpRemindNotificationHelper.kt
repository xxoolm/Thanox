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

    private val LOCATION_OPS = intArrayOf(
        AppOpsManager.OP_COARSE_LOCATION,
        AppOpsManager.OP_FINE_LOCATION,
        AppOpsManager.OP_GPS,
        AppOpsManager.OP_WIFI_SCAN,
        AppOpsManager.OP_NEIGHBORING_CELLS,
        AppOpsManager.OP_MONITOR_LOCATION,
        AppOpsManager.OP_MONITOR_HIGH_POWER_LOCATION
    )

    private val OP_ALIAS_LOCATION = 0x12345

    fun remindOpStart(pkg: String, op: Int) {
        Timber.d("remindOpStart: %s %s", pkg, op)
        remindAsNotification(pkg, mergeOp(op))
    }

    fun remindOpFinish(pkg: String, op: Int) {
        Timber.d("remindOpFinish: %s %s", pkg, op)
        NotificationManagerCompat.from(context).cancel(constructNotificationId(pkg, mergeOp(op)))
    }

    private fun mergeOp(opOriginal: Int): Int {
        if (LOCATION_OPS.contains(opOriginal)) {
            return OP_ALIAS_LOCATION
        }
        return opOriginal
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

            // Merged alias.
            OP_ALIAS_LOCATION -> Res.Drawables.DRAWABLE_MAP_PIN_FILL
            else -> "Missing..."
        }
    }

    private fun getLabelForOp(op: Int): String {
        return when (op) {
            AppOpsManager.OP_RECORD_AUDIO -> Res.Strings.STRING_SERVICE_OP_LABEL_RECORD_AUDIO
            AppOpsManager.OP_CAMERA -> Res.Strings.STRING_SERVICE_OP_LABEL_CAMERA
            AppOpsManager.OP_PLAY_AUDIO -> Res.Strings.STRING_SERVICE_OP_LABEL_PLAY_AUDIO

            // Merged alias.
            OP_ALIAS_LOCATION -> Res.Strings.STRING_SERVICE_OP_LABEL_LOCATION
            else -> "Missing..."
        }
    }
}
