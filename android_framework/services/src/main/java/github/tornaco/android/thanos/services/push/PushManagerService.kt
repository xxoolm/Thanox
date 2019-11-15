package github.tornaco.android.thanos.services.push

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.app.event.IEventSubscriber
import github.tornaco.android.thanos.core.app.event.ThanosEvent
import github.tornaco.android.thanos.core.persist.JsonObjectSetRepo
import github.tornaco.android.thanos.core.persist.RepoFactory
import github.tornaco.android.thanos.core.push.IPushManager
import github.tornaco.android.thanos.core.push.PushChannel
import github.tornaco.android.thanos.core.util.Timber
import github.tornaco.android.thanos.services.S
import github.tornaco.android.thanos.services.SystemService
import github.tornaco.android.thanos.services.apihint.ExecuteBySystemHandler
import github.tornaco.android.thanos.services.app.EventBus
import util.CollectionUtils
import java.util.*

class PushManagerService(s: S) : SystemService(), IPushManager {
    private val dummyChannel =
        PushChannel(
            arrayOf("android.intent.action.TIME_TICK"),
            "android:dummy",
            "D78A8F3D-0FC2-4A45-A913-280DC73598E0"
        )

    private lateinit var channelRepo: JsonObjectSetRepo<PushChannel>

    private val eventSubscriber = object : IEventSubscriber.Stub() {
        override fun onEvent(e: ThanosEvent) {
            handleNewIntent(e.intent)
        }
    }

    override fun onStart(context: Context) {
        super.onStart(context)
        channelRepo =
            RepoFactory.get().getOrCreateJsonObjectSetRepo(
                T.pushChannelsFile().path,
                PushChannelRepo::class.java
            )
        Timber.v("channelRepo: %s", channelRepo)
        registerReceivers()
        registerPrebuiltChannels()
    }

    private fun registerPrebuiltChannels() {
        registerChannel(PushChannel.FCM_GCM)
        registerChannel(PushChannel.MIPUSH)
        registerChannel(dummyChannel)
    }

    override fun unRegisterChannel(channel: PushChannel?) {
        executeInternal(Runnable { unRegisterChannelInternal(channel) })
    }

    @ExecuteBySystemHandler
    fun unRegisterChannelInternal(channel: PushChannel?) {
        Timber.d("unRegisterChannel: %s", channel)
        channelRepo.remove(Objects.requireNonNull(channel, "Channel is null"))

        unRegisterReceivers()
        registerReceivers()
    }

    override fun registerChannel(channel: PushChannel?) {
        executeInternal(Runnable { registerChannelInternal(channel) })
    }

    @ExecuteBySystemHandler
    fun registerChannelInternal(channel: PushChannel?) {
        Timber.d("registerChannel: %s", channel)
        channelRepo.add(Objects.requireNonNull(channel, "Channel is null"))

        unRegisterReceivers()
        registerReceivers()
    }

    fun handleNewIntent(intent: Intent) {
        executeInternal(Runnable { handleNewIntentInternal(intent) })
    }

    @ExecuteBySystemHandler
    fun handleNewIntentInternal(intent: Intent) {
        CollectionUtils.consumeRemaining(channelRepo.all) { channel ->
            if (channel.match(intent)) {
                Timber.d("handleNewIntentInternal matches channel: %s", channel)
            }
        }
    }

    private fun registerReceivers() {
        val filter = IntentFilter()
        // Add actions.
        channelRepo.all.forEach { channel -> channel.actions.forEach(filter::addAction) }
        EventBus.getInstance().registerEventSubscriber(filter, eventSubscriber)
    }

    private fun unRegisterReceivers() {
        EventBus.getInstance().unRegisterEventSubscriber(eventSubscriber)
    }

    override fun shutdown() {
        super.shutdown()
        unRegisterReceivers()
    }

    override fun asBinder(): IBinder {
        return github.tornaco.android.thanos.core.util.Noop.notSupported()
    }
}