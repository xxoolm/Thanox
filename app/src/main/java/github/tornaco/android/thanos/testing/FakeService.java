package github.tornaco.android.thanos.testing;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class FakeService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }
}
