package github.tornaco.android.thanos.apps;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.common.collect.Lists;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppItemViewClickListener;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonAppListFilterViewModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.SwitchBar;
import github.tornaco.java.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class AppsManageActivity extends CommonAppListFilterActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, AppsManageActivity.class);
    }

    @Override
    protected int getTitleRes() {
        return R.string.activity_title_apps_manager;
    }

    @NonNull
    @Override
    protected AppItemViewClickListener onCreateAppItemViewClickListener() {
        return appInfo -> {

        };
    }

    @NonNull
    @Override
    protected CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return index -> {
            ThanosManager thanos = ThanosManager.from(getApplicationContext());
            if (!thanos.isServiceInstalled()) {
                return Lists.newArrayList(new AppListModel(AppInfo.dummy()));
            }
            List<AppInfo> installed = Lists.newArrayList(thanos.getPkgManager().getInstalledPkgs(index.flag));
            List<AppListModel> res = new ArrayList<>();
            CollectionUtils.consumeRemaining(installed, appInfo -> {
                res.add(new AppListModel(appInfo));
            });
            return res;
        };
    }

    @Override
    protected void onSetupSwitchBar(SwitchBar switchBar) {
        super.onSetupSwitchBar(switchBar);
        switchBar.hide();
    }
}
