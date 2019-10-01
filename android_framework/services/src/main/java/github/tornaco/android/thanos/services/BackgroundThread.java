package github.tornaco.android.thanos.services;

import android.os.Process;
import android.os.*;
import io.reactivex.Completable;

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

    public static class ErrorSafetyHandler extends Handler {
        ErrorSafetyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Completable.fromAction(() -> ErrorSafetyHandler.super.handleMessage(msg)).subscribe();
        }

        @Override
        public void dispatchMessage(Message msg) {
            Completable.fromAction(() -> ErrorSafetyHandler.super.dispatchMessage(msg)).subscribe();
        }
    }
}
