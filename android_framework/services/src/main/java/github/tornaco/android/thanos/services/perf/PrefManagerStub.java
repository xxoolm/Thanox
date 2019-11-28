package github.tornaco.android.thanos.services.perf;

import github.tornaco.android.thanos.core.pref.IPrefManager;

public class PrefManagerStub extends IPrefManager.Stub {

    private final PreferenceManagerService provider;

    public PrefManagerStub(PreferenceManagerService provider) {
        this.provider = provider;
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
}
