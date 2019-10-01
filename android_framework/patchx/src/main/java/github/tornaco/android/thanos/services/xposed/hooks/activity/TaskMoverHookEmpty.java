package github.tornaco.android.thanos.services.xposed.hooks.activity;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor;
import github.tornaco.android.thanos.core.util.Timber;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

class TaskMoverHookEmpty extends TaskMoverHook {

    TaskMoverHookEmpty(IActivityStackSupervisor verifier) {
        super(verifier);
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Timber.e("Empty TaskMoverHookEmpty!!!");
    }
}
