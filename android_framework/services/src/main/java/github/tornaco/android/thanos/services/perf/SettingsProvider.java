package github.tornaco.android.thanos.services.perf;

import android.os.HandlerThread;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.util.Log;
import github.tornaco.android.thanos.core.pref.IPrefChangeListener;
import github.tornaco.android.thanos.services.ThanosSchedulers;
import io.reactivex.Completable;
import io.reactivex.Scheduler;
import lombok.Synchronized;

import java.io.File;
import java.util.List;


/**
 * Created by guohao4 on 2017/12/19.
 * Email: Tornaco@163.com
 */

public class SettingsProvider {

    private static final String TAG = "SettingsProvider";

    private SettingsState settingsState;
    private final Object lock = new Object();

    private final Scheduler scheduler;

    private final RemoteCallbackList<IPrefChangeListener> prefChangeListenerRemoteCallbackList = new RemoteCallbackList<>();

    private SettingsProvider(String path, int key) {

        HandlerThread stateThread = new HandlerThread("SettingsProvider#" + path);
        stateThread.start();
        Looper stateLooper = stateThread.getLooper();
        scheduler = ThanosSchedulers.from(stateLooper);

        initSettingsState(stateLooper, path, key);
    }

    private void initSettingsState(Looper looper, String path, int key) {
        settingsState = new SettingsState(lock,
                new File(path),
                key,
                -1, // No limit.
                looper);
    }

    private boolean insertSettingLocked(final String name, String value) {
        synchronized (lock) {
            boolean res = settingsState.insertSettingLocked(name, value, "tornaco", true, "android");
            if (res) {
                Completable.fromRunnable(() -> notifySettingsChangeListener(name))
                        .subscribeOn(scheduler)
                        .subscribe();
            }
            return res;
        }
    }

    private String getSettingLocked(String name) {
        synchronized (lock) {
            SettingsState.Setting setting = settingsState.getSettingLocked(name);
            if (setting.isNull()) {
                return null;
            }
            return setting.getValue();
        }
    }

    public List<String> getSettingNames() {
        synchronized (lock) {
            return settingsState.getSettingNamesLocked();
        }
    }

    public boolean putString(String name, String value) {
        try {
            return insertSettingLocked(name, value);
        } catch (Throwable e) {
            Log.e(TAG, "putString" + Log.getStackTraceString(e));
            return false;
        }
    }

    public String getString(String name, String def) {
        try {
            return getSettingLocked(name);
        } catch (Throwable e) {
            Log.e(TAG, "getString" + Log.getStackTraceString(e));
            return def;
        }
    }

    public boolean putInt(String name, int value) {
        return putString(name, String.valueOf(value));
    }

    public int getInt(String name, int def) {
        try {
            String s = getString(name, String.valueOf(def));
            if (s == null) {
                return def;
            }
            return Integer.parseInt(s);
        } catch (Throwable e) {
            Log.e(TAG, "getInt" + Log.getStackTraceString(e));
            return def;
        }
    }

    public boolean getBoolean(String name, boolean def) {
        String v = getSettingLocked(name);
        if (v == null) {
            return def;
        }
        try {
            return Boolean.parseBoolean(v);
        } catch (Throwable e) {
            Log.e(TAG, "getBoolean" + Log.getStackTraceString(e));
            return def;
        }
    }

    public boolean putBoolean(String name, boolean value) {
        try {
            return insertSettingLocked(name, String.valueOf(value));
        } catch (Throwable e) {
            Log.e(TAG, "putBoolean" + Log.getStackTraceString(e));
            return false;
        }
    }

    public long getLong(String name, long def) {
        String v = getSettingLocked(name);
        if (v == null) {
            return def;
        }
        try {
            return Long.parseLong(v);
        } catch (Throwable e) {
            Log.e(TAG, "getLong" + Log.getStackTraceString(e));
            return def;
        }
    }

    public boolean putLong(String name, long value) {
        try {
            return insertSettingLocked(name, String.valueOf(value));
        } catch (Throwable e) {
            Log.e(TAG, "putLong" + Log.getStackTraceString(e));
            return false;
        }
    }

    public boolean registerSettingsChangeListener(IPrefChangeListener listener) {
        return listener != null && prefChangeListenerRemoteCallbackList.register(listener);
    }

    public boolean unRegisterSettingsChangeListener(IPrefChangeListener listener) {
        return listener != null && prefChangeListenerRemoteCallbackList.unregister(listener);
    }

    @Synchronized
    private void notifySettingsChangeListener(String name) {
        try {
            int itemCount = prefChangeListenerRemoteCallbackList.beginBroadcast();
            for (int i = 0; i < itemCount; i++) {
                try {
                    IPrefChangeListener listener = prefChangeListenerRemoteCallbackList.getBroadcastItem(i);
                    try {
                        listener.onPrefChanged(name);
                    } catch (Throwable e) {
                        Log.wtf(TAG, "notifySettingsChangeListener fail call onChange! "
                                + Log.getStackTraceString(e));
                    }
                } catch (Throwable ignored) {
                    // We tried...
                }
            }
        } catch (Throwable e) {
            Log.wtf(TAG, "notifySettingsChangeListener err: "
                    + Log.getStackTraceString(e));
        } finally {
            prefChangeListenerRemoteCallbackList.finishBroadcast();
            // If dead, go dead!!!!!
        }
    }

    public static SettingsProvider newInstance(String path) {
        return new SettingsProvider(path, path.hashCode());
    }
}
