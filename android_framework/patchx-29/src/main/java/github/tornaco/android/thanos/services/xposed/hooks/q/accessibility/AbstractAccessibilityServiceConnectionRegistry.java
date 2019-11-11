package github.tornaco.android.thanos.services.xposed.hooks.q.accessibility;

import android.view.accessibility.AccessibilityEvent;

import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.annotation.XposedHook;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._29;

@XposedHook(targetSdkVersion = {_29})
@Beta
public class AbstractAccessibilityServiceConnectionRegistry implements IXposedHook {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookNotifyAccessibilityEvent(lpparam);
        }
    }

    private void hookNotifyAccessibilityEvent(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.d("hookNotifyAccessibilityEvent...");

        try {
            // com/android/server/accessibility/AbstractAccessibilityServiceConnection.java
            Class clz = XposedHelpers.findClass("com.android.server.accessibility.AccessibilityManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(clz, "sendAccessibilityEvent", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    AccessibilityEvent event = (AccessibilityEvent) param.args[0];
                    Timber.v("sendAccessibilityEvent: %s", event);
                }
            });
            Timber.d("hookNotifyAccessibilityEvent OK: " + unHooks);
        } catch (Exception e) {
            Timber.e(e, "Fail hookNotifyAccessibilityEvent");
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Noop.
    }
}
