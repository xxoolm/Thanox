package github.tornaco.android.thanos.services.app;

import android.content.IntentFilter;

import github.tornaco.android.thanos.core.app.event.IEventSubscriber;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import util.ObjectsUtils;

@AllArgsConstructor
class EventSubscriberClient extends IEventSubscriber.Stub {
    private IntentFilter intentFilter;
    @Delegate
    private IEventSubscriber subscriber;

    public boolean hasAction(String action) {
        return intentFilter != null && intentFilter.hasAction(action);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventSubscriberClient that = (EventSubscriberClient) o;
        return ObjectsUtils.equals(subscriber, that.subscriber);
    }

    @Override
    public int hashCode() {
        return ObjectsUtils.hash(subscriber);
    }
}