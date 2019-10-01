package github.tornaco.android.thanos.services;

import android.content.Context;
import github.tornaco.java.common.util.CollectionUtils;
import io.reactivex.Completable;

import java.util.Collection;

public class SystemServiceLifecycle {

    public static void onStart(Context context, Collection<SystemService> services) {
        CollectionUtils.consumeRemaining(services,
                systemService -> Completable.fromAction(() ->
                        systemService.onStart(context))
                        .subscribeOn(ThanosSchedulers.serverThread())
                        .subscribe());
    }

    public static void systemReady(Collection<SystemService> services) {
        CollectionUtils.consumeRemaining(services,
                systemService -> Completable.fromAction(systemService::systemReady)
                        .subscribeOn(ThanosSchedulers.serverThread())
                        .subscribe());
    }

    public static void shutdown(Collection<SystemService> services) {
        CollectionUtils.consumeRemaining(services,
                systemService -> Completable.fromAction(systemService::shutdown)
                        .subscribeOn(ThanosSchedulers.serverThread())
                        .subscribe());
    }
}
