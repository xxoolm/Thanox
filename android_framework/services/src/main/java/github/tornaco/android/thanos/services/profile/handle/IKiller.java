package github.tornaco.android.thanos.services.profile.handle;

import android.content.Context;

import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.services.S;

interface IKiller {

    boolean killPackage(String pkgName);

    class Impl implements IKiller {
        private Context context;
        private S s;

        Impl(Context context, S s) {
            this.context = context;
            this.s = s;
        }

        @Override
        public boolean killPackage(String pkgName) {
            if (!PkgUtils.isPkgInstalled(context, pkgName)) return false;
            s.getActivityManagerService().forceStopPackage(pkgName);
            return true;
        }
    }
}
