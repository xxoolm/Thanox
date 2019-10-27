package github.tornaco.android.thanos.services

import github.tornaco.android.thanos.services.app.ActivityManagerService
import github.tornaco.android.thanos.services.app.ActivityStackSupervisorService
import github.tornaco.android.thanos.services.audio.AudioService
import github.tornaco.android.thanos.services.backup.BackupAgentService
import github.tornaco.android.thanos.services.n.NotificationManagerService
import github.tornaco.android.thanos.services.os.ServiceManagerService
import github.tornaco.android.thanos.services.perf.PreferenceManagerService
import github.tornaco.android.thanos.services.pm.PkgManagerService
import github.tornaco.android.thanos.services.power.PowerService
import github.tornaco.android.thanos.services.profile.ProfileService
import github.tornaco.android.thanos.services.push.PushManagerService
import github.tornaco.android.thanos.services.secure.PrivacyService
import github.tornaco.android.thanos.services.secure.ops.AppOpsService
import github.tornaco.android.thanos.services.wm.WindowManagerService

interface S {
    val activityManagerService: ActivityManagerService

    val serviceManagerService: ServiceManagerService

    val pkgManagerService: PkgManagerService

    val preferenceManagerService: PreferenceManagerService

    val activityStackSupervisor: ActivityStackSupervisorService

    val privacyService: PrivacyService

    val appOpsService: AppOpsService

    val pushManagerService: PushManagerService

    val notificationManagerService: NotificationManagerService

    val audioService: AudioService

    val backupAgent: BackupAgentService

    val windowManagerService: WindowManagerService

    val profileService: ProfileService

    val powerService: PowerService
}
