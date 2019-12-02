package github.tornaco.android.thanos.services.profile.handle;

import android.content.Context;
import android.content.Intent;

import github.tornaco.android.thanos.core.T;
import github.tornaco.android.thanos.services.S;

interface ITask {

    void removeTasksForPackage(String pkgName);

    boolean hasTaskFromPackage(String pkgName);

    void clearBackgroundTasks();

    class Impl implements ITask {
        private Context context;
        private S s;

        Impl(Context context, S s) {
            this.context = context;
            this.s = s;
        }

        @Override
        public void removeTasksForPackage(String pkgName) {
            s.getActivityManagerService().removeTaskForPackage(pkgName);
        }

        @Override
        public boolean hasTaskFromPackage(String pkgName) {
            return s.getActivityManagerService().getRecentTasks().hasRecentTaskForPkg(pkgName);
        }

        @Override
        public void clearBackgroundTasks() {
            context.sendBroadcast(new Intent(T.Actions.ACTION_RUNNING_PROCESS_CLEAR));
        }
    }
}
