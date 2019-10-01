package github.tornaco.android.thanos.services.xposed.hooks.activity;

import github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

class TaskMoverHookV25 extends TaskMoverHookV24 {

    TaskMoverHookV25(IActivityStackSupervisor verifier) {
        super(verifier);
    }
}
