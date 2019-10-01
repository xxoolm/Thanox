package github.tornaco.android.thanos.services.xposed.hooks.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor;
import github.tornaco.android.thanos.core.util.OsUtils;

import java.lang.reflect.Method;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

//void findTaskToMoveToFront(TaskRecord task, int flags, ActivityOptions options, String reason,
//        boolean forceNonResizeable) {
class TaskMoverHookV28 extends TaskMoverHookV27 {
    TaskMoverHookV28(IActivityStackSupervisor verifier) {
        super(verifier);
    }

    @SuppressLint("PrivateApi")
    @Override
    Method methodForTaskMover(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException, NoSuchMethodException {
        String clazzName = OsUtils.isQOrAbove() ? "com.android.server.wm.TaskRecord" : "com.android.server.am.TaskRecord";
        @SuppressLint("PrivateApi")
        Class taskRecordClass = Class.forName(clazzName, false, lpparam.classLoader);
        @SuppressLint("PrivateApi") final Method moveToFront
                = Class.forName(
                OsUtils.isQOrAbove()
                        ? "com.android.server.wm.ActivityStackSupervisor"
                        : "com.android.server.am.ActivityStackSupervisor",
                false, lpparam.classLoader)
                .getDeclaredMethod("findTaskToMoveToFront",
                        taskRecordClass, int.class, ActivityOptions.class,
                        String.class, boolean.class);
        return moveToFront;
    }
}
