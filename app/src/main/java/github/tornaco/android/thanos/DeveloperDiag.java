package github.tornaco.android.thanos;

import android.app.Application;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.util.Timber;

class DeveloperDiag {

    static void diag(Application application) {
        ThanosManager.from(application)
                .ifServiceInstalled(thanosManager -> Timber.w("Platform app idle enabled: %s", (thanosManager.getActivityManager().isPlatformAppIdleEnabled())));
    }
}
