package github.tornaco.android.thanos.services.profile.handle;

import android.content.Context;

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

        }

        @Override
        public boolean hasTaskFromPackage(String pkgName) {
            return false;
        }

        @Override
        public void clearBackgroundTasks() {

        }
    }
}
