package github.tornaco.android.thanos.core.util;

import android.os.Handler;
import github.tornaco.android.thanos.core.annotation.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lombok.AllArgsConstructor;

import java.util.concurrent.Executor;

public class Rxs {
    public static final Consumer<Throwable> ON_ERROR_LOGGING = Timber::e;

    public static final Action EMPTY_ACTION = () -> {
        // Empty.
    };

    public static final Consumer<Object> EMPTY_CONSUMER = o -> {
        // Noop.
    };

    public static Executor fromHandler(Handler handler) {
        return new HandlerExecutor(handler);
    }

    @AllArgsConstructor
    private static class HandlerExecutor implements Executor {
        private Handler handler;

        @Override
        public void execute(@NonNull Runnable runnable) {
            handler.post(runnable);
        }
    }

    public static class Executors {
        private static final Executor IO = java.util.concurrent.Executors.newCachedThreadPool();

        private Executors() {
        }

        public static Executor io() {
            return IO;
        }
    }
}
