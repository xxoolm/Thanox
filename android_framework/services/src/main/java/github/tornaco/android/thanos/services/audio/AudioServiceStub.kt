package github.tornaco.android.thanos.services.audio

import android.os.IBinder
import github.tornaco.android.thanos.core.audio.IAudioManager

class AudioServiceStub(private val service: AudioService) : IAudioManager.Stub(), IAudioManager by service {
    override fun asBinder(): IBinder {
        return super.asBinder()
    }
}

