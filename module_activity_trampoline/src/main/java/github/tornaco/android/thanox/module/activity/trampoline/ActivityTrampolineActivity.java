package github.tornaco.android.thanox.module.activity.trampoline;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.component.ComponentReplacement;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.SwitchBar;
import github.tornaco.android.thanox.module.activity.trampoline.databinding.ModuleActivityTrampolineActivityBinding;

public class ActivityTrampolineActivity extends ThemeActivity implements ActivityTrampolineItemClickListener {
    private ModuleActivityTrampolineActivityBinding binding;
    private TrampolineViewModel viewModel;

    public static void start(Context context) {
        ActivityUtils.startActivity(context, ActivityTrampolineActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ModuleActivityTrampolineActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setupView();
        setupViewModel();
    }

    private void setupView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // List.
        binding.replacements.setLayoutManager(new LinearLayoutManager(this));
        binding.replacements.setAdapter(new ActivityTrampolineAdapter(this));
        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(getResources().getIntArray(github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));

        // Switch.
        onSetupSwitchBar(binding.switchBar);

        binding.fab.setOnClickListener(v -> {
            ThanosManager.from(getApplicationContext())
                    .ifServiceInstalled(thanosManager -> showAddReplacementDialog(null, null, false));
        });
    }

    private void onSetupSwitchBar(SwitchBar switchBar) {
        switchBar.setChecked(getSwitchBarCheckState());
        switchBar.addOnSwitchChangeListener(this::onSwitchBarCheckChanged);
    }

    private boolean getSwitchBarCheckState() {
        return ThanosManager.from(getApplicationContext()).isServiceInstalled()
                && ThanosManager.from(getApplicationContext()).getActivityStackSupervisor()
                .isActivityTrampolineEnabled();
    }

    private void onSwitchBarCheckChanged(Switch switchBar, boolean isChecked) {
        ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager -> thanosManager.getActivityStackSupervisor()
                        .setActivityTrampolineEnabled(isChecked));
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        viewModel.start();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    private void showAddReplacementDialog(String from, String to, boolean canDelete) {
        View layout = LayoutInflater.from(this).inflate(R.layout.module_activity_trampoline_comp_replace_editor, null, false);

        final AppCompatEditText fromEditText = layout.findViewById(R.id.from_comp);
        fromEditText.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        fromEditText.setText(from);

        final AppCompatEditText toEditText = layout.findViewById(R.id.to_comp);
        toEditText.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        toEditText.setText(to);

        AlertDialog d = new AlertDialog.Builder(ActivityTrampolineActivity.this)
                .setTitle(canDelete
                        ? R.string.module_activity_trampoline_edit_dialog_title
                        : R.string.module_activity_trampoline_add_dialog_title)
                .setView(layout)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, (dialog, which) ->
                        onRequestAddNewReplacement(
                                fromEditText.getEditableText().toString(),
                                toEditText.getEditableText().toString()))
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        if (canDelete) {
            d.setButton(DialogInterface.BUTTON_NEUTRAL,
                    getString(R.string.module_activity_trampoline_add_dialog_delete),
                    (dialog, which) -> onRequestDeleteNewReplacement(from, to));
        }
        d.show();
    }

    private void onRequestDeleteNewReplacement(String f, String t) {
        if (TextUtils.isEmpty(f) || TextUtils.isEmpty(t) || TextUtils.isEmpty(f.trim()) || TextUtils.isEmpty(t.trim())) {
            showComponentEmptyTips();
            return;
        }
        ComponentName fromCompName = ComponentName.unflattenFromString(f);
        if (fromCompName == null) {
            showComponentFromInvalidTips();
            return;
        }
        ComponentName toCompName = ComponentName.unflattenFromString(t);
        if (toCompName == null) {
            showComponentToInvalidTips();
            return;
        }
        viewModel.onRequestRemoveNewReplacement(fromCompName, toCompName);
    }

    private void onRequestAddNewReplacement(String f, String t) {
        if (TextUtils.isEmpty(f) || TextUtils.isEmpty(t) || TextUtils.isEmpty(f.trim()) || TextUtils.isEmpty(t.trim())) {
            showComponentEmptyTips();
            return;
        }
        ComponentName fromCompName = ComponentName.unflattenFromString(f);
        if (fromCompName == null) {
            showComponentFromInvalidTips();
            return;
        }
        ComponentName toCompName = ComponentName.unflattenFromString(t);
        if (toCompName == null) {
            showComponentToInvalidTips();
            return;
        }
        viewModel.onRequestAddNewReplacement(fromCompName, toCompName);
    }

    private void showComponentFromInvalidTips() {
        Toast.makeText(
                ActivityTrampolineActivity.this,
                R.string.module_activity_trampoline_add_invalid_from_component
                , Toast.LENGTH_LONG)
                .show();
    }

    private void showComponentToInvalidTips() {
        Toast.makeText(
                ActivityTrampolineActivity.this,
                R.string.module_activity_trampoline_add_invalid_to_component
                , Toast.LENGTH_LONG)
                .show();
    }

    private void showComponentEmptyTips() {
        Toast.makeText(
                ActivityTrampolineActivity.this,
                R.string.module_activity_trampoline_add_empty_component
                , Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onItemClick(@NonNull ComponentReplacement replacement) {
        showAddReplacementDialog(replacement.from.flattenToString(), replacement.to.flattenToString(), true);
    }

    public static TrampolineViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(TrampolineViewModel.class);
    }

    private class EmojiExcludeFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source,
                                   int start,
                                   int end,
                                   Spanned dest,
                                   int dstart,
                                   int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
        }
    }
}
