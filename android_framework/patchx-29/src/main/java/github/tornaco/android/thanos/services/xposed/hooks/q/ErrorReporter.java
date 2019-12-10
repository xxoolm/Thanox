package github.tornaco.android.thanos.services.xposed.hooks.q;

import github.tornaco.android.thanos.services.BootStrap;

public class ErrorReporter {

    public static void report(String registryName, String errorMessage) {
        BootStrap.THANOS_X.reportFrameworkInitializeError(registryName, errorMessage);
    }
}
