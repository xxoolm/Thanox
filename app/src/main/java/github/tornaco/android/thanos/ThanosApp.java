package github.tornaco.android.thanos;

import github.tornaco.android.thanos.core.util.Timber;
import io.reactivex.plugins.RxJavaPlugins;

public class ThanosApp extends MultipleModulesApp {
    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onCreate() {
        super.onCreate();

        // Install error handler.
        // Error handler default print error info.
        RxJavaPlugins.setErrorHandler(throwable -> {
            Timber.e("\n");
            Timber.e("==== App un-handled error, please file a bug ====");
            Timber.e(throwable);
            Timber.e("\n");
        });

        CrashHandler.install(this);

        if (BuildConfig.DEBUG) {
            DeveloperDiag.diag(this);
        }
    }

    public static boolean isPrc() {
        return getMarket() == Market.PRC;
    }

    public static Market getMarket() {
        return Market.valueOf(BuildConfig.FLAVOR.toUpperCase());
    }

    public enum Market {
        PRC, ROW
    }
}
