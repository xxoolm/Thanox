package github.tornaco.android.thanos.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.UserHandle;
import android.util.Log;
import de.robv.android.xposed.XposedBridge;
import github.tornaco.android.thanos.core.util.Timber;
import io.reactivex.plugins.RxJavaPlugins;
import lombok.Getter;

// Do not convert to kotlin.
public class BootStrap {
    private static final String LOG_PREFIX = "[ThanosS]";

    private static int currentLogLevel = Log.VERBOSE;

    private BootStrap() {
        // Noop.
    }

    @SuppressLint("StaticFieldLeak")
    public static final ThanosService THANOS_X = new ThanosService();

    @Getter
    private static State state = State.BOOTING;

    // Entry.
    public static void main() {

        Timber.plant(new Timber.DebugTree() {
            @Override
            protected void log(int priority, String tag, String message, Throwable t) {
                if (priority < currentLogLevel) return;
                String logTag = logTagWithUserId(Timber.tagWithPrefix(LOG_PREFIX, tag));
                logTag = logTagWithThreadId(logTag);
                super.log(priority, logTag, message, t);
                if (priority >= Log.WARN) {
                    // Redirect to exposed too.
                    XposedBridge.log(logTag + "\t" + message);
                }
            }
        });

        // Error handler default print error info.
        RxJavaPlugins.setErrorHandler(throwable -> {
            Timber.e("\n");
            Timber.e("==== Thanos un-handled error, please file a bug ====");
            Timber.e(throwable);
            Timber.e("\n");
        });

        Timber.i("Bring up with thanos: %s, sdk: %s", THANOS_X, Build.VERSION.SDK_INT);
    }

    public static void start(Context context) {
        state = State.STARTED;
        THANOS_X.onStart(context);
    }

    public static void ready() {
        state = State.READY;
        THANOS_X.systemReady();
    }


    public static void shutdown() {
        state = State.SHUTDOWN;
        THANOS_X.shutdown();
    }

    public enum State {
        BOOTING, STARTED, READY, SHUTDOWN
    }

    public static void enforceSystemStarted() {
        // During class init, state is null.
        if (state == null || state.ordinal() < BootStrap.State.STARTED.ordinal()) {
            throw new IllegalStateException("System is not ready!!!");
        }
    }

    public static void setCurrentLogLevel(int level) {
        currentLogLevel = level;
    }

    public static int getCurrentLogLevel() {
        return currentLogLevel;
    }

    private static String logTagWithUserId(String tag) {
        return state != null && state.ordinal() >= State.STARTED.ordinal()
                ? tag + "[u:" + UserHandle.getCallingUserId() + "]"
                : tag;
    }

    private static String logTagWithThreadId(String tag) {
        return tag + "[t:" + Thread.currentThread().getName() + "]";
    }
}
