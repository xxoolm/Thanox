package github.tornaco.android.thanos.services;

import android.content.Context;

import java.util.Collection;

import io.reactivex.Completable;
import util.CollectionUtils;

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
