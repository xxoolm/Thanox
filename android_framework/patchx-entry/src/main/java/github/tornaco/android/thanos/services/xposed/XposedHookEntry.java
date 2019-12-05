package github.tornaco.android.thanos.services.xposed;

import android.os.Build;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.annotation.Keep;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.xposed.patchx.XposedHookRegistry_PATCHX;
import github.tornaco.xposed.patchx29.XposedHookRegistry_PATCHX29;

@Keep
public class XposedHookEntry implements IXposedHook {

    static {
        BootStrap.main();
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        for (IXposedHookLoadPackage loadPackage : XposedHookRegistry_PATCHX.getXposedHookLoadPackageForSdk(Build.VERSION.SDK_INT)) {
            loadPackage.handleLoadPackage(lpparam);
        }
        for (IXposedHookLoadPackage loadPackage : XposedHookRegistry_PATCHX29.getXposedHookLoadPackageForSdk(Build.VERSION.SDK_INT)) {
            loadPackage.handleLoadPackage(lpparam);
        }

        // Dummy.
        ErrorReporter.report("XposedHookEntry-dummy-error", "This is dummy error.");
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        for (IXposedHookZygoteInit init : XposedHookRegistry_PATCHX.getXposedHookZygoteInitForSdk(Build.VERSION.SDK_INT)) {
            init.initZygote(startupParam);
        }
        for (IXposedHookZygoteInit init : XposedHookRegistry_PATCHX29.getXposedHookZygoteInitForSdk(Build.VERSION.SDK_INT)) {
            init.initZygote(startupParam);
        }
    }
}
