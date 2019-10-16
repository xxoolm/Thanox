package github.tornaco.android.thanos.notification;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import com.google.common.collect.Lists;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.common.OnAppItemSelectStateChangeListener;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.java.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ScreenOnNotificationActivity extends CommonFuncToggleAppListFilterActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, ScreenOnNotificationActivity.class);
    }

    @NonNull
    @Override
    protected String getTitleString() {
        return getString(R.string.feature_title_light_on_notifiation);
    }

    @NonNull
    @Override
    protected OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener() {
        return (appInfo, selected) -> ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager -> thanosManager.getNotificationManager()
                        .setScreenOnNotificationEnabledForPkg(appInfo.getPkgName(), selected));
    }

    @NonNull
    @Override
    protected CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return index -> {
            ThanosManager thanos = ThanosManager.from(getApplicationContext());
            if (!thanos.isServiceInstalled()) return Lists.newArrayListWithCapacity(0);
            List<AppInfo> installed = Lists.newArrayList(thanos.getPkgManager().getInstalledPkgs(index.flag));
            List<AppListModel> res = new ArrayList<>();
            CollectionUtils.consumeRemaining(installed, appInfo -> {
                appInfo.setSelected(thanos.getNotificationManager().isScreenOnNotificationEnabledForPkg(appInfo.getPkgName()));
                res.add(new AppListModel(appInfo));
            });
            return res;
        };
    }

    @Override
    protected boolean getSwitchBarCheckState() {
        return ThanosManager.from(getApplicationContext())
                .isServiceInstalled() && ThanosManager.from(getApplicationContext())
                .getNotificationManager().isScreenOnNotificationEnabled();
    }

    @Override
    protected void onSwitchBarCheckChanged(SwitchCompat switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);
        ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager ->
                        thanosManager.getNotificationManager().setScreenOnNotificationEnabled(isChecked));
    }
}
