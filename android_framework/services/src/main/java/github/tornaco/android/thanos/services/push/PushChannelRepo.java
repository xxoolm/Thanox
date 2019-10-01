package github.tornaco.android.thanos.services.push;

import android.os.Handler;
import com.google.gson.reflect.TypeToken;
import github.tornaco.android.thanos.core.persist.JsonObjectSetRepo;
import github.tornaco.android.thanos.core.push.PushChannel;

import java.io.File;
import java.util.concurrent.ExecutorService;

public class PushChannelRepo extends JsonObjectSetRepo<PushChannel> {

    public PushChannelRepo(File file, Handler handler, ExecutorService service) {
        super(file, handler, service);
    }

    @Override
    protected TypeToken onCreateTypeToken() {
        return new TypeToken<PushChannel>() {
        };
    }
}
