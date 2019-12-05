package github.tornaco.android.thanos.services.xposed.hooks.notification;

import android.util.Log;

import java.util.List;
import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.annotation.Keep;
import github.tornaco.android.thanos.core.n.NotificationRecord;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.core.util.obs.ListProxy;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.util.NotificationRecordUtils;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.android.thanos.services.xposed.hooks.ErrorReporter;
import github.tornaco.xposed.annotation.XposedHook;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

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
@Keep
public class NMSRegistry implements IXposedHook {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookNMSStart(lpparam);
        }
    }

    private void hookNMSStart(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class nms = XposedHelpers.findClass("com.android.server.notification.NotificationManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(nms, "onStart", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    // Install list proxy.
                    List mNotificationList = (List) XposedHelpers
                            .getObjectField(param.thisObject, "mNotificationList");
                    Timber.d("mNotificationList: " + mNotificationList);
                    @SuppressWarnings("unchecked")
                    List proxyList = NotificationRecordListProxy.newProxy(mNotificationList);
                    XposedHelpers.setObjectField(param.thisObject, "mNotificationList", proxyList);
                }
            });
            Timber.i("hookNMSStart, unhooks %s", unHooks);
        } catch (Throwable e) {
            Timber.e("hookNMSStart error %s", Log.getStackTraceString(e));
            ErrorReporter.report("hookNMSStart", Log.getStackTraceString(e));
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Noop.
    }

    private static class NotificationRecordListProxy<T> extends ListProxy<T> {

        static <T> NotificationRecordListProxy<T> newProxy(List<T> orig) {
            return new NotificationRecordListProxy<>(orig);
        }

        NotificationRecordListProxy(List<T> orig) {
            super(orig);
        }

        @Override
        public void add(int i, T e) {
            super.add(i, e);
            Completable.fromRunnable(() -> {
                NotificationRecord record = NotificationRecordUtils.fromLegacy(e);
                BootStrap.THANOS_X.getNotificationManagerService().onAddNotificationRecord(record);
            }).subscribeOn(Schedulers.trampoline()).subscribe();
        }

        @Override
        public boolean add(T e) {
            boolean added = super.add(e);
            if (added) {
                Completable.fromRunnable(() -> {
                    NotificationRecord record = NotificationRecordUtils.fromLegacy(e);
                    BootStrap.THANOS_X.getNotificationManagerService().onAddNotificationRecord(record);
                }).subscribeOn(Schedulers.trampoline()).subscribe();
            }
            return added;
        }

        @Override
        public T remove(int i) {
            T removed = super.remove(i);
            if (removed != null) {
                Completable.fromRunnable(() -> {
                    NotificationRecord record = NotificationRecordUtils.fromLegacy(removed);
                    BootStrap.THANOS_X.getNotificationManagerService().onRemoveNotificationRecord(record);
                }).subscribeOn(Schedulers.trampoline()).subscribe();
            }
            return removed;
        }

        @Override
        public boolean remove(Object object) {
            boolean removed = super.remove(object);
            if (removed) {
                Completable.fromRunnable(() -> {
                    NotificationRecord record = NotificationRecordUtils.fromLegacy(object);
                    BootStrap.THANOS_X.getNotificationManagerService().onRemoveNotificationRecord(record);
                }).subscribeOn(Schedulers.trampoline()).subscribe();
            }
            return removed;
        }
    }
}
