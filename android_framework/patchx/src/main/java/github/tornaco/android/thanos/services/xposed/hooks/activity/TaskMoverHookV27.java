package github.tornaco.android.thanos.services.xposed.hooks.activity;


import github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

// oid findTaskToMoveToFrontLocked(TaskRecord task, int flags, ActivityOptions options,
//            String reason, boolean forceNonResizeable) {
class TaskMoverHookV27 extends TaskMoverHookV26 {

    TaskMoverHookV27(IActivityStackSupervisor verifier) {
        super(verifier);
    }
}
