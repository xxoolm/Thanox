package github.tornaco.thanos.android.module.profile;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import java.util.UUID;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.profile.RuleAddCallback;
import github.tornaco.android.thanos.core.util.TextWatcherAdapter;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;

/**
 * Created by Tornaco on 2018/6/15 16:24.
 * This file is writen for project X-APM at host guohao4.
 */
public class RuleEditorActivity extends ThemeActivity {

    private EditText mContentEditable, mTitleEditable;

    public static void start(Context context) {
        ActivityUtils.startActivity(context, RuleEditorActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_profile_workflow_editor);

        mContentEditable = findViewById(R.id.edit_text);
        mContentEditable.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mTitleEditable = findViewById(R.id.toolbar_title);
        mTitleEditable.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Toolbar editorActionToolbar = findViewById(R.id.editor_actions_toolbar);
        editorActionToolbar.inflateMenu(R.menu.module_profile_rule_actions);
        String id = UUID.randomUUID().toString();
        editorActionToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_save_apply) {
                ThanosManager.from(getApplicationContext())
                        .getProfileManager()
                        .addRule(id,
                                mContentEditable.getEditableText().toString(),
                                new RuleAddCallback() {
                                    @Override
                                    protected void onRuleAddSuccess() {
                                        super.onRuleAddSuccess();
                                        Toast.makeText(getApplicationContext(), "Add success.", Toast.LENGTH_LONG).show();

                                        ThanosManager.from(getApplicationContext())
                                                .getProfileManager()
                                                .setRuleEnabled(id, true);
                                    }

                                    @Override
                                    protected void onRuleAddFail(int errorCode, String errorMessage) {
                                        super.onRuleAddFail(errorCode, errorMessage);
                                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                });
                return true;
            }
            return false;
        });
    }

    @Override
    public void setTitle(int titleId) {
        // super.setTitle(titleId);
        setTitleInternal(getString(titleId));
    }

    @Override
    public void setTitle(CharSequence title) {
        // super.setTitle(title);
        setTitleInternal(title);
    }

    private void setTitleInternal(CharSequence title) {
        mTitleEditable.setText(title);
    }

    private String getCurrentEditingContent() {
        return String.valueOf(mContentEditable.getText().toString()).trim();
    }

    private String getCurrentEditingTitle() {
        return String.valueOf(mTitleEditable.getText().toString()).trim();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
