package github.tornaco.android.thanos.services.xposed.hooks.activity;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

class ActivityStartHookV21 extends ActivityStartHook {

    ActivityStartHookV21(IActivityStackSupervisor verifier) {
        super(verifier);
    }

    @Override
    Class clzForStartActivityMayWait(XC_LoadPackage.LoadPackageParam lpparam)
            throws ClassNotFoundException {
        return XposedHelpers.findClass("com.android.server.am.ActivityStackSupervisor",
                lpparam.classLoader);
    }
}
