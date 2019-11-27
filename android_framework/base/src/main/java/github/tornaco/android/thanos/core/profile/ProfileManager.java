package github.tornaco.android.thanos.core.profile;

import lombok.SneakyThrows;

public class ProfileManager {
    /**
     * Dummy package name for new installed apps config.
     */
    public static final String PROFILE_AUTO_APPLY_NEW_INSTALLED_APPS_CONFIG_PKG_NAME
            = "github.tornaco.android.thanos.core.profile.AAC9A203-A42F-4AD6-976E-815D929D444A";

    private final IProfileManager server;

    public ProfileManager(IProfileManager server) {
        this.server = server;
    }

    @SneakyThrows
    public void setAutoApplyForNewInstalledAppsEnabled(boolean enable) {
        server.setAutoApplyForNewInstalledAppsEnabled(enable);
    }

    @SneakyThrows
    public boolean isAutoApplyForNewInstalledAppsEnabled() {
        return server.isAutoApplyForNewInstalledAppsEnabled();
    }

    @SneakyThrows
    public void addRule(String id, String ruleString, RuleAddCallback callback) {
        server.addRule(id, ruleString, callback.getStub());
    }

    @SneakyThrows
    public void deleteRule(String ruleId) {
        server.deleteRule(ruleId);
    }

    @SneakyThrows
    public void setRuleEnabled(String ruleId, boolean enable) {
        server.setRuleEnabled(ruleId, enable);
    }

    @SneakyThrows
    public void isRuleEnabled(String ruleId) {
        server.isRuleEnabled(ruleId);
    }
}
