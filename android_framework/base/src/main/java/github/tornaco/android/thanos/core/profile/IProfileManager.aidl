package github.tornaco.android.thanos.core.profile;

import github.tornaco.android.thanos.core.profile.IRuleAddCallback;

interface IProfileManager {
    void setAutoApplyForNewInstalledAppsEnabled(boolean enable);
    boolean isAutoApplyForNewInstalledAppsEnabled();

    void addRule(String id, String ruleJson, in IRuleAddCallback callback);
    void deleteRule(String ruleId);

    void setRuleEnabled(String ruleId, boolean enable);
    void isRuleEnabled(String ruleId);
}