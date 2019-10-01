package github.tornaco.android.thanos.services.xposed.hooks.audio;

import android.text.TextUtils;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.core.util.obs.StackProxy;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.annotation.XposedHook;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

import java.util.Set;
import java.util.Stack;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions.*;

@XposedHook(targetSdkVersion = {_23, _24, _25, _26, _27, _28, _29})
@Beta
public class MediaFocusRegistry implements IXposedHook {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookMediaFocus(lpparam);
        }
    }

    private void hookMediaFocus(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.v("hookMediaFocus...");
        try {
            final Class clazz = XposedHelpers.findClass("com.android.server.audio.MediaFocusControl",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllConstructors(clazz, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Stack requestStack = (Stack) XposedHelpers.getObjectField(param.thisObject, "mFocusStack");
                    Timber.d("Original requestStack: %s", requestStack);
                    @SuppressWarnings("unchecked") RequestStackProxy proxy = RequestStackProxy.newProxy(requestStack);
                    XposedHelpers.setObjectField(param.thisObject, "mFocusStack", proxy);
                }
            });
            Timber.v("hookMediaFocus OK:" + unHooks);
        } catch (Exception e) {
            Timber.e(e, "Fail hook hookMediaFocus");
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Noop.
    }

    static class RequestStackProxy<T> extends StackProxy<T> {
        static <T> RequestStackProxy<T> newProxy(Stack<T> orig) {
            return new RequestStackProxy<>(orig);
        }

        RequestStackProxy(Stack<T> orig) {
            super(orig);
        }

        @Override
        public T pop() {
            T t = super.pop();
            String pkgName = (String) XposedHelpers.getObjectField(t, "mPackageName");
            if (!TextUtils.isEmpty(pkgName)) {
                Completable
                        .fromRunnable(() -> BootStrap.THANOS_X.getAudioService().onAbandonAudioFocus(pkgName))
                        .subscribeOn(Schedulers.trampoline())
                        .subscribe();
            }
            return t;
        }

        @Override
        public T push(T t) {
            String pkgName = (String) XposedHelpers.getObjectField(t, "mPackageName");
            if (!TextUtils.isEmpty(pkgName)) {
                Completable
                        .fromRunnable(() -> BootStrap.THANOS_X.getAudioService().onRequestAudioFocus(pkgName))
                        .subscribeOn(Schedulers.trampoline())
                        .subscribe();
            }
            return super.push(t);
        }
    }
}
