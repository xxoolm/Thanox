package github.tornaco.android.thanos.services.pm;

import android.content.ComponentName;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.UserHandle;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.core.T;
import github.tornaco.android.thanos.core.annotation.Nullable;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.IPkgManager;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.ArrayUtils;
import github.tornaco.android.thanos.core.util.FileUtils;
import github.tornaco.android.thanos.core.util.Noop;
import github.tornaco.android.thanos.core.util.Optional;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BackgroundThread;
import github.tornaco.android.thanos.services.S;
import github.tornaco.android.thanos.services.ThanoxSystemService;
import lombok.Getter;
import lombok.val;

public class PkgManagerService extends ThanoxSystemService implements IPkgManager {
    @Getter
    @Nullable
    private Optional<PkgPool> pkgCache;

    @Getter
    private final PackageMonitor monitor = new PackageMonitor() {
        @Override
        public void onPackageAdded(String packageName, int uid) {
            super.onPackageAdded(packageName, uid);
            pkgCache.ifPresent(pkgPool -> pkgPool.addOrUpdate(packageName));
        }

        @Override
        public void onPackageRemoved(String packageName, int uid) {
            super.onPackageRemoved(packageName, uid);
            pkgCache.ifPresent(pkgPool -> pkgPool.remove(packageName));
            if (Objects.equals(packageName, BuildProp.THANOS_APP_PKG_NAME)) {
                onThanoxAppPackageRemoved();
            }
        }

        @Override
        public boolean onPackageChanged(String packageName, int uid, String[] components) {
            pkgCache.ifPresent(pkgPool -> pkgPool.addOrUpdate(packageName));
            return super.onPackageChanged(packageName, uid, components);
        }
    };

    public PkgManagerService(S s) {
        super(s);
    }

    @Override
    public void onStart(Context context) {
        super.onStart(context);
    }

    @Override
    public void systemReady() {
        super.systemReady();
        this.pkgCache = Optional.of(new PkgPool(getContext()));
        this.pkgCache.ifPresent(PkgPool::invalidateAll);
        getMonitor().register(getContext(), UserHandle.CURRENT, true, BackgroundThread.getHandler());
    }

    @Override
    public void shutdown() {
        super.shutdown();
        getMonitor().unregister();
    }

    @Override
    public String[] getPkgNameForUid(int uid) {
        // If this is system@1000, return 'android'
        if (PkgUtils.isSystemCall(uid)) {
            return new String[]{PackageManager.packageNameOfAndroid()};
        }
        // If this is system/phone...etc <=2000, return 'android'
        if (PkgUtils.isSystemOrPhoneOrShell(uid)) {
            return new String[]{PackageManager.packageNameOfAndroid()};
        }
        if (!pkgCache.isPresent()) {
            return null;
        }
        val pool = pkgCache.get();
        return pool.getUid2PkgMap().containsKey(uid)
                ? pool.getUid2PkgMap().get(uid).toArray(new String[0])
                : null;
    }

    @Nullable
    public String getFirstPkgNameForUid(int uid) {
        String[] all = getPkgNameForUid(uid);
        if (ArrayUtils.isEmpty(all)) {
            return null;
        }
        if (all.length > 1) {
            Timber.e(new Throwable("Here"), "Found more than 1 pkgs for uid: %s, they are: %s", uid, Arrays.toString(all));
        }
        return all[0];
    }

    @Override
    public int getUidForPkgName(String pkgName) {
        if (!pkgCache.isPresent()) {
            return -1;
        }
        val pool = pkgCache.get();
        Integer uid = pool.getPkg2UidMap().get(pkgName);
        return uid == null ? -1 : uid;
    }

    public boolean mayBeThanosAppUid(int uid) {
        if (!pkgCache.isPresent()) {
            return false;
        }
        val pool = pkgCache.get();
        return pool.getThanosAppUid().contains(uid);
    }

    @Override
    public AppInfo[] getInstalledPkgs(int flags) {
        if (!pkgCache.isPresent()) {
            return new AppInfo[0];
        }
        val pool = pkgCache.get();
        List<AppInfo> res = new ArrayList<>();
        if ((flags & AppInfo.FLAGS_SYSTEM) != 0) {
            res.addAll(pool.getSystemApps());
            Timber.d("getInstalledPkgs, adding FLAGS_SYSTEM");
        }
        if ((flags & AppInfo.FLAGS_SYSTEM_MEDIA) != 0) {
            res.addAll(pool.getMediaUidApps());
            Timber.d("getInstalledPkgs, adding FLAGS_SYSTEM_MEDIA");
        }
        if ((flags & AppInfo.FLAGS_SYSTEM_PHONE) != 0) {
            res.addAll(pool.getPhoneUidApps());
            Timber.d("getInstalledPkgs, adding FLAGS_SYSTEM_PHONE");
        }
        if ((flags & AppInfo.FLAGS_SYSTEM_UID) != 0) {
            res.addAll(pool.getSystemUidApps());
            Timber.d("getInstalledPkgs, adding FLAGS_SYSTEM_UID");
        }
        if ((flags & AppInfo.FLAGS_USER) != 0) {
            res.addAll(pool.get_3rdApps());
            Timber.d("getInstalledPkgs, adding FLAGS_USER");
        }
        if ((flags & AppInfo.FLAGS_WEB_VIEW_PROVIDER) != 0) {
            res.addAll(pool.getWebViewProviderApps());
            Timber.d("getInstalledPkgs, adding FLAGS_WEB_VIEW_PROVIDER");
        }
        if ((flags & AppInfo.FLAGS_WHITE_LISTED) != 0) {
            res.addAll(pool.getWhiteListApps());
            Timber.d("getInstalledPkgs, adding FLAGS_WHITE_LISTED");
        }
        return res.toArray(new AppInfo[0]);
    }

    @Override
    public AppInfo getAppInfo(String pkgName) {
        if (!pkgCache.isPresent()) {
            return null;
        }
        val pool = pkgCache.get();
        return pool.getAllAppsMap().get(pkgName);
    }

    @Override
    public String[] getWhiteListPkgs() {
        if (!pkgCache.isPresent()) {
            return new String[0];
        }
        val pool = pkgCache.get();
        return pool.getWhiteListPkgs().toArray(new String[0]);
    }

    @Override
    public boolean isPkgInWhiteList(String pkg) {
        if (!pkgCache.isPresent()) {
            return false;
        }
        val pool = pkgCache.get();
        return pool.getWhiteListPkgs().contains(pkg);
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
