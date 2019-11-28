package github.tornaco.android.thanos.services.perf

import github.tornaco.android.thanos.core.pref.IPrefManager

class PrefManagerStub(private val provider: PreferenceManagerService) : IPrefManager.Stub() {

    override fun putInt(key: String, value: Int): Boolean {
        return provider.putInt(key, value)
    }

    override fun getInt(key: String, def: Int): Int {
        return provider.getInt(key, def)
    }

    override fun getBoolean(name: String, def: Boolean): Boolean {
        return provider.getBoolean(name, def)
    }

    override fun putBoolean(name: String, value: Boolean): Boolean {
        return provider.putBoolean(name, value)
    }

    override fun getLong(name: String, def: Long): Long {
        return provider.getLong(name, def)
    }

    override fun putLong(name: String, value: Long): Boolean {
        return provider.putLong(name, value)
    }

    override fun putString(name: String, value: String): Boolean {
        return provider.putString(name, value)
    }

    override fun getString(name: String, def: String): String? {
        return provider.getString(name, def)
    }
}
