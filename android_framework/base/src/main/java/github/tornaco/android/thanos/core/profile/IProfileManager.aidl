package github.tornaco.android.thanos.core.profile;

import github.tornaco.android.thanos.core.profile.IRuleAddCallback;

interface IProfileManager {
    void setAutoApplyForNewInstalledAppsEnabled(boolean enable);
    boolean isAutoApplyForNewInstalledAppsEnabled();

    void addRule(String ruleJson, in IRuleAddCallback callback);
    void deleteRule(String ruleId);

    boolean setRuleEnabled(String ruleId, boolean enable);
    boolean isRuleEnabled(String ruleId);
}