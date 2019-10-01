package github.tornaco.practice.honeycomb.locker.ui.start;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import com.google.common.collect.Lists;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.common.OnAppItemSelectStateChangeListener;
import github.tornaco.android.thanos.core.app.ActivityManager;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.java.common.util.CollectionUtils;
import github.tornaco.practice.honeycomb.locker.R;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends CommonFuncToggleAppListFilterActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, StartActivity.class);
    }

    @NonNull
    @Override
    protected String getTitleString() {
        return getString(R.string.module_locker_app_name);
    }

    @NonNull
    @Override
    protected OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener() {
        return (appInfo, selected) -> {
            ThanosManager thanosManager = ThanosManager.from(getApplicationContext());
            if (thanosManager.isServiceInstalled()) {
                thanosManager.getActivityStackSupervisor()
                        .setPackageLocked(appInfo.getPkgName(), selected);
            }
        };
    }

    @NonNull
    @Override
    protected CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return index -> {
            ThanosManager thanos = ThanosManager.from(getApplicationContext());
            if (!thanos.isServiceInstalled()) return Lists.newArrayListWithCapacity(0);

            ActivityManager am = thanos.getActivityManager();
            List<AppInfo> installed = Lists.newArrayList(thanos.getPkgManager().getInstalledPkgs(index.flag));
            List<AppListModel> res = new ArrayList<>();
            CollectionUtils.consumeRemaining(installed, appInfo -> {
                appInfo.setSelected(!am.isPkgStartBlocking(appInfo.getPkgName()));
                res.add(new AppListModel(appInfo));
            });
            return res;
        };
    }

    @Override
    protected boolean getSwitchBarCheckState() {
        ThanosManager thanosManager = ThanosManager.from(getApplicationContext());
        return thanosManager.isServiceInstalled() && thanosManager.getActivityStackSupervisor().isAppLockEnabled();
    }

    @Override
    protected void onSwitchBarCheckChanged(SwitchCompat switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);
        ThanosManager thanosManager = ThanosManager.from(getApplicationContext());
        if (thanosManager.isServiceInstalled()) {
            thanosManager.getActivityStackSupervisor().setAppLockEnabled(isChecked);
        }
    }
}
