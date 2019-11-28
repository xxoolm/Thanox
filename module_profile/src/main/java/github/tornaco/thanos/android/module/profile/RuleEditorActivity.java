package github.tornaco.thanos.android.module.profile;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.profile.RuleAddCallback;
import github.tornaco.android.thanos.core.util.TextWatcherAdapter;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.thanos.android.module.profile.databinding.ModuleProfileWorkflowEditorBinding;

public class RuleEditorActivity extends ThemeActivity {

    private ModuleProfileWorkflowEditorBinding binding;

    public static void start(Context context) {
        ActivityUtils.startActivity(context, RuleEditorActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        binding.editText.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {

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
                float size = binding.editText.getTextSize();
                binding.editText.setTextSize(size * 1.2f);
                return true;
            }
            if (item.getItemId() == R.id.action_text_size_dec) {
                float size = binding.editText.getTextSize();
                binding.editText.setTextSize(size * 0.8f);
                return true;
            }
            return false;
        });
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
        return super.onHomeMenuSelected();
    }
}
