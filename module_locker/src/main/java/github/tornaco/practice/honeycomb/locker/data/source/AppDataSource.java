package github.tornaco.practice.honeycomb.locker.data.source;

import android.content.Context;

import java.util.List;

import github.tornaco.android.thanos.core.pm.AppInfo;

public interface AppDataSource {

    interface AppsLoadCallback {
        void onAppsLoaded(List<AppInfo> appInfoList);

        void onDataNotAvailable();
    }

    void getApps(Context context, int flags, AppsLoadCallback callback);
}
