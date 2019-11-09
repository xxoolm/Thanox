package github.tornaco.android.thanos.services;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

public class BackgroundThread extends HandlerThread {

    private static BackgroundThread sInstance;
    private static Handler sHandler;

    private BackgroundThread() {
        super("thanos.bg", Process.THREAD_PRIORITY_BACKGROUND);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new BackgroundThread();
            sInstance.start();
            sHandler = new ErrorSafetyHandler(sInstance.getLooper());
        }

    }

    public static BackgroundThread get() {
        BootStrap.enforceSystemStarted();
        synchronized (BackgroundThread.class) {
            ensureThreadLocked();
            return sInstance;
        }
    }

    public static Handler getHandler() {
        BootStrap.enforceSystemStarted();
        synchronized (BackgroundThread.class) {
            ensureThreadLocked();
            return sHandler;
        }
    }

}
