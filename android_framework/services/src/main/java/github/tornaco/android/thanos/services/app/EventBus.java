package github.tornaco.android.thanos.services.app;

import android.content.IntentFilter;
import android.os.RemoteCallbackList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import github.tornaco.android.thanos.core.app.event.IEventSubscriber;
import github.tornaco.android.thanos.core.app.event.ThanosEvent;
import github.tornaco.android.thanos.core.util.Timber;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import lombok.Synchronized;
import util.PreconditionUtils;
import util.Singleton;


@SuppressWarnings({"WeakerAccess"})
public class EventBus {

    private final Executor eventPublishExecutor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "EventPub");
        thread.setPriority(Thread.NORM_PRIORITY);
        return thread;
    });

    private final RemoteCallbackList<EventSubscriberClient> eventSubscribers = new RemoteCallbackList<>();
    private static final Singleton<EventBus> BUS = new Singleton<EventBus>() {
        @Override
        protected EventBus create() {
            return new EventBus();
        }
    };

    public static EventBus getInstance() {
        return BUS.get();
    }

    public void publishEventToSubscribersAsync(final ThanosEvent thanosEvent) {
        Completable.fromRunnable(() -> EventBus.this.publishEventToSubscribers(thanosEvent))
                .subscribeOn(Schedulers.from(eventPublishExecutor))
                .subscribe();
    }

    @Synchronized
    private void publishEventToSubscribers(ThanosEvent thanosEvent) {
        if (thanosEvent.getIntent() == null) {
            return;
        }
        int itemCount = eventSubscribers.beginBroadcast();
        try {
            for (int i = 0; i < itemCount; ++i) {
                try {
                    EventSubscriberClient c = eventSubscribers.getBroadcastItem(i);
                    if (c.hasAction(thanosEvent.getIntent().getAction())) {
                        c.onEvent(thanosEvent);
                    }
                } catch (Throwable e) {
                    Timber.e("publishEventToSubscriber %s", e);
                }
            }
        } finally {
            eventSubscribers.finishBroadcast();
        }
    }

    public void registerEventSubscriber(IntentFilter filter, IEventSubscriber subscriber) {
        PreconditionUtils.checkNotNull(subscriber, "subscriber is null");
        PreconditionUtils.checkNotNull(filter, "filter is null");
        eventSubscribers.register(new EventSubscriberClient(filter, subscriber));
    }

    public void unRegisterEventSubscriber(IEventSubscriber subscriber) {
        PreconditionUtils.checkNotNull(subscriber, "subscriber is null");
        eventSubscribers.unregister(new EventSubscriberClient(null, subscriber));
    }
}
