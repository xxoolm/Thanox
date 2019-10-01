package github.tornaco.android.thanos.services.perf;

import android.content.Context;
import android.os.IBinder;
import github.tornaco.android.thanos.core.T;
import github.tornaco.android.thanos.core.pref.IPrefChangeListener;
import github.tornaco.android.thanos.core.pref.IPrefManager;
import github.tornaco.android.thanos.services.SystemService;

import java.io.File;
import java.util.List;

public class PreferenceManagerService extends SystemService implements IPrefManager {

    private SettingsProvider provider;

    public PreferenceManagerService() {
    }

    @Override
    public void onStart(Context context) {
        super.onStart(context);
        // Init Pref.
        // Note: Should init after onStart, because it need a Handler.
        this.provider = SettingsProvider.newInstance(T.baseServerDataDir().getPath() + File.separator + T.serviceContextName() + ".xml");
    }

    @Override
    public IBinder asBinder() {
        return null;
    }

    public List<String> getSettingNames() {
        return provider.getSettingNames();
    }

    @Override
    public boolean putString(String name, String value) {
        return provider.putString(name, value);
    }

    @Override
    public String getString(String name, String def) {
        return provider.getString(name, def);
    }

    @Override
    public boolean putInt(String name, int value) {
        return provider.putInt(name, value);
    }

    @Override
    public int getInt(String name, int def) {
        return provider.getInt(name, def);
    }

    @Override
    public boolean getBoolean(String name, boolean def) {
        return provider.getBoolean(name, def);
    }

    @Override
    public boolean putBoolean(String name, boolean value) {
        return provider.putBoolean(name, value);
    }

    @Override
    public long getLong(String name, long def) {
        return provider.getLong(name, def);
    }

    @Override
    public boolean putLong(String name, long value) {
        return provider.putLong(name, value);
    }

    public boolean registerSettingsChangeListener(IPrefChangeListener listener) {
        return provider.registerSettingsChangeListener(listener);
    }

    public boolean unRegisterSettingsChangeListener(IPrefChangeListener listener) {
        return provider.unRegisterSettingsChangeListener(listener);
    }

    public static SettingsProvider newInstance(String path) {
        return SettingsProvider.newInstance(path);
    }
}
