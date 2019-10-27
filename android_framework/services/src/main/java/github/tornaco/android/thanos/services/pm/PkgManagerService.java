package github.tornaco.android.thanos.services.pm;

import android.content.ComponentName;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.UserHandle;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.core.T;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.IPkgManager;
import github.tornaco.android.thanos.core.util.FileUtils;
import github.tornaco.android.thanos.core.util.Noop;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BackgroundThread;
import github.tornaco.android.thanos.services.S;
import github.tornaco.android.thanos.services.ThanoxSystemService;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PkgManagerService extends ThanoxSystemService implements IPkgManager {
    @Getter
    private final PkgCache pkgCache = new PkgCache();

    @Getter
    private final PackageMonitor monitor = new PackageMonitor() {
        @Override
        public void onPackageAdded(String packageName, int uid) {
            super.onPackageAdded(packageName, uid);
            getPkgCache().invalidate();
        }

        @Override
        public void onPackageRemoved(String packageName, int uid) {
            super.onPackageRemoved(packageName, uid);
            getPkgCache().invalidate();
            if (Objects.equals(packageName, BuildProp.THANOS_APP_PKG_NAME)) {
                onThanoxAppPackageRemoved();
            }
        }

        @Override
        public boolean onPackageChanged(String packageName, int uid, String[] components) {
            getPkgCache().invalidate();
            return super.onPackageChanged(packageName, uid, components);
        }
    };

    public PkgManagerService(S s) {
        super(s);
    }

    @Override
    public void onStart(Context context) {
        super.onStart(context);
        getPkgCache().onStart(context);
    }

    @Override
    public void systemReady() {
        super.systemReady();
        getPkgCache().invalidate();
        getMonitor().register(getContext(), UserHandle.CURRENT, true, BackgroundThread.getHandler());
    }

    @Override
    public void shutdown() {
        super.shutdown();
        getMonitor().unregister();
    }

    @Override
    public String[] getPkgNameForUid(int uid) {
        return getPkgCache().getUid2Pkg().containsKey(uid)
                ? getPkgCache().getUid2Pkg().get(uid).toArray(new String[0])
                : null;
    }

    @Override
    public int getUidForPkgName(String pkgName) {
        Integer uid = getPkgCache().getPkg2Uid().get(pkgName);
        return uid == null ? -1 : uid;
    }

    public int getThanosAppUid() {
        return getPkgCache().getThanosAppUid();
    }

    @Override
    public AppInfo[] getInstalledPkgs(int flags) {
        List<AppInfo> res = new ArrayList<>();
        if ((flags & AppInfo.FLAGS_SYSTEM) != 0) {
            res.addAll(pkgCache.getSystemApps());
            Timber.d("getInstalledPkgs, adding FLAGS_SYSTEM");
        }
        if ((flags & AppInfo.FLAGS_SYSTEM_MEDIA) != 0) {
            res.addAll(pkgCache.getMediaUidApps());
            Timber.d("getInstalledPkgs, adding FLAGS_SYSTEM_MEDIA");
        }
        if ((flags & AppInfo.FLAGS_SYSTEM_PHONE) != 0) {
            res.addAll(pkgCache.getPhoneUidApps());
            Timber.d("getInstalledPkgs, adding FLAGS_SYSTEM_PHONE");
        }
        if ((flags & AppInfo.FLAGS_SYSTEM_UID) != 0) {
            res.addAll(pkgCache.getSystemUidApps());
            Timber.d("getInstalledPkgs, adding FLAGS_SYSTEM_UID");
        }
        if ((flags & AppInfo.FLAGS_USER) != 0) {
            res.addAll(pkgCache.get_3rdApps());
            Timber.d("getInstalledPkgs, adding FLAGS_USER");
        }
        if ((flags & AppInfo.FLAGS_WEB_VIEW_PROVIDER) != 0) {
            res.addAll(pkgCache.getWebViewProviderApps());
            Timber.d("getInstalledPkgs, adding FLAGS_WEB_VIEW_PROVIDER");
        }
        if ((flags & AppInfo.FLAGS_WHITE_LISTED) != 0) {
            res.addAll(pkgCache.getWhiteListApps());
            Timber.d("getInstalledPkgs, adding FLAGS_WHITE_LISTED");
        }
        return res.toArray(new AppInfo[0]);
    }

    @Override
    public AppInfo getAppInfo(String pkgName) {
        return pkgCache.getAllApps().get(pkgName);
    }

    @Override
    public String[] getWhiteListPkgs() {
        return pkgCache.getWhiteList().toArray(new String[0]);
    }

    @Override
    public boolean isPkgInWhiteList(String pkg) {
        return pkgCache.getWhiteList().contains(pkg);
    }

    public boolean isSystemUidPkg(String pkg) {
        AppInfo appInfo = getAppInfo(pkg);
        return appInfo != null && appInfo.getFlags() == AppInfo.FLAGS_SYSTEM_UID;
    }

    public boolean isSystemPhonePkg(String pkg) {
        AppInfo appInfo = getAppInfo(pkg);
        return appInfo != null && appInfo.getFlags() == AppInfo.FLAGS_SYSTEM_PHONE;
    }

    public boolean isSystemMediaPkg(String pkg) {
        AppInfo appInfo = getAppInfo(pkg);
        return appInfo != null && appInfo.getFlags() == AppInfo.FLAGS_SYSTEM_MEDIA;
    }

    @Override
    public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags) {
        enforceCallingPermissions();
        executeInternal(() -> Objects.requireNonNull(getContext()).getPackageManager().setComponentEnabledSetting(componentName, newState, flags));
    }

    @Override
    public int getComponentEnabledSetting(ComponentName componentName) {
        long ident = Binder.clearCallingIdentity();
        try {
            return Objects.requireNonNull(getContext()).getPackageManager().getComponentEnabledSetting(componentName);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    @Override
    public int getApplicationEnabledSetting(String packageName) {
        long ident = Binder.clearCallingIdentity();
        try {
            return Objects.requireNonNull(getContext()).getPackageManager().getApplicationEnabledSetting(packageName);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    @Override
    public void setApplicationEnabledSetting(String packageName, int newState, int flags, boolean tmp) {
        enforceCallingPermissions();
        executeInternal(() -> Objects.requireNonNull(getContext()).getPackageManager().setApplicationEnabledSetting(packageName, newState, flags));
    }

    private void onThanoxAppPackageRemoved() {
        // Clean up resources.
        executeInternal(() -> {
            File dir = T.baseServerDir();
            FileUtils.deleteDirQuiet(dir);
            Timber.w("onThanoxAppPackageRemoved, thanox data has been cleaned.");
        });
    }

    @Override
    public IBinder asBinder() {
        return Noop.notSupported();
    }
}
