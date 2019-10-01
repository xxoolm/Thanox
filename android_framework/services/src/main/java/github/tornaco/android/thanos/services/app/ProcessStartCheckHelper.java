package github.tornaco.android.thanos.services.app;

import com.google.common.collect.Sets;
import github.tornaco.android.thanos.core.app.start.StartReason;
import lombok.Getter;

import java.util.Set;

class ProcessStartCheckHelper {

    private static final String HOST_TYPE_ACTIVITY = "activity";
    private static final String HOST_TYPE_BROADCAST = "broadcast";
    private static final String HOST_TYPE_CONTENT_PROVIDER = "content provider";
    private static final String HOST_TYPE_SERVICE = "service";

    @Getter
    private final Set<String> processCheckType = Sets.newHashSet(HOST_TYPE_CONTENT_PROVIDER, HOST_TYPE_BROADCAST);

    int getStartReasonFromHostType(String string) {
        if (string == null) return StartReason.OTHERS;
        switch (string) {
            case HOST_TYPE_ACTIVITY:
                return StartReason.ACTIVITY;
            case HOST_TYPE_BROADCAST:
                return StartReason.BROADCAST;
            case HOST_TYPE_CONTENT_PROVIDER:
                return StartReason.PROVIDER;
            case HOST_TYPE_SERVICE:
                return StartReason.SERVICE;
            default:
                return StartReason.OTHERS;
        }
    }
}
