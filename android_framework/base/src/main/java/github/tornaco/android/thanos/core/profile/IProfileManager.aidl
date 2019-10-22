package github.tornaco.android.thanos.core.profile;

interface IProfileManager {
    void setNewInstalledAppsConfig();

    void setAutoApplyForNewInstalledAppsEnabled(boolean enable);
    boolean isAutoApplyForNewInstalledAppsEnabled();
}