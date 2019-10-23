package github.tornaco.android.thanos.core.profile;

interface IProfileManager {
    void setAutoApplyForNewInstalledAppsEnabled(boolean enable);
    boolean isAutoApplyForNewInstalledAppsEnabled();
}