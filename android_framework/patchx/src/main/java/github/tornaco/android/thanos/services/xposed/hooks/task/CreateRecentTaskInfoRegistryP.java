package github.tornaco.android.thanos.services.xposed.hooks.task;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.annotation.Keep;
import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.xposed.annotation.XposedHook;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._28;

@XposedHook(targetSdkVersion = {_28})
@Beta
@Keep
public class CreateRecentTaskInfoRegistryP extends CreateRecentTaskInfoRegistry {

    @Override
    protected Class clazzToHook(XC_LoadPackage.LoadPackageParam lpparam) {
        return XposedHelpers.findClass("com.android.server.am.RecentTasks",
                lpparam.classLoader);
    }

    @Override
    protected String methodToHook() {
        return "createRecentTaskInfo";
    }
}
