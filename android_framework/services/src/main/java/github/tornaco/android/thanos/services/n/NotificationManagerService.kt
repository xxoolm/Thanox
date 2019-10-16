package github.tornaco.android.thanos.services.n

import android.content.Context
import android.os.IBinder
import android.os.PowerManager
import android.os.RemoteCallbackList
import android.os.SystemClock
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.n.INotificationManager
import github.tornaco.android.thanos.core.n.INotificationObserver
import github.tornaco.android.thanos.core.n.NotificationRecord
import github.tornaco.android.thanos.core.persist.RepoFactory
import github.tornaco.android.thanos.core.persist.StringSetRepo
import github.tornaco.android.thanos.core.pref.IPrefChangeListener
import github.tornaco.android.thanos.core.util.Noop
import github.tornaco.android.thanos.core.util.Preconditions
import github.tornaco.android.thanos.core.util.Timber
import github.tornaco.android.thanos.services.BackgroundThread
import github.tornaco.android.thanos.services.S
import github.tornaco.android.thanos.services.SystemService
import github.tornaco.android.thanos.services.ThanosSchedulers
import github.tornaco.android.thanos.services.apihint.ExecuteBySystemHandler
import github.tornaco.java.common.util.ObjectsUtils
import io.reactivex.Completable
import java.util.concurrent.ConcurrentHashMap

class NotificationManagerService(private val s: S) : SystemService(), INotificationManager {

    private val notificationRecords = ConcurrentHashMap<String, MutableList<NotificationRecord>>()
    private val observers = RemoteCallbackList<INotificationObserver>()

    private lateinit var screenOnNotificationPkgs: StringSetRepo

    private var screenOnNotificationEnabled = false

    override fun onStart(context: Context) {
        super.onStart(context)
        screenOnNotificationPkgs = RepoFactory.get().getOrCreateStringSetRepo(T.screenOnNotificationPkgsFile().path)
    }

    override fun systemReady() {
        super.systemReady()
        initPrefs()
    }

    private fun initPrefs() {
        readPrefs()
        listenToPrefs()
    }

    private fun readPrefs() {
        val preferenceManagerService = s.preferenceManagerService
        this.screenOnNotificationEnabled = preferenceManagerService.getBoolean(
            T.Settings.PREF_SCREEN_ON_NOTIFICATION_ENABLED.key,
            T.Settings.PREF_SCREEN_ON_NOTIFICATION_ENABLED.defaultValue
        )
    }

    private fun listenToPrefs() {
        val listener = object : IPrefChangeListener.Stub() {
            override fun onPrefChanged(key: String) {
                if (ObjectsUtils.equals(T.Settings.PREF_SCREEN_ON_NOTIFICATION_ENABLED.key, key)) {
                    Timber.i("Pref changed, reload.")
                    readPrefs()
                }
            }
        }
        s.preferenceManagerService.registerSettingsChangeListener(listener)
    }

    override fun getNotificationRecordsForPackage(packageName: String?): Array<NotificationRecord> {
        return notificationRecords[packageName]!!.toTypedArray()
    }

    override fun hasNotificationRecordsForPackage(packageName: String?): Boolean {
        return !notificationRecords[packageName].isNullOrEmpty()
    }

    fun onAddNotificationRecord(record: NotificationRecord) {
        executeInternal(Runnable {
            onAddNotificationRecordInternal(record)
        })
    }

    @ExecuteBySystemHandler
    private fun onAddNotificationRecordInternal(record: NotificationRecord) {
        Timber.d("onAddNotificationRecordInternal: %s", record)
        var list: MutableList<NotificationRecord>? = notificationRecords[record.pkg]
        if (list == null) list = mutableListOf()
        list.add(record)
        notificationRecords[record.pkg] = list

        lightOnScreenIfNeed(record)

        notifyNewNotification(record)
    }

    private fun lightOnScreenIfNeed(record: NotificationRecord) {
        if (screenOnNotificationEnabled && screenOnNotificationPkgs.has(record.pkg)) {
            Timber.d("lightOnScreenIfNeed, will light on")
            val powerManager: PowerManager = context!!.getSystemService(Context.POWER_SERVICE) as PowerManager
            powerManager.wakeUp(SystemClock.uptimeMillis())
        }
    }

    fun onRemoveNotificationRecord(record: NotificationRecord) {
        executeInternal(Runnable {
            onRemoveNotificationRecordInternal(record)
        })
    }

    @ExecuteBySystemHandler
    private fun onRemoveNotificationRecordInternal(record: NotificationRecord) {
        Timber.d("onRemoveNotificationRecordInternal: %s", record)
        notificationRecords.remove(record.pkg)

        notifyRemoveNotification(record)
    }

    override fun unRegisterObserver(obs: INotificationObserver?) {
        observers.unregister(Preconditions.checkNotNull(obs))
    }

    override fun registerObserver(obs: INotificationObserver?) {
        observers.register(Preconditions.checkNotNull(obs))
    }

    private fun notifyNewNotification(record: NotificationRecord) {
        Completable
            .fromRunnable {
                val count = observers.beginBroadcast()
                Timber.v("Call notifyNewNotification of obs count: %s", count)
                for (i in 0 until count) {
                    try {
                        Timber.v("Call notifyNewNotification of obs index: %s", i)
                        observers.getBroadcastItem(i).onNewNotification(record)
                    } catch (e: Throwable) {
                        Timber.e(e, "Error notifyNewNotification")
                    }
                }
                observers.finishBroadcast()
            }
            .subscribeOn(ThanosSchedulers.from(BackgroundThread.getHandler()))
            .subscribe()
    }

    private fun notifyRemoveNotification(record: NotificationRecord) {
        Completable
            .fromRunnable {
                val count = observers.beginBroadcast()
                Timber.v("Call notifyNewNotification of obs count: %s", count)
                for (i in 0 until count) {
                    try {
                        Timber.v("Call onNotificationRemoved of obs index: %s", i)
                        observers.getBroadcastItem(i).onNotificationRemoved(record)
                    } catch (e: Throwable) {
                        Timber.e(e, "Error notifyRemoveNotification")
                    }
                }
                observers.finishBroadcast()
            }
            .subscribeOn(ThanosSchedulers.from(BackgroundThread.getHandler()))
            .subscribe()
    }

    override fun isScreenOnNotificationEnabledForPkg(pkg: String?): Boolean {
        return screenOnNotificationPkgs.has(pkg)
    }

    override fun setScreenOnNotificationEnabledForPkg(pkg: String?, enable: Boolean) {
        if (enable) screenOnNotificationPkgs.add(pkg) else screenOnNotificationPkgs.remove(pkg)
    }

    override fun setScreenOnNotificationEnabled(enable: Boolean) {
        this.screenOnNotificationEnabled = enable
        val preferenceManagerService = s.preferenceManagerService
        preferenceManagerService.putBoolean(
            T.Settings.PREF_SCREEN_ON_NOTIFICATION_ENABLED.key,
            enable
        )
    }

    override fun isScreenOnNotificationEnabled(): Boolean {
        return screenOnNotificationEnabled
    }

    override fun asBinder(): IBinder {
        return Noop.notSupported()
    }
}