package github.tornaco.android.thanos.services.xposed.hooks;

import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.annotation.XposedHook;
import lombok.AllArgsConstructor;

import java.util.Set;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._28;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._29;

@AllArgsConstructor
@XposedHook(targetSdkVersion = {_28, _29})
public class ActiveServicesBindingFixRegistry implements IXposedHook {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookBindService(lpparam);
        }
    }

    // bindServiceLocked
    // if (!mAm.mIntentFirewall.checkService(r.name, service, callingUid, callingPid,
    //                    resolvedType, r.appInfo)) {
    //                return new ServiceLookupResult(null, "blocked by firewall");
    // }
    //
    //
    // Android Q will check ifw and return null record, which cause ContextImpl
    // throw SecureException.
    //
    //
    // Stack trace like:
    // java.lang.SecurityException: Not allowed to bind to service Intent { act=android.content.SyncAdapter cmp=com.youdao.translator/.wakeup.SyncService (has extras) }
    //	at android.app.ContextImpl.bindServiceCommon(ContextImpl.java:1740)
    //	at android.app.ContextImpl.bindServiceAsUser(ContextImpl.java:1675)
    //	at com.android.server.content.SyncManager$ActiveSyncContext.bindToSyncAdapter(SyncManager.java:1917)
    //	at com.android.server.content.SyncManager$SyncHandler.dispatchSyncOperation(SyncManager.java:3539)
    //	at com.android.server.content.SyncManager$SyncHandler.startSyncH(SyncManager.java:3217)
    //	at com.android.server.content.SyncManager$SyncHandler.handleSyncMessage(SyncManager.java:2948)
    //	at com.android.server.content.SyncManager$SyncHandler.handleMessage(SyncManager.java:2920)
    //	at android.os.Handler.dispatchMessage(Handler.java:107)
    //	at android.os.Looper.loop(Looper.java:214)
    //	at android.os.HandlerThread.run(HandlerThread.java:67)
    //
    //
    // So, we will fix it.
    private void hookBindService(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class ams = XposedHelpers.findClass("com.android.server.am.ActiveServices",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams, "bindServiceLocked",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            int res = (int) param.getResult();
                            if (res < 0) {
                                Timber.w("bindServiceLocked result < 0, we will fix it to 0.");
                                // Do not crash.
                                param.setResult(0);
                            }
                        }
                    });
            Timber.i("bindServiceLocked, unhooks %s", unHooks);
        } catch (Exception e) {
            Timber.e("bindServiceLocked error %s", Log.getStackTraceString(e));
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Nothing.
    }
}
