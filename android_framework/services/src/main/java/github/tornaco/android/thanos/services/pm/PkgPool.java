package github.tornaco.android.thanos.services.pm;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.IWebViewUpdateService;
import android.webkit.WebViewProviderInfo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.core.Res;
import github.tornaco.android.thanos.core.annotation.Nullable;
import github.tornaco.android.thanos.core.app.AppResources;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.util.ArrayUtils;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.core.util.Timber;
import lombok.Getter;
import lombok.experimental.var;
import lombok.val;
import util.CollectionUtils;

@Getter
public class PkgPool {
    private Context context;
    private PackageManager pm;

    private final Executor executor = Executors.newSingleThreadExecutor();

    private final Set<String> whiteListPkgs = Sets.newHashSet();
    private final Set<Pattern> whiteListPatterns = Sets.newHashSet();
    // To prevent the apps with system signature added to white list.
    private final Set<String> whiteListHook = Sets.newHashSet();
    private final Set<String> webViewProviderPkgs = Sets.newHashSet();
    private final Set<Integer> thanosAppUid = Sets.newHashSet();

    private final List<AppInfo> systemApps = Lists.newArrayList();
    private final List<AppInfo> systemUidApps = Lists.newArrayList();
    private final List<AppInfo> mediaUidApps = Lists.newArrayList();
    private final List<AppInfo> phoneUidApps = Lists.newArrayList();
    private final List<AppInfo> webViewProviderApps = Lists.newArrayList();
    private final List<AppInfo> _3rdApps = Lists.newArrayList();
    private final List<AppInfo> whiteListApps = Lists.newArrayList();

    private final Map<String, AppInfo> allAppsMap = Maps.newConcurrentMap();
    private final Map<String, Integer> pkg2UidMap = Maps.newConcurrentMap();
    private final Map<Integer, ArrayList<String>> uid2PkgMap = Maps.newConcurrentMap();

    public PkgPool(Context context) {
        this.context = context;
        this.pm = context.getPackageManager();
    }

    public void invalidateAll() {
        loadWhiteList();
        cacheWebviewPackages();
        loadAllInstalledApps();
    }

    public void addOrUpdate(String pkgName) {
        loadSingleAppByPackageName(pkgName);
    }

    public void remove(String pkgName) {
        AppInfo dummy = new AppInfo();
        dummy.setPkgName(pkgName);
        _3rdApps.remove(dummy);
        allAppsMap.remove(dummy.getPkgName());
        pkg2UidMap.remove(dummy.getPkgName());
    }

    private void loadWhiteList() {
        AppResources appResources = new AppResources(context, BuildProp.THANOS_APP_PKG_NAME);
        whiteListPkgs.addAll(Arrays.asList(appResources.getStringArray(Res.Strings.STRING_WHILE_LIST_PACKAGES)));
        Timber.d("loadWhiteList:\n%s", Arrays.toString(whiteListPkgs.toArray()));
    }

    private void cacheWebviewPackages() {
        try {
            IWebViewUpdateService w = IWebViewUpdateService.Stub.asInterface(ServiceManager.getService("webviewupdate"));
            WebViewProviderInfo[] providerInfos = w.getValidWebViewPackages();
            if (ArrayUtils.isEmpty(providerInfos)) {
                Timber.wtf("No webview providers found.");
                return;
            }

            // Clear first!
            webViewProviderPkgs.clear();

            for (WebViewProviderInfo info : providerInfos) {
                String pkgName = info.packageName;
                Timber.d("Add webview provider: " + pkgName + ", description: " + info.description);
                webViewProviderPkgs.add(pkgName);
            }
        } catch (Throwable e) {
            Timber.wtf("Fail cacheWebviewPackages: " + Log.getStackTraceString(e));
        }

        Timber.d("cacheWebviewPackages done.");
    }

    private boolean isWebViewProvider(String pkg) {
        return webViewProviderPkgs.contains(pkg);
    }

    private void loadAllInstalledApps() {
        CollectionUtils.consumeRemaining(PkgUtils.getInstalledApplications(context), this::loadApplication);
    }

    private void loadSingleAppByPackageName(String pkgName) {
        ApplicationInfo applicationInfo = PkgUtils.getApplicationInfo(context, pkgName);
        if (applicationInfo == null) {
            Timber.e("loadApp, applicationInfo is null: " + pkgName);
            return;
        }
        loadApplication(applicationInfo);
    }

    private void loadApplication(ApplicationInfo applicationInfo) {
        AppInfo appInfo = parseApplication(applicationInfo);
        if (appInfo == null) {
            Timber.e("Parse app fail: " + applicationInfo);
            return;
        }
        int flags = appInfo.getFlags();

        if ((flags & AppInfo.FLAGS_WHITE_LISTED) != 0) {
            whiteListApps.remove(appInfo);
            whiteListApps.add(appInfo);
        }
        if ((flags & AppInfo.FLAGS_WEB_VIEW_PROVIDER) != 0) {
            webViewProviderApps.remove(appInfo);
            webViewProviderApps.add(appInfo);
        }
        if ((flags & AppInfo.FLAGS_SYSTEM) != 0) {
            systemApps.remove(appInfo);
            systemApps.add(appInfo);
        }
        if ((flags & AppInfo.FLAGS_SYSTEM_MEDIA) != 0) {
            mediaUidApps.remove(appInfo);
            mediaUidApps.add(appInfo);
        }
        if ((flags & AppInfo.FLAGS_SYSTEM_PHONE) != 0) {
            phoneUidApps.remove(appInfo);
            phoneUidApps.add(appInfo);
        }
        if ((flags & AppInfo.FLAGS_SYSTEM_UID) != 0) {
            systemUidApps.remove(appInfo);
            systemUidApps.add(appInfo);
        }
        if ((flags & AppInfo.FLAGS_USER) != 0) {
            _3rdApps.remove(appInfo);
            _3rdApps.add(appInfo);
        }


        allAppsMap.put(appInfo.getPkgName(), appInfo);
        pkg2UidMap.put(appInfo.getPkgName(), appInfo.getUid());

        ArrayList<String> uidPkgs = uid2PkgMap.get(appInfo.getUid());
        if (uidPkgs == null) uidPkgs = Lists.newArrayList();
        uidPkgs.add(appInfo.getPkgName());
        uid2PkgMap.put(appInfo.getUid(), uidPkgs);
    }

    @Nullable
    private AppInfo parseApplication(ApplicationInfo applicationInfo) {
        if (applicationInfo == null) return null;

        String pkgName = applicationInfo.packageName;
        // Cache for thanos.
        if (pkgName.contains(BuildProp.THANOS_APP_PKG_NAME_PREFIX)) {
            thanosAppUid.add(applicationInfo.uid);
            Timber.w("thanosAppUid=%s", thanosAppUid);
        }

        // Enabled/Disable state.
        int pkgState;
        try {
            pkgState = pm.getApplicationEnabledSetting(pkgName);
        } catch (Throwable e) {
            Timber.e(e, "Error getApplicationEnabledSetting for: " + pkgName);
            return null;
        }

        PackageInfo packageInfo = PkgUtils.getPackageInfo(context, pkgName);
        if (packageInfo == null) {
            Timber.e("Error getPackageInfo for %s", pkgName);
            return null;
        }

        return constructAppInfo(applicationInfo, packageInfo,
                detectApplicationFlags(applicationInfo, packageInfo), pkgState);
    }

    private int detectApplicationFlags(ApplicationInfo applicationInfo, PackageInfo packageInfo) {
        int flags = AppInfo.FLAGS_NONE;
        if (whiteListPkgs.contains(applicationInfo.packageName)) {
            flags |= AppInfo.FLAGS_WHITE_LISTED;
        }
        if (isWebViewProvider(applicationInfo.packageName)) {
            flags |= AppInfo.FLAGS_WEB_VIEW_PROVIDER;
        }
        // System apps.
        // Or 3-rd.
        if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
            val sharedUserId = packageInfo.sharedUserId;
            if (PkgUtils.isSharedUserIdMedia(sharedUserId)) {
                flags |= AppInfo.FLAGS_SYSTEM;
            } else if (PkgUtils.isSharedUserIdPhone(sharedUserId)) {
                flags |= AppInfo.FLAGS_SYSTEM;
            } else if (PkgUtils.isSharedUserIdSystem(sharedUserId)) {
                flags |= AppInfo.FLAGS_SYSTEM;
            } else {
                flags |= AppInfo.FLAGS_SYSTEM;
            }
        } else {
            flags |= AppInfo.FLAGS_USER;
        }
        return flags;
    }

    private AppInfo constructAppInfo(ApplicationInfo applicationInfo,
                                     PackageInfo packageInfo,
                                     int flags,
                                     int pkgState) {
        AppInfo appInfo = new AppInfo();
        appInfo.setPkgName(applicationInfo.packageName);


        var loadedLabel = applicationInfo.loadLabel(pm);
        if (TextUtils.isEmpty(loadedLabel)) loadedLabel = appInfo.getPkgName();
        appInfo.setAppLabel(loadedLabel.toString());
        appInfo.setVersionCode(applicationInfo.versionCode);
        appInfo.setVersionName(packageInfo.versionName);
        appInfo.setFlags(flags);
        appInfo.setUid(applicationInfo.uid);
        appInfo.setState(pkgState);
        return appInfo;
    }
}
