package github.tornaco.practice.honeycomb.locker.data.source;

import android.content.Context;

import java.util.Arrays;
import java.util.List;

import github.tornaco.android.common.Collections;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.util.DevNull;
import github.tornaco.android.thanos.core.util.Rxs;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import rx2.android.schedulers.AndroidSchedulers;

public class AppsRepo implements AppDataSource {

    @Override
    public void getApps(Context context, int flags, AppDataSource.AppsLoadCallback callback) {
        DevNull.accept(Single.create((SingleOnSubscribe<List<AppInfo>>) emitter -> {
            ThanosManager thanos = ThanosManager.from(context);
            emitter.onSuccess(Arrays.asList(thanos.getPkgManager().getInstalledPkgs(AppInfo.FLAGS_ALL)));
        })
                .map(appInfos -> {
                    ThanosManager thanos = ThanosManager.from(context);
                    ActivityStackSupervisor supervisor = thanos.getActivityStackSupervisor();
                    Collections.consumeRemaining(appInfos,
                            appInfo -> appInfo.setSelected(
                                    supervisor.isPackageLocked(appInfo.getPkgName())));
                    return appInfos;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onAppsLoaded, Rxs.ON_ERROR_LOGGING));
    }
}
