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

import com.vic797.syntaxhighlight.SyntaxListener;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.profile.ProfileManager;
import github.tornaco.android.thanos.core.profile.RuleAddCallback;
import github.tornaco.android.thanos.core.profile.RuleCheckCallback;
import github.tornaco.android.thanos.core.profile.RuleInfo;
import github.tornaco.android.thanos.core.util.TextWatcherAdapter;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.TypefaceHelper;
import github.tornaco.thanos.android.module.profile.databinding.ModuleProfileWorkflowEditorBinding;
import util.ObjectsUtils;

public class RuleEditorActivity extends ThemeActivity implements SyntaxListener {

    private ModuleProfileWorkflowEditorBinding binding;
    @Nullable
    private RuleInfo ruleInfo;
    private String originalContent = "";
    private int format;

    public static void start(Context context, RuleInfo ruleInfo, int format) {
        Bundle data = new Bundle();
        data.putParcelable("rule", ruleInfo);
        data.putInt("format", format);
        ActivityUtils.startActivity(context, RuleEditorActivity.class, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (resolveIntent()) {
            binding = ModuleProfileWorkflowEditorBinding.inflate(LayoutInflater.from(this));
            setContentView(binding.getRoot());
            initView();
        }
    }

    private boolean resolveIntent() {
        ruleInfo = getIntent().getParcelableExtra("rule");
        if (ruleInfo != null) {
            originalContent = ruleInfo.getRuleString();
            format = ruleInfo.getFormat();
        } else {
            format = getIntent().getIntExtra("format", ProfileManager.RULE_FORMAT_JSON);
        }
        return format == ProfileManager.RULE_FORMAT_YAML || format == ProfileManager.RULE_FORMAT_JSON;
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
                if (TextUtils.isEmpty(getCurrentEditingContent())) {
                    return false;
                }
                ThanosManager.from(getApplicationContext())
                        .getProfileManager()
                        .addRule(getCurrentEditingContent(),
                                new RuleAddCallback() {
                                    @Override
                                    protected void onRuleAddSuccess() {
                                        super.onRuleAddSuccess();
                                        Toast.makeText(getApplicationContext(),
                                                R.string.module_profile_editor_save_success,
                                                Toast.LENGTH_LONG)
                                                .show();
                                        // Disable rule since it has been changed.
                                        if (ruleInfo != null) {
                                            ThanosManager.from(getApplicationContext())
                                                    .getProfileManager()
                                                    .disableRule(ruleInfo.getName());
                                        }
                                        finish();
                                    }

                                    @Override
                                    protected void onRuleAddFail(int errorCode, String errorMessage) {
                                        super.onRuleAddFail(errorCode, errorMessage);
                                        new AlertDialog.Builder(thisActivity())
                                                .setTitle(R.string.module_profile_editor_save_check_error)
                                                .setMessage(errorMessage)
                                                .setCancelable(true)
                                                .setPositiveButton(android.R.string.ok, null)
                                                .show();
                                    }
                                },
                                format);
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

        // Format.
        if (format == ProfileManager.RULE_FORMAT_JSON) {
            binding.setFormat("JSON");
        }
        if (format == ProfileManager.RULE_FORMAT_YAML) {
            binding.setFormat("YAML");
        }

        binding.setRuleInfo(ruleInfo);
        binding.setPlaceholder(null);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();

        binding.editText.setTypeface(TypefaceHelper.googleSourceCodePro(thisActivity()));
        binding.lineLayout.setTypeface(TypefaceHelper.googleSourceCodePro(thisActivity()));
        binding.editText.setListener(this);
        binding.editText.addSyntax(getAssets(), "json.json");
        binding.lineLayout.attachEditText(binding.editText);
        binding.editText.startHighlight(true);
        binding.editText.updateVisibleRegion();
    }

    private String getCurrentEditingContent() {
        if (binding.editText.getText() == null) return "";
        return binding.editText.getText().toString().trim();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_delete).setVisible(ruleInfo != null);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.module_profile_rule_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_delete == item.getItemId()) {
            onRequestDelete();
        }
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
        if (ObjectsUtils.equals(getCurrentEditingContent(), originalContent)) {
            finish();
            return;
        }
        new AlertDialog.Builder(thisActivity())
                .setTitle(R.string.module_profile_editor_discard_dialog_title)
                .setMessage(R.string.module_profile_editor_discard_dialog_message)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> finish())
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void onRequestDelete() {
        new AlertDialog.Builder(thisActivity())
                .setTitle(R.string.module_profile_editor_delete_dialog_title)
                .setMessage(R.string.module_profile_editor_delete_dialog_message)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    if (ruleInfo != null) {
                        ThanosManager.from(getApplicationContext())
                                .getProfileManager()
                                .deleteRule(ruleInfo.getName());
                        finish();
                    }
                })
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
                                Timber.w("onInvalid: %s", errorMessage);
                                binding.ruleCheckIndicator.setImageResource(R.drawable.module_profile_ic_rule_invalid_red_fill);
                            }

                            @Override
                            protected void onValid() {
                                super.onValid();
                                Timber.w("onValid: %s", format);
                                binding.ruleCheckIndicator.setImageResource(R.drawable.module_profile_ic_rule_valid_green_fill);
                            }
                        },
                        format);
    }

    @Override
    public void onLineClick(Editable editable, String text, int line) {
        Timber.v("onLineClick");
    }

    @Override
    public void onHighlightStart(Editable editable) {
        Timber.v("onHighlightStart");
    }

    @Override
    public void onHighlightEnd(Editable editable) {
        Timber.v("onHighlightEnd");
    }

    @Override
    public void onError(Exception e) {
        Timber.e(e, "onError");
    }
}
