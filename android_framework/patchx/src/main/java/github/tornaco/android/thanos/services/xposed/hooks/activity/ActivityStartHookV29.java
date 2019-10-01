package github.tornaco.android.thanos.services.xposed.hooks.activity;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor;

public class ActivityStartHookV29 extends ActivityStartHookV28 {

    ActivityStartHookV29(IActivityStackSupervisor verifier) {
        super(verifier);
    }

    @Override
    Class clzForStartActivityMayWait(XC_LoadPackage.LoadPackageParam lpparam)
            throws ClassNotFoundException {
        return XposedHelpers.findClass("com.android.server.wm.ActivityStarter", lpparam.classLoader);
    }
}
