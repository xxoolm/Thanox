package github.tornaco.android.thanos;

import android.app.Application;
import github.tornaco.android.thanos.core.util.Timber;

public class MultipleModulesApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
