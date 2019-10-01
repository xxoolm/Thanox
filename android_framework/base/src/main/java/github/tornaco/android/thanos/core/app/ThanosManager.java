package github.tornaco.android.thanos.core.app;

import android.content.Context;
import android.content.IntentFilter;
import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor;
import github.tornaco.android.thanos.core.app.event.IEventSubscriber;
import github.tornaco.android.thanos.core.backup.BackupAgent;
import github.tornaco.android.thanos.core.n.NotificationManager;
import github.tornaco.android.thanos.core.os.ServiceManager;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.pref.PrefManager;
import github.tornaco.android.thanos.core.push.PushManager;
import github.tornaco.android.thanos.core.secure.PrivacyManager;
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.java.common.util.Consumer;
import lombok.Getter;
import lombok.SneakyThrows;

public class ThanosManager {
    private IThanos service;
    @Getter
    private Context context;

    public ThanosManager(Context context, IThanos service) {
        this.context = context;
        this.service = service;
        Timber.w("Init thanos manager for package: %s", context.getPackageName());
    }

    public boolean isServiceInstalled() {
        boolean firstCheck = service != null && service.asBinder() != null && service.asBinder().isBinderAlive();
        if (!firstCheck) {
            return false;
        }
        try {
            service.whoAreYou();
        } catch (Throwable e) {
            Timber.e("Ask for thanox server error", e);
            return false;
        }
        return true;
    }

    public void ifServiceInstalled(Consumer<ThanosManager> consumer) {
        if (isServiceInstalled()) {
            consumer.accept(this);
        }
    }

    @SneakyThrows
    public ServiceManager getServiceManager() {
        return new ServiceManager(service.getServiceManager());
    }

    @SneakyThrows
    public PrefManager getPrefManager() {
        return new PrefManager(service.getPrefManager());
    }

    @SneakyThrows
    public PackageManager getPkgManager() {
        return new PackageManager(service.getPkgManager());
    }

    @SneakyThrows
    public PrivacyManager getPrivacyManager() {
        return new PrivacyManager(service.getPrivacyManager());
    }

    @SneakyThrows
    public ActivityManager getActivityManager() {
        return new ActivityManager(service.getActivityManager());
    }

    @SneakyThrows
    public ActivityStackSupervisor getActivityStackSupervisor() {
        return new ActivityStackSupervisor(service.getActivityStackSupervisor());
    }

    @SneakyThrows
    public AppOpsManager getAppOpsManager() {
        return new AppOpsManager(context, service.getAppOpsService());
    }

    @SneakyThrows
    public PushManager getPushManager() {
        return new PushManager(service.getPushManager());
    }

    @SneakyThrows
    public NotificationManager getNotificationManager() {
        return new NotificationManager(service.getNotificationManager());
    }

    @SneakyThrows
    public BackupAgent getBackupAgent() {
        return new BackupAgent(service.getBackupAgent());
    }

    @SneakyThrows
    public void registerEventSubscriber(IntentFilter filter, IEventSubscriber subscriber) {
        service.registerEventSubscriber(filter, subscriber);
    }

    @SneakyThrows
    public void unRegisterEventSubscriber(IEventSubscriber subscriber) {
        service.unRegisterEventSubscriber(subscriber);
    }

    @SneakyThrows
    public String fingerPrint() {
        return service.fingerPrint();
    }

    @SneakyThrows
    public String getVersionName() {
        return service.getVersionName();
    }

    public static ThanosManager from(Context context) {
        return new ThanosManager(context, ThanosManagerNative.getDefault());
    }
}
