package github.tornaco.android.thanos.services.xposed.hooks;

import android.content.Context;
import android.os.ServiceManager;
import android.util.Log;

import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.T;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.annotation.XposedHook;
import util.ObjectsUtils;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._21;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._22;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._23;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._24;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._25;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._26;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._27;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._28;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._29;

@XposedHook(targetSdkVersion = {_21, _22, _23, _24, _25, _26, _27, _28, _29})
public class ContextServiceRegistry implements IXposedHook {

    @Override
    public void initZygote(StartupParam startupParam) {
        hookSystemServiceRegistry();
    }

    private void hookSystemServiceRegistry() {
        try {
            Class clz = XposedHelpers.findClass(
                    "android.app.SystemServiceRegistry", null);
            Set unHooks = XposedBridge.hookAllMethods(clz,
                    "getSystemService", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            Context context = (Context) param.args[0];
                            String name = (String) param.args[1];
                            if (ObjectsUtils.equals(name, T.serviceContextName())) {
                                XposedBridge.log("getSystemService of thanos called...");
                                IThanos thanos = IThanos.Stub.asInterface(
                                        ServiceManager.getService(T.serviceInstallName()));
                                param.setResult(new ThanosManager(context, thanos));
                            }
                        }
                    });
            XposedBridge.log("hookSystemServiceRegistry, unhooks=" + unHooks);
        } catch (Exception e) {
            XposedBridge.log("hookSystemServiceRegistry, err=" + Log.getStackTraceString(e));
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

    }
}
