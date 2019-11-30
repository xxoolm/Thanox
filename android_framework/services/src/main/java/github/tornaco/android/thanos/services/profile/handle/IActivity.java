package github.tornaco.android.thanos.services.profile.handle;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.S;

interface IActivity {

    boolean launchProcessForPackage(String pkgName);

    boolean launchActivity(Intent intent);

    boolean launchMainActivityForPackage(String pkgName);

    Intent getLaunchIntentForPackage(String pkgName);

    String getFrontAppPackage();

    ComponentName getFrontAppPackageComponent();

    class Impl implements IActivity {
        private Context context;
        private S s;

        Impl(Context context, S s) {
            this.context = context;
            this.s = s;
        }

        @Override
        public boolean launchProcessForPackage(String pkgName) {
            return false;
        }

        @Override
        public boolean launchActivity(Intent intent) {
            try {
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return true;
            } catch (Exception e) {
                Timber.e(e);
                return false;
            }
        }

        @Override
        public boolean launchMainActivityForPackage(String pkgName) {
            if (!PkgUtils.isPkgInstalled(context, pkgName)) return false;
            Intent main = getLaunchIntentForPackage(pkgName);
            if (main == null) return false;
            return launchActivity(main);
        }

        @Override
        public Intent getLaunchIntentForPackage(String pkgName) {
            if (!PkgUtils.isPkgInstalled(context, pkgName)) return null;
            return context.getPackageManager()
                    .getLaunchIntentForPackage(pkgName);
        }

        @Override
        public String getFrontAppPackage() {
            return s.getActivityStackSupervisor().getCurrentFrontApp();
        }

        @Override
        public ComponentName getFrontAppPackageComponent() {
            return s.getActivityStackSupervisor().getCurrentFrontComponentName();
        }
    }
}
