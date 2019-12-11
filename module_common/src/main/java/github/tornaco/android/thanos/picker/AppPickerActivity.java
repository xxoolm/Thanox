package github.tornaco.android.thanos.picker;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import github.tornaco.android.thanos.common.AppItemViewClickListener;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonAppListFilterAdapter;
import github.tornaco.android.thanos.common.CommonAppListFilterViewModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.module.common.R;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.SwitchBar;
import util.CollectionUtils;

public class AppPickerActivity extends CommonAppListFilterActivity {

    private final Map<String, AppInfo> selectedAppInfoMap = Maps.newHashMap();
    private CommonAppListFilterAdapter appListFilterAdapter = null;

    private final AppItemViewClickListener appItemViewClickListener = appInfo -> {
        if (appInfo.isSelected()) {
            selectedAppInfoMap.put(appInfo.getPkgName(), appInfo);
        } else {
            selectedAppInfoMap.remove(appInfo.getPkgName());
        }
    };

    public static void start(Context context) {
        ActivityUtils.startActivity(context, AppPickerActivity.class);
    }

    @Override
    protected int getTitleRes() {
        return R.string.app_picker_title;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedAppInfoMap.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        selectedAppInfoMap.clear();
    }

    @Override
    protected void onSetupSwitchBar(SwitchBar switchBar) {
        super.onSetupSwitchBar(switchBar);
        switchBar.hide();
    }

    @NonNull
    @Override
    protected AppItemViewClickListener onCreateAppItemViewClickListener() {
        return appItemViewClickListener;
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
                appInfo.setSelected(selectedAppInfoMap.containsKey(appInfo.getPkgName()));
                res.add(new AppListModel(appInfo));
            });
            return res;
        };
    }

    @Override
    protected CommonAppListFilterAdapter onCreateCommonAppListFilterAdapter() {
        appListFilterAdapter = new CommonAppListFilterAdapter(onCreateAppItemViewClickListener(), true);
        return appListFilterAdapter;
    }

    @Override
    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app_picker, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_select_all == item.getItemId()) {
            toogleListSelection(true);
            return true;
        }
        if (R.id.action_un_select_all == item.getItemId()) {
            toogleListSelection(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toogleListSelection(boolean selected) {
        CollectionUtils.consumeRemaining(appListFilterAdapter.getListModels()
                , appListModel -> {
                    appListModel.appInfo.setSelected(selected);
                    appItemViewClickListener.onAppItemClick(appListModel.appInfo);
                });
        appListFilterAdapter.notifyDataSetChanged();
    }
}
