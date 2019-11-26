package github.tornaco.android.thanos.services.profile;

import android.content.ComponentName;
import android.content.Context;
import android.widget.Toast;

import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.S;
import github.tornaco.android.thanos.services.ThanosSchedulers;
import io.reactivex.Completable;

class ThanoxImpl implements IThanox {

    private S server;
    private Context context;

    public ThanoxImpl(S server, Context context) {
        this.server = server;
        this.context = context;
    }

    @Override
    public boolean killPkg(String pkgName) {
        if (server.getPkgManagerService().isPkgInWhiteList(pkgName)) return false;
        if (server.getPkgManagerService().isSystemPhonePkg(pkgName)) return false;
        if (server.getPkgManagerService().isSystemUidPkg(pkgName)) return false;
        server.getActivityManagerService().forceStopPackage(pkgName);
        return true;
    }

    @Override
    public boolean killApp(String appLabel) {
        return false;
    }

    @Override
    public void showShortToast(String msg) {
        Timber.w("showShortToast: %s", msg);
        Completable.fromRunnable(() -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show())
                .subscribeOn(ThanosSchedulers.serverThread()).subscribe();
    }

    @Override
    public void showLongToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public String getFrontPkg() {
        return server.getActivityManagerService().getCurrentFrontApp();
    }

    @Override
    public String getFrontComponentName() {
        ComponentName componentName = server.getActivityStackSupervisor().getCurrentFrontComponentName();
        return componentName == null ? "" : componentName.flattenToString();
    }
}
