package github.tornaco.android.thanos.services

import android.content.IntentFilter
import android.util.Log
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.core.IThanos
import github.tornaco.android.thanos.core.app.IActivityManager
import github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor
import github.tornaco.android.thanos.core.app.event.IEventSubscriber
import github.tornaco.android.thanos.core.audio.IAudioManager
import github.tornaco.android.thanos.core.backup.IBackupAgent
import github.tornaco.android.thanos.core.n.INotificationManager
import github.tornaco.android.thanos.core.os.IServiceManager
import github.tornaco.android.thanos.core.pm.IPkgManager
import github.tornaco.android.thanos.core.pref.IPrefManager
import github.tornaco.android.thanos.core.profile.IProfileManager
import github.tornaco.android.thanos.core.push.IPushManager
import github.tornaco.android.thanos.core.secure.IPrivacyManager
import github.tornaco.android.thanos.core.secure.ops.IAppOpsService
import github.tornaco.android.thanos.core.util.Noop
import github.tornaco.android.thanos.services.app.*
import github.tornaco.android.thanos.services.audio.AudioService
import github.tornaco.android.thanos.services.audio.AudioServiceStub
import github.tornaco.android.thanos.services.backup.BackupAgentService
import github.tornaco.android.thanos.services.backup.BackupAgentServiceStub
import github.tornaco.android.thanos.services.n.NotificationManagerService
import github.tornaco.android.thanos.services.n.NotificationManagerServiceStub
import github.tornaco.android.thanos.services.os.ServiceManagerService
import github.tornaco.android.thanos.services.os.ServiceManagerStub
import github.tornaco.android.thanos.services.perf.PrefManagerStub
import github.tornaco.android.thanos.services.perf.PreferenceManagerService
import github.tornaco.android.thanos.services.pm.PkgManagerService
import github.tornaco.android.thanos.services.pm.PkgManagerStub
import github.tornaco.android.thanos.services.push.PushManagerService
import github.tornaco.android.thanos.services.push.PushManagerServiceStub
import github.tornaco.android.thanos.services.secure.PrivacyManagerStub
import github.tornaco.android.thanos.services.secure.PrivacyService
import github.tornaco.android.thanos.services.secure.ops.AppOpsService
import github.tornaco.android.thanos.services.secure.ops.AppOpsServiceStub

internal class ThanosServiceStub(
    private val ams: ActivityManagerService,
    private val sms: ServiceManagerService,
    private val pms: PkgManagerService,
    private val prefms: PreferenceManagerService,
    private val supervisor: ActivityStackSupervisorService,
    private val priv: PrivacyService,
    private val ops: AppOpsService,
    private val push: PushManagerService,
    private val notification: NotificationManagerService,
    private val audio: AudioService,
    private val backup: BackupAgentService
) : IThanos.Stub() {

    override fun getProfileManager(): IProfileManager {
        return Noop.notSupported()
    }

    override fun getAppOpsService(): IAppOpsService {
        return AppOpsServiceStub(ops)
    }

    override fun getServiceManager(): IServiceManager {
        return ServiceManagerStub(sms)
    }

    override fun getPrefManager(): IPrefManager {
        return PrefManagerStub(prefms)
    }

    override fun getActivityManager(): IActivityManager {
        return ActivityManagerStub(ams)
    }

    override fun getPkgManager(): IPkgManager {
        return PkgManagerStub(pms)
    }

    override fun getActivityStackSupervisor(): IActivityStackSupervisor {
        return ActivityStackSupervisorStub(supervisor)
    }

    override fun getPrivacyManager(): IPrivacyManager {
        return PrivacyManagerStub(priv)
    }

    override fun getPushManager(): IPushManager {
        return PushManagerServiceStub(push)
    }

    override fun getNotificationManager(): INotificationManager {
        return NotificationManagerServiceStub(notification)
    }

    override fun getAudioManager(): IAudioManager {
        return AudioServiceStub(audio)
    }

    override fun getBackupAgent(): IBackupAgent {
        return BackupAgentServiceStub(backup)
    }

    override fun registerEventSubscriber(filter: IntentFilter, subscriber: IEventSubscriber) {
        EventBus.getInstance().registerEventSubscriber(filter, subscriber)
    }

    override fun unRegisterEventSubscriber(subscriber: IEventSubscriber) {
        EventBus.getInstance().unRegisterEventSubscriber(subscriber)
    }

    override fun fingerPrint(): String {
        return BuildProp.FINGERPRINT
    }

    override fun getVersionName(): String {
        return BuildProp.VERSION
    }

    override fun whoAreYou(): String {
        return "I am Thanox!!!"
    }

    override fun setLoggingEnabled(enable: Boolean) {
        if (enable) BootStrap.setCurrentLogLevel(Log.VERBOSE)
        // Logging error anyway.
        else BootStrap.setCurrentLogLevel(Log.ERROR)
    }

    override fun isLoggingEnabled(): Boolean {
        return BootStrap.getCurrentLogLevel() < Log.ERROR
    }
}
