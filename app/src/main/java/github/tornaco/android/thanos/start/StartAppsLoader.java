package github.tornaco.android.thanos.start;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.common.collect.Lists;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CategoryIndex;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.core.app.ActivityManager;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.java.common.util.CollectionUtils;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class StartAppsLoader implements CommonFuncToggleAppListFilterViewModel.ListModelLoader {
    @NonNull
    private final Context context;

    @Override
    public List<AppListModel> load(@NonNull CategoryIndex index) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return Lists.newArrayListWithCapacity(0);

        String runningBadge = context.getString(R.string.badge_app_running);
        ActivityManager am = thanos.getActivityManager();
        List<AppInfo> installed = Lists.newArrayList(thanos.getPkgManager().getInstalledPkgs(index.flag));
        List<AppListModel> res = new ArrayList<>();
        CollectionUtils.consumeRemaining(installed, appInfo -> {
            appInfo.setSelected(!am.isPkgStartBlocking(appInfo.getPkgName()));
            res.add(new AppListModel(appInfo, thanos.getActivityManager().isPackageRunning(appInfo.getPkgName()) ? runningBadge : null));
        });
        return res;
    }
}
