package github.tornaco.android.thanos.services.wm;

import android.content.ComponentName;

import java.util.concurrent.TimeUnit;

import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.S;
import github.tornaco.android.thanos.services.ThanosSchedulers;

class ViewClickWorker implements Runnable {

    private String text;
    private ComponentName targetComponent;
    private long interval;
    private int maxRetryTimes;
    private int retryTimes;
    private S service;

    ViewClickWorker(String text,
                    ComponentName targetComponent,
                    long interval,
                    int maxRetryTimes,
                    S service) {
        this.text = text;
        this.targetComponent = targetComponent;
        this.interval = interval;
        this.maxRetryTimes = maxRetryTimes;
        this.service = service;
    }

    @Override
    public void run() {
        retryTimes++;
        Timber.v("ViewClickWorker: %s-%s, retry @ %s", text, targetComponent, retryTimes);
        boolean handled = service
                .getWindowManagerService()
                .findAndClickViewByTextInternal(text, targetComponent);
        if (handled) {
            Timber.v("ViewClickWorker: %s-%s, handled!", text, targetComponent);
        }
        if (!handled && retryTimes < maxRetryTimes
                && targetComponent.equals(service.getActivityStackSupervisor().getCurrentFrontComponentName())) {
            Timber.v("ViewClickWorker: %s-%s, post again...", text, targetComponent);
            ThanosSchedulers.serverThread().scheduleDirect(this, interval, TimeUnit.MILLISECONDS);
        }
    }
}
