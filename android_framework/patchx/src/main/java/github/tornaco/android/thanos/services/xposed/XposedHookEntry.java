package github.tornaco.android.thanos.services.xposed;

import android.os.Build;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.annotation.Keep;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.xposed.XposedHookRegistry;

@Keep
public class XposedHookEntry implements IXposedHook {

    static {
        BootStrap.main();
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        for (IXposedHookLoadPackage loadPackage : XposedHookRegistry.getXposedHookLoadPackageForSdk(Build.VERSION.SDK_INT)) {
            Timber.v("Invoke handleLoadPackage: %s %s", lpparam.packageName, loadPackage);
            loadPackage.handleLoadPackage(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        for (IXposedHookZygoteInit init : XposedHookRegistry.getXposedHookZygoteInitForSdk(Build.VERSION.SDK_INT)) {
            Timber.v("Invoke initZygote: %s", init);
            init.initZygote(startupParam);
        }
    }
}
