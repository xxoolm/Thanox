package github.tornaco.android.thanos.services

import android.content.Context
import android.os.IBinder
import android.os.ServiceManager
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.util.Timber
import github.tornaco.android.thanos.services.app.ActivityManagerService
import github.tornaco.android.thanos.services.app.ActivityStackSupervisorService
import github.tornaco.android.thanos.services.audio.AudioService
import github.tornaco.android.thanos.services.backup.BackupAgentService
import github.tornaco.android.thanos.services.n.NotificationManagerService
import github.tornaco.android.thanos.services.os.ServiceManagerService
import github.tornaco.android.thanos.services.perf.PreferenceManagerService
import github.tornaco.android.thanos.services.pm.PkgManagerService
import github.tornaco.android.thanos.services.push.PushManagerService
import github.tornaco.android.thanos.services.secure.PrivacyService
import github.tornaco.android.thanos.services.secure.ops.AppOpsService
import java.util.*

class ThanosService : SystemService(), S {

    override val activityManagerService = ActivityManagerService(this)

    override val serviceManagerService = ServiceManagerService(this)

    override val pkgManagerService = PkgManagerService(this)

    override val preferenceManagerService = PreferenceManagerService()

    override val activityStackSupervisor = ActivityStackSupervisorService(this)

    override val privacyService = PrivacyService(this)

    override val appOpsService = AppOpsService(this)

    override val pushManagerService = PushManagerService(this)

    override val notificationManagerService = NotificationManagerService(this)

    override val audioService = AudioService(this)

    override val backupAgent = BackupAgentService(this)

    private val services: Vector<SystemService> = Vector()

    init {
        Timber.d("ThanosService init.")
        services.add(activityManagerService)
        services.add(serviceManagerService)
        services.add(pkgManagerService)
        services.add(preferenceManagerService)
        services.add(activityStackSupervisor)
        services.add(privacyService)
        services.add(appOpsService)
        services.add(pushManagerService)
        services.add(notificationManagerService)
        services.add(audioService)
        services.add(backupAgent)
    }

    override fun onStart(context: Context) {
        super.onStart(context)

        // Invoke all system services.
        SystemServiceLifecycle.onStart(context, services)

        publishBinderService(
            T.serviceInstallName(),
            ThanosServiceStub(
                activityManagerService,
                serviceManagerService,
                pkgManagerService,
                preferenceManagerService,
                activityStackSupervisor,
                privacyService,
                appOpsService,
                pushManagerService,
                notificationManagerService,
                audioService,
                backupAgent
            ).asBinder()
        )

        Timber.i("Bring up with build fingerprint %s", BuildProp.FINGERPRINT)
    }

    private fun publishBinderService(name: String, service: IBinder) {
        this.publishBinderService(name, service, false)
    }

    private fun publishBinderService(name: String, service: IBinder, allowIsolated: Boolean) {
        ServiceManager.addService(name, service, allowIsolated)
    }

    override fun systemReady() {
        super.systemReady()
        SystemServiceLifecycle.systemReady(services)

        initLogging()
    }

    override fun shutdown() {
        super.shutdown()
        SystemServiceLifecycle.shutdown(services)
    }

    override fun serviceName(): String {
        return "ThanosService"
    }

    private fun initLogging() {
        val preferenceManagerService = preferenceManagerService
        val loggingEnabled = preferenceManagerService.getBoolean(
            T.Settings.PREF_LOGGING_ENABLED.key,
            T.Settings.PREF_LOGGING_ENABLED.defaultValue
        )
        BootStrap.setLoggingEnabled(loggingEnabled)
    }

}