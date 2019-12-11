package github.tornaco.thanos.android.module.profile;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import com.google.common.collect.Lists;
import com.vic797.syntaxhighlight.SyntaxListener;

import java.util.List;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.profile.GlobalVar;
import github.tornaco.android.thanos.core.util.TextWatcherAdapter;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.TypefaceHelper;
import github.tornaco.thanos.android.module.profile.databinding.ModuleProfileGlobalVarEditorBinding;

public class GlobalVarEditorActivity extends ThemeActivity implements SyntaxListener {

    private ModuleProfileGlobalVarEditorBinding binding;
    private boolean hasAnyInput;
    @Nullable
    private GlobalVar globalVar = new GlobalVar("New", Lists.newArrayList());

    public static void start(Context context, GlobalVar globalVar) {
        Bundle data = new Bundle();
        data.putParcelable("var", globalVar);
        ActivityUtils.startActivity(context, GlobalVarEditorActivity.class, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (resolveIntent()) {
            binding = ModuleProfileGlobalVarEditorBinding.inflate(LayoutInflater.from(this));
            setContentView(binding.getRoot());
            initView();
        }
    }

    private boolean resolveIntent() {
        if (getIntent() != null && getIntent().hasExtra("var")) {
            GlobalVar extra = getIntent().getParcelableExtra("var");
            if (extra != null) {
                globalVar = extra;
            }
        }
        return true;
    }

    private void initView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(globalVar == null ? R.string.module_profile_rule_new : R.string.module_profile_rule_edit);

        if (globalVar != null) {
            checkRuleAndUpdateTips(globalVar.listToJson());
        }

        binding.editText.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String current = getCurrentEditingContent();
                if (!TextUtils.isEmpty(current)) {
                    checkRuleAndUpdateTips(current);
                }
                hasAnyInput = true;
            }
        });

        binding.editorActionsToolbar.inflateMenu(R.menu.module_profile_var_actions);
        binding.editorActionsToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_save_apply) {
                if (TextUtils.isEmpty(getCurrentEditingContent())) {
                    return false;
                }
                List<String> stringList = GlobalVar.listFromJson(getCurrentEditingContent());
                if (stringList == null) {
                    return false;
                }
                ThanosManager.from(getApplicationContext())
                        .ifServiceInstalled(thanosManager ->
                                thanosManager
                                        .getProfileManager()
                                        .addGlobalRuleVar(globalVar.getName(),
                                                stringList.toArray(new String[0])));
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

        binding.setVar(globalVar);
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
        menu.findItem(R.id.action_delete).setVisible(globalVar != null);
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
        if (!hasAnyInput) {
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

    private void onRequestDelete() {
        new AlertDialog.Builder(thisActivity())
                .setTitle(R.string.module_profile_rule_editor_delete_dialog_title)
                .setMessage(R.string.module_profile_rule_editor_delete_dialog_message)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    if (globalVar != null) {
                        ThanosManager.from(getApplicationContext())
                                .getProfileManager()
                                .removeGlobalRuleVar(globalVar.getName());
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void checkRuleAndUpdateTips(String rule) {
        if (GlobalVar.listFromJson(rule) != null) {
            binding.ruleCheckIndicator.setImageResource(R.drawable.module_profile_ic_rule_valid_green_fill);
        } else {
            binding.ruleCheckIndicator.setImageResource(R.drawable.module_profile_ic_rule_invalid_red_fill);
        }
    }

    @Override
    public void onLineClick(Editable editable, String text, int line) {
        // Noop.
    }

    @Override
    public void onHighlightStart(Editable editable) {
        // Noop.
    }

    @Override
    public void onHighlightEnd(Editable editable) {
        // Noop.
    }

    @Override
    public void onError(Exception e) {
        // Noop.
    }
}
