package github.tornaco.android.thanos.services.xposed.hooks.activity;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor;
import github.tornaco.android.thanos.core.util.OsUtils;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

class ActivityStartHookV25 extends ActivityStartHook {

    ActivityStartHookV25(IActivityStackSupervisor verifier) {
        super(verifier);
    }

    @Override
    Class clzForStartActivityMayWait(XC_LoadPackage.LoadPackageParam lpparam)
            throws ClassNotFoundException {
        return XposedHelpers.findClass(
                OsUtils.isQOrAbove()
                        ? "com.android.server.wm.ActivityStarter"
                        : "com.android.server.am.ActivityStarter", lpparam.classLoader);
    }
}
