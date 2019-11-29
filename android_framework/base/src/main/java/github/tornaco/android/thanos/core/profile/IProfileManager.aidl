package github.tornaco.android.thanos.core.profile;

import github.tornaco.android.thanos.core.profile.IRuleAddCallback;
import github.tornaco.android.thanos.core.profile.IRuleCheckCallback;

interface IProfileManager {

    void setAutoApplyForNewInstalledAppsEnabled(boolean enable);
    boolean isAutoApplyForNewInstalledAppsEnabled();

    void addRule(String ruleJson, in IRuleAddCallback callback, int format);
    void deleteRule(String ruleId);

    boolean enableRule(String ruleId);
    boolean disableRule(String ruleId);
    boolean isRuleEnabled(String ruleId);

    void checkRule(String ruleJson, in IRuleCheckCallback callback, int format);

    Rule[] getAllRules();
    Rule[] getEnabledRules();
}