package github.tornaco.android.thanos.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.java.common.util.PinyinComparatorUtils;
import lombok.ToString;

@ToString
public class AppListModel extends ListModel<AppListModel> {
    @NonNull
    public AppInfo appInfo;
    // Extra badge.
    @Nullable
    public String badge;

    public AppListModel(@NonNull AppInfo appInfo, @Nullable String badge) {
        this.appInfo = appInfo;
        this.badge = badge;
    }

    public AppListModel(@NonNull AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    @Override
    public int compareTo(@NonNull AppListModel listModel) {
        if (this.appInfo.isSelected() != listModel.appInfo.isSelected()) {
            return this.appInfo.isSelected() ? -1 : 1;
        }
        return PinyinComparatorUtils.compare(this.appInfo.getAppLabel(), listModel.appInfo.getAppLabel());
    }
}
