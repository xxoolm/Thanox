package github.tornaco.thanos.android.module.profile;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.profile.ProfileManager;
import github.tornaco.android.thanos.core.profile.RuleAddCallback;
import github.tornaco.android.thanos.core.profile.RuleCheckCallback;
import github.tornaco.android.thanos.core.profile.RuleInfo;
import github.tornaco.android.thanos.core.util.TextWatcherAdapter;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.thanos.android.module.profile.databinding.ModuleProfileWorkflowEditorBinding;

public class RuleEditorActivity extends ThemeActivity {

    private ModuleProfileWorkflowEditorBinding binding;
    @Nullable
    private RuleInfo ruleInfo;

    public static void start(Context context, RuleInfo ruleInfo) {
        Bundle data = new Bundle();
        data.putParcelable("rule", ruleInfo);
        ActivityUtils.startActivity(context, RuleEditorActivity.class, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ruleInfo = getIntent().getParcelableExtra("rule");
        binding = ModuleProfileWorkflowEditorBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(ruleInfo == null ? R.string.module_profile_rule_new : R.string.module_profile_rule_edit);

        if (ruleInfo != null) {
            checkRuleAndUpdateTips(ruleInfo.getRuleString());
        }

        binding.editText.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String current = getCurrentEditingContent();
                if (!TextUtils.isEmpty(current)) {
                    checkRuleAndUpdateTips(current);
                }
            }
        });

        binding.editorActionsToolbar.inflateMenu(R.menu.module_profile_rule_actions);
        binding.editorActionsToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_save_apply) {
                ThanosManager.from(getApplicationContext())
                        .getProfileManager()
                        .addRule(getCurrentEditingContent(),
                                new RuleAddCallback() {
                                    @Override
                                    protected void onRuleAddSuccess() {
                                        super.onRuleAddSuccess();
                                        Toast.makeText(getApplicationContext(), "Add success.", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    protected void onRuleAddFail(int errorCode, String errorMessage) {
                                        super.onRuleAddFail(errorCode, errorMessage);
                                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                },
                                0);
                return true;
            }
            if (item.getItemId() == R.id.action_text_size_inc) {
                binding.editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, binding.editText.getTextSize() + 5f);
                return true;
            }
            if (item.getItemId() == R.id.action_text_size_dec) {
                binding.editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, binding.editText.getTextSize() - 5f);
                return true;
            }
            return false;
        });

        binding.setRuleInfo(ruleInfo);
        binding.setPlaceholder(getString(R.string.module_profile_rule_example));
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    private String getCurrentEditingContent() {
        if (binding.editText.getText() == null) return "";
        return binding.editText.getText().toString().trim();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean onHomeMenuSelected() {
        onRequestAbort();
        return true;
    }

    @Override
    public void onBackPressed() {
        onRequestAbort();
    }

    private void onRequestAbort() {
        if (TextUtils.isEmpty(getCurrentEditingContent())) {
            finish();
            return;
        }
        new AlertDialog.Builder(thisActivity())
                .setTitle(R.string.module_profile_rule_editor_discard_dialog_title)
                .setMessage(R.string.module_profile_rule_editor_discard_dialog_message)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> finish())
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void checkRuleAndUpdateTips(String rule) {
        ThanosManager.from(getApplicationContext())
                .getProfileManager()
                .checkRule(rule, new RuleCheckCallback() {
                            @Override
                            protected void onInvalid(int errorCode, String errorMessage) {
                                super.onInvalid(errorCode, errorMessage);
                                binding.ruleCheckIndicator.setImageResource(R.drawable.module_profile_ic_rule_invalid_red_fill);
                            }

                            @Override
                            protected void onValid() {
                                super.onValid();
                                binding.ruleCheckIndicator.setImageResource(R.drawable.module_profile_ic_rule_valid_green_fill);
                            }
                        },
                        ProfileManager.RULE_FORMAT_JSON);
    }
}
