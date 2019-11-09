package github.tornaco.android.thanos.core.util;

import android.os.Handler;
import android.os.HandlerThread;

public class HandlerUtils {

    public static Handler newHandlerOfNewThread(String name) {
        HandlerThread hr = new HandlerThread(name);
        hr.start();
        return new Handler(hr.getLooper());
    }
}
