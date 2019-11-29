package github.tornaco.android.thanos.services.profile.handle;

import android.content.Context;
import android.content.Intent;

import github.tornaco.android.thanos.services.S;

interface ILauncher {

    boolean launchProcessForPackage(String pkgName);

    boolean launchActivity(Intent intent);

    boolean launchMainActivityForPackage(String pkgName);

    Intent getLaunchIntentForPackage(String pkgName);

    class Impl implements ILauncher {
        private Context context;
        private S s;

        public Impl(Context context, S s) {
            this.context = context;
            this.s = s;
        }

        @Override
        public boolean launchProcessForPackage(String pkgName) {
            return false;
        }

        @Override
        public boolean launchActivity(Intent intent) {
            return false;
        }

        @Override
        public boolean launchMainActivityForPackage(String pkgName) {
            return false;
        }

        @Override
        public Intent getLaunchIntentForPackage(String pkgName) {
            return null;
        }
    }
}
