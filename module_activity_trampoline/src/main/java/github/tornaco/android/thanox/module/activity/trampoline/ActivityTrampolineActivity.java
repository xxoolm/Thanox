package github.tornaco.android.thanox.module.activity.trampoline;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.SwitchBar;
import github.tornaco.android.thanox.module.activity.trampoline.databinding.ModuleActivityTrampolineActivityBinding;

public class ActivityTrampolineActivity extends ThemeActivity {
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
        binding.replacements.setAdapter(new ActivityTrampolineAdapter());
        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(getResources().getIntArray(github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));

        // Switch.
        onSetupSwitchBar(binding.switchBar);

        binding.fab.setOnClickListener(v -> {
            ThanosManager.from(getApplicationContext())
                    .ifServiceInstalled(thanosManager -> showAddReplacementDialog(null, null));
        });
    }

    protected void onSetupSwitchBar(SwitchBar switchBar) {
        switchBar.setChecked(getSwitchBarCheckState());
        switchBar.addOnSwitchChangeListener(this::onSwitchBarCheckChanged);
    }

    protected boolean getSwitchBarCheckState() {
        return false;
    }

    protected void onSwitchBarCheckChanged(Switch switchBar, boolean isChecked) {
        // Noop.
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        viewModel.start();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    private void showAddReplacementDialog(String from, String to) {
        View layout = LayoutInflater.from(this).inflate(R.layout.module_activity_trampoline_comp_replace_editor, null, false);

        final AppCompatEditText fromEditText = layout.findViewById(R.id.from_comp);
        fromEditText.setText(from);

        final AppCompatEditText toEditText = layout.findViewById(R.id.to_comp);
        toEditText.setText(to);

        AlertDialog d = new AlertDialog.Builder(ActivityTrampolineActivity.this)
                .setTitle(R.string.module_activity_trampoline_add_dialog_title)
                .setView(layout)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, (dialog, which) ->
                        onRequestAddNewReplacement(
                                fromEditText.getEditableText().toString(),
                                toEditText.getEditableText().toString()))
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        d.show();
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

    public static TrampolineViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(TrampolineViewModel.class);
    }
}
