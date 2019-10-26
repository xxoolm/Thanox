package github.tornaco.android.thanos.power;

import android.content.Context;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.google.common.collect.Lists;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.common.OnAppItemSelectStateChangeListener;
import github.tornaco.android.thanos.core.app.ActivityManager;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.util.YesNoDontKnow;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.SwitchBar;
import github.tornaco.java.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class SmartStandbyActivity extends CommonFuncToggleAppListFilterActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, SmartStandbyActivity.class);
    }

    @NonNull
    @Override
    protected String getTitleString() {
        return getString(R.string.feature_title_smart_app_standby);
    }

    @Override
    protected void onSwitchBarCheckChanged(Switch switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);
        boolean isPlatformAppIdleEnabled = ThanosManager.from(getApplicationContext())
                .isServiceInstalled() && ThanosManager.from(getApplicationContext())
                .getActivityManager().isPlatformAppIdleEnabled() == YesNoDontKnow.YES.code;
    }

    @Override
    protected boolean getSwitchBarCheckState() {
        boolean isPlatformAppIdleEnabled = ThanosManager.from(getApplicationContext())
                .isServiceInstalled() && ThanosManager.from(getApplicationContext())
                .getActivityManager().isPlatformAppIdleEnabled() == YesNoDontKnow.YES.code;
        return isPlatformAppIdleEnabled;
    }

    @Override
    protected void onSetupSwitchBar(SwitchBar switchBar) {
        super.onSetupSwitchBar(switchBar);
        boolean isPlatformAppIdleEnabled = ThanosManager.from(getApplicationContext())
                .isServiceInstalled() && ThanosManager.from(getApplicationContext())
                .getActivityManager().isPlatformAppIdleEnabled() == YesNoDontKnow.YES.code;
        if (!isPlatformAppIdleEnabled) {
            showPlatformAppIdleEnableDialog();
        }
    }

    @NonNull
    @Override
    protected OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener() {
        return (appInfo, selected) -> ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager ->
                        thanosManager.getActivityManager().setPkgSmartStandByEnabled(appInfo.getPkgName(), selected));
    }

    @NonNull
    @Override
    protected CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return index -> {
            ThanosManager thanos = ThanosManager.from(getApplicationContext());
            if (!thanos.isServiceInstalled()) return Lists.newArrayListWithCapacity(0);

            String runningBadge = getApplicationContext().getString(R.string.badge_app_running);
            String idleBadge = getApplicationContext().getString(R.string.badge_app_idle);
            ActivityManager am = thanos.getActivityManager();
            List<AppInfo> installed = Lists.newArrayList(thanos.getPkgManager().getInstalledPkgs(index.flag));
            List<AppListModel> res = new ArrayList<>();
            CollectionUtils.consumeRemaining(installed, appInfo -> {
                appInfo.setSelected(am.isPkgSmartStandByEnabled(appInfo.getPkgName()));
                res.add(new AppListModel(appInfo,
                        thanos.getActivityManager().isPackageRunning(appInfo.getPkgName()) ? runningBadge : null,
                        thanos.getActivityManager().isPackageIdle(appInfo.getPkgName()) ? idleBadge : null));
            });
            return res;
        };
    }

    private void showPlatformAppIdleEnableDialog() {
        new AlertDialog.Builder(SmartStandbyActivity.this)
                .setTitle(R.string.dialog_title_smart_app_standby_need_enable_platform_app_idle)
                .setMessage(R.string.dialog_message_smart_app_standby_need_enable_platform_app_idle)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    // Noop.
                })
                .show();
    }
}
