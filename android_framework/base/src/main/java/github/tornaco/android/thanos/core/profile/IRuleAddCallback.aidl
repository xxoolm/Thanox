package github.tornaco.android.thanos.core.profile;

interface IRuleAddCallback {
    void onRuleAddSuccess();
    void onRuleAddFail(int errorCode, String errorMessage);
}