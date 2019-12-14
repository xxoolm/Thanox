package github.tornaco.android.thanos.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import github.tornaco.android.thanos.core.pm.AppInfo;
import lombok.ToString;
import util.PinyinComparatorUtils;

@ToString
public class AppListModel extends ListModel<AppListModel> {
    @NonNull
    public AppInfo appInfo;
    // Extra badge.
    @Nullable
    public String badge;
    @Nullable
    public String badge2;

    public AppListModel(@NonNull AppInfo appInfo, @Nullable String badge, @Nullable String badge2) {
        this.appInfo = appInfo;
        this.badge = badge;
        this.badge2 = badge2;
    }

    public AppListModel(@NonNull AppInfo appInfo, @Nullable String badge) {
        this.appInfo = appInfo;
        this.badge = badge;
    }

    public AppListModel(@NonNull AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    @Override
    public int compareTo(@NonNull AppListModel listModel) {
        if (this.appInfo.disabled() != listModel.appInfo.disabled()) {
            return this.appInfo.disabled() ? 1 : -1;
        }
        if (this.appInfo.isSelected() != listModel.appInfo.isSelected()) {
            return this.appInfo.isSelected() ? -1 : 1;
        }
        return PinyinComparatorUtils.compare(this.appInfo.getAppLabel(), listModel.appInfo.getAppLabel());
    }
}
