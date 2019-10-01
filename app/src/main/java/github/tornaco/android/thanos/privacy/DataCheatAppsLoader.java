package github.tornaco.android.thanos.privacy;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.common.collect.Lists;
import github.tornaco.android.thanos.common.CategoryIndex;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.secure.PrivacyManager;
import github.tornaco.java.common.util.CollectionUtils;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class DataCheatAppsLoader implements CommonFuncToggleAppListFilterViewModel.ListModelLoader {
    @NonNull
    private final Context context;

    @Override
    public List<AppListModel> load(@NonNull CategoryIndex index) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return Lists.newArrayListWithCapacity(0);
        PrivacyManager priv = thanos.getPrivacyManager();
        List<AppInfo> installed = Lists.newArrayList(thanos.getPkgManager().getInstalledPkgs(index.flag));
        List<AppListModel> res = new ArrayList<>();
        CollectionUtils.consumeRemaining(installed, appInfo -> {
            appInfo.setSelected(priv.isPkgPrivacyDataCheat(appInfo.getPkgName()));
            res.add(new AppListModel(appInfo));
        });
        return res;
    }
}
