package github.tornaco.android.thanos.services.audio

import android.content.Context
import android.os.IBinder
import android.text.TextUtils
import github.tornaco.android.thanos.core.audio.IAudioManager
import github.tornaco.android.thanos.core.util.Noop
import github.tornaco.android.thanos.core.util.Timber
import github.tornaco.android.thanos.services.S
import github.tornaco.android.thanos.services.SystemService
import java.util.concurrent.atomic.AtomicReference

class AudioService(private val s: S) : SystemService(), IAudioManager {

    private val currentFocusedPackageName: AtomicReference<String> = AtomicReference()

    override fun onStart(context: Context) {
        super.onStart(context)
    }

    override fun systemReady() {
        super.systemReady()
    }

    override fun hasAudioFocus(pkgName: String?): Boolean {
        return !TextUtils.isEmpty(pkgName) && pkgName.equals(currentFocusedPackageName.get())
    }

    fun onRequestAudioFocus(pkgName: String) {
        Timber.v("onRequestAudioFocus: %s", String)
        currentFocusedPackageName.set(pkgName)
    }

    fun onAbandonAudioFocus(pkgName: String) {
        Timber.v("onAbandonAudioFocus: %s", String)
        if (pkgName == currentFocusedPackageName.get()) {
            currentFocusedPackageName.set(null)
        }
    }

    override fun asBinder(): IBinder {
        return Noop.notSupported()
    }
}