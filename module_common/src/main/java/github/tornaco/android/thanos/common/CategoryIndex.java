package github.tornaco.android.thanos.common;

import github.tornaco.android.thanos.core.pm.AppInfo;

public enum CategoryIndex {
    _3rd(AppInfo.FLAGS_USER),
    System(AppInfo.FLAGS_SYSTEM),
    Media(AppInfo.FLAGS_SYSTEM_MEDIA),
    Phone(AppInfo.FLAGS_SYSTEM_PHONE),
    WebView((AppInfo.FLAGS_WEB_VIEW_PROVIDER)),
    SystemUid((AppInfo.FLAGS_SYSTEM_UID)),
    All((AppInfo.FLAGS_ALL));

    public int flag;

    CategoryIndex(int flag) {
        this.flag = flag;
    }
}
