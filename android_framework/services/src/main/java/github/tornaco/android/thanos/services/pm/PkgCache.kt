package github.tornaco.android.thanos.services.pm

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.ServiceManager
import android.text.TextUtils
import android.util.Log
import android.webkit.IWebViewUpdateService
import com.google.common.collect.Lists
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.core.Res
import github.tornaco.android.thanos.core.app.AppResources
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.util.*
import github.tornaco.java.common.util.CollectionUtils
import github.tornaco.java.common.util.Consumer
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.regex.Pattern
import kotlin.collections.HashSet

internal class PkgCache {
    private var context: Context? = null
    private val executor = Executors.newSingleThreadExecutor()

    val whiteList = HashSet<String>()
    val whiteListPatterns = ArrayList<Pattern>()
    // To prevent the apps with system signature added to white list.
    val whiteListHook = ArrayList<String>()
    // Installed in system/, not contains system-packages and persist packages.

    // Regular system apps.
    val systemApps = ArrayList<AppInfo>()
    val systemUidApps = ArrayList<AppInfo>()
    val mediaUidApps = ArrayList<AppInfo>()
    val phoneUidApps = ArrayList<AppInfo>()
    val webViewProviderApps = ArrayList<AppInfo>()
    val _3rdApps = ArrayList<AppInfo>()
    val whiteListApps = ArrayList<AppInfo>()

    val allApps = ConcurrentHashMap<String, AppInfo>()

    val pkg2Uid = ConcurrentHashMap<String, Int>()
    val uid2Pkg = ConcurrentHashMap<Int, ArrayList<String>>()

    val webViewProviderPkgs = ArrayList<String>()

    var thanosAppUid = 0

    fun onStart(context: Context) {
        this.context = context
    }

    fun invalidate() {
        Timber.i("invalidate")
        loadInstalledPkgs()
    }

    @Suppress("DEPRECATION")
    private fun loadInstalledPkgs() {
        DevNull.accept(
            Completable.fromAction {
                clear()
            }.andThen(Completable.fromAction {
                loadWhiteList()
            }).andThen(Completable.fromAction {
                cacheWebviewPackacgaes()
            }).andThen(Observable.fromIterable(getInstalledApplications()))
                .subscribeOn(Schedulers.from(executor))
                .subscribe(io.reactivex.functions.Consumer { applicationInfo ->
                    parseApplication(applicationInfo)
                }, Rxs.ON_ERROR_LOGGING, Action {
                    onApplicationsParsed()
                })
        )
    }

    private fun getInstalledApplications(): List<ApplicationInfo> {
        val pm = context!!.packageManager
        return if (OsUtils.isNOrAbove())
            pm.getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES)
        else
            pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES)
    }

    private fun loadWhiteList() {
        val appResources = AppResources(context, BuildProp.THANOS_APP_PKG_NAME)
        whiteList.addAll(listOf(*appResources.getStringArray(Res.Strings.STRING_WHILE_LIST_PACKAGES)))
        Timber.d("loadWhiteList:\n%s", Arrays.toString(whiteList.toTypedArray()))
    }

    private fun cacheWebviewPackacgaes() {
        try {
            val w = IWebViewUpdateService.Stub.asInterface(
                ServiceManager
                    .getService("webviewupdate")
            )
            val providerInfos = w.validWebViewPackages
            if (providerInfos == null || providerInfos.isEmpty()) {
                Timber.wtf("No webview providers found.")
                return
            }

            // Clear first!
            webViewProviderPkgs.clear()

            for (info in providerInfos) {
                val pkgName = info.packageName
                Timber.d("Add webview provider: " + pkgName + ", description: " + info.description)

                if (!webViewProviderPkgs.contains(pkgName)) webViewProviderPkgs.add(pkgName)
            }
        } catch (e: Throwable) {
            Timber.wtf("Fail cacheWebviewPackacgaes: " + Log.getStackTraceString(e))
        }

        Timber.d("cacheWebviewPackacgaes")
    }

    private fun isWebViewProvider(pkg: String): Boolean {
        return webViewProviderPkgs.contains(pkg)
    }

    private fun parseApplication(applicationInfo: ApplicationInfo) {
        val pkgName = applicationInfo.packageName
        val pm = context!!.packageManager

        // Cache for thanos.
        if (pkgName == BuildProp.THANOS_APP_PKG_NAME) {
            thanosAppUid = applicationInfo.uid
            Timber.w("thanosAppUid=%s", thanosAppUid)
        }

        val packageInfo = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                pm.getPackageInfo(pkgName, PackageManager.MATCH_UNINSTALLED_PACKAGES)
            } else {
                pm.getPackageInfo(pkgName, PackageManager.GET_UNINSTALLED_PACKAGES)
            }
        } catch (nnf: PackageManager.NameNotFoundException) {
            Timber.e("Error getPackageInfo for $pkgName", nnf)
            return
        }

        // WhiteList
        when {

            whiteList.contains(pkgName) -> whiteListApps.add(
                this@PkgCache.constructAppInfo(
                    pm,
                    packageInfo,
                    applicationInfo,
                    AppInfo.FLAGS_WHITE_LISTED
                )
            )

            this@PkgCache.isWebViewProvider(pkgName) -> webViewProviderApps.add(
                this@PkgCache.constructAppInfo(
                    pm,
                    packageInfo,
                    applicationInfo,
                    AppInfo.FLAGS_WEB_VIEW_PROVIDER
                )
            )

            applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0 ->
                try {
                    val sharedUserId = packageInfo.sharedUserId

                    when {
                        PkgUtils.isSharedUserIdMedia(sharedUserId) -> mediaUidApps.add(
                            this@PkgCache.constructAppInfo(
                                pm,
                                packageInfo,
                                applicationInfo,
                                AppInfo.FLAGS_SYSTEM_MEDIA
                            )
                        )
                        PkgUtils.isSharedUserIdPhone(sharedUserId) -> phoneUidApps.add(
                            this@PkgCache.constructAppInfo(
                                pm,
                                packageInfo,
                                applicationInfo,
                                AppInfo.FLAGS_SYSTEM_PHONE
                            )
                        )
                        PkgUtils.isSharedUserIdSystem(sharedUserId) -> systemUidApps.add(
                            this@PkgCache.constructAppInfo(
                                pm,
                                packageInfo,
                                applicationInfo,
                                AppInfo.FLAGS_SYSTEM_UID
                            )
                        )
                        else -> systemApps.add(
                            this@PkgCache.constructAppInfo(
                                pm,
                                packageInfo,
                                applicationInfo,
                                AppInfo.FLAGS_SYSTEM
                            )
                        )
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    Timber.e(e)
                }

            else -> _3rdApps.add(this@PkgCache.constructAppInfo(pm, packageInfo, applicationInfo, AppInfo.FLAGS_USER))
        }
    }

    private fun onApplicationsParsed() {
        val consumer: Consumer<AppInfo> = Consumer { appInfo ->
            allApps[appInfo.pkgName] = appInfo
            pkg2Uid[appInfo.pkgName] = appInfo.uid

            var pkgSet: ArrayList<String>? = uid2Pkg[appInfo.uid]
            if (pkgSet == null) pkgSet = Lists.newArrayList()
            pkgSet!!.add(appInfo.pkgName)
            uid2Pkg[appInfo.uid] = pkgSet
        }

        CollectionUtils.consumeRemaining(mediaUidApps, consumer)
        CollectionUtils.consumeRemaining(phoneUidApps, consumer)
        CollectionUtils.consumeRemaining(systemUidApps, consumer)
        CollectionUtils.consumeRemaining(systemApps, consumer)
        CollectionUtils.consumeRemaining(_3rdApps, consumer)
        CollectionUtils.consumeRemaining(webViewProviderApps, consumer)
        CollectionUtils.consumeRemaining(whiteListApps, consumer)

        Timber.d("onApplicationsParsed")
    }

    private fun constructAppInfo(
        pm: PackageManager,
        packageInfo: PackageInfo,
        applicationInfo: ApplicationInfo,
        flags: Int
    ): AppInfo {
        val appInfo = AppInfo()
        appInfo.pkgName = applicationInfo.packageName
        var loadedLabel = applicationInfo.loadLabel(pm)
        if (TextUtils.isEmpty(loadedLabel)) loadedLabel = appInfo.pkgName
        appInfo.appLabel = loadedLabel.toString()
        appInfo.versionCode = applicationInfo.versionCode
        appInfo.versionName = packageInfo.versionName
        appInfo.flags = flags
        appInfo.uid = applicationInfo.uid
        return appInfo
    }

    private fun clear() {
        whiteListApps.clear()
        webViewProviderApps.clear()
        mediaUidApps.clear()
        phoneUidApps.clear()
        systemUidApps.clear()
        systemApps.clear()
        _3rdApps.clear()

        allApps.clear()
        pkg2Uid.clear()
        uid2Pkg.clear()

        Timber.d("Clear")
    }
}
