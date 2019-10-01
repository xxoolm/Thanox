/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package github.tornaco.android.thanos.services;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.android.schedulers.DroppingScheduler;
import io.reactivex.android.schedulers.HandlerScheduler;

/**
 * Android-specific Schedulers.
 */
public final class ThanosSchedulers {

    private static final class MainHolder {
        static Scheduler def() {
            return new HandlerScheduler(new Handler(LooperHolder.def()));
        }
    }

    private static final class LooperHolder {
        static Looper def() {
            HandlerThread thread = new HandlerThread("ThanosSchedulers");
            thread.start();
            return thread.getLooper();
        }
    }

    private static final Scheduler THANOS_SERVER_THREAD = RxAndroidPlugins.initMainThreadScheduler(MainHolder::def);
    private static final Scheduler ALWAYS_THROWS = new DroppingScheduler();

    /**
     * A {@link Scheduler} which executes actions on the Thanos server thread.
     */
    public static Scheduler serverThread() {
        return BootStrap.getState() == BootStrap.State.BOOTING
                ? ALWAYS_THROWS
                : THANOS_SERVER_THREAD;
    }

    /**
     * A {@link Scheduler} which executes actions on {@code looper}.
     */
    public static Scheduler from(Looper looper) {
        if (looper == null) throw new NullPointerException("looper == null");
        return new HandlerScheduler(new Handler(looper));
    }

    /**
     * A {@link Scheduler} which executes actions on {@code handler}.
     */
    public static Scheduler from(Handler handler) {
        if (handler == null) throw new NullPointerException("handler == null");
        return new HandlerScheduler(handler);
    }

    private ThanosSchedulers() {
        throw new AssertionError("No instances.");
    }
}
