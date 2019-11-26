package github.tornaco.android.thanos.core.profile;

interface IProfileManager {
    void setAutoApplyForNewInstalledAppsEnabled(boolean enable);
    boolean isAutoApplyForNewInstalledAppsEnabled();

    boolean addRule(String ruleString, String ruleId);
    void deleteRule(String ruleId);

    void setRuleEnabled(String ruleId, boolean enable);
    void isRuleEnabled(String ruleId);
}