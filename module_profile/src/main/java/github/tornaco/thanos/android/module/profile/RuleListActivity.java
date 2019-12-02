package github.tornaco.thanos.android.module.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.concurrent.atomic.AtomicInteger;

import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.profile.ProfileManager;
import github.tornaco.android.thanos.core.profile.RuleInfo;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.SwitchBar;
import github.tornaco.thanos.android.module.profile.databinding.ModuleProfileRuleListActivityBinding;

public class RuleListActivity extends ThemeActivity implements RuleItemClickListener {

    private ModuleProfileRuleListActivityBinding binding;
    private RuleListViewModel viewModel;

    public static void start(Context context) {
        ActivityUtils.startActivity(context, RuleListActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ModuleProfileRuleListActivityBinding.inflate(LayoutInflater.from(this));
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
        binding.replacements.setAdapter(new RuleListAdapter(this, (ruleInfo, checked) -> {
            ruleInfo.setEnabled(checked);
            if (checked) ThanosManager.from(getApplicationContext())
                    .getProfileManager()
                    .enableRule(ruleInfo.getName());
            else ThanosManager.from(getApplicationContext())
                    .getProfileManager()
                    .disableRule(ruleInfo.getName());
        }));
        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(getResources().getIntArray(
                github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));

        // Switch.
        onSetupSwitchBar(binding.switchBar);

        binding.fab.setOnClickListener((View v) -> {
            AtomicInteger format = new AtomicInteger(ProfileManager.RULE_FORMAT_JSON);
            new AlertDialog.Builder(thisActivity())
                    .setTitle(R.string.module_profile_rule_editor_select_format)
                    .setSingleChoiceItems(new String[]{
                            "JSON",
                            "YAML"
                    }, 0, (dialog, which) -> format.set(which == 0 ? ProfileManager.RULE_FORMAT_JSON
                            : ProfileManager.RULE_FORMAT_YAML))
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.ok, (dialog, which)
                            -> ThanosManager.from(getApplicationContext())
                            .ifServiceInstalled(
                                    thanosManager ->
                                            RuleEditorActivity.start(thisActivity(),
                                                    null,
                                                    format.get())))
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        });
    }

    private void onSetupSwitchBar(SwitchBar switchBar) {
        switchBar.setChecked(getSwitchBarCheckState());
        switchBar.addOnSwitchChangeListener(this::onSwitchBarCheckChanged);
    }

    private boolean getSwitchBarCheckState() {
        return ThanosManager.from(getApplicationContext()).isServiceInstalled()
                && ThanosManager.from(getApplicationContext()).getProfileManager()
                .isProfileEnabled();
    }

    private void onSwitchBarCheckChanged(Switch switchBar, boolean isChecked) {
        ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager -> thanosManager.getProfileManager()
                        .setProfileEnabled(isChecked));
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        viewModel.start();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @Override
    public void onItemClick(@NonNull RuleInfo ruleInfo) {
        RuleEditorActivity.start(thisActivity(), ruleInfo, ruleInfo.getFormat());
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.resume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.module_profile_rule_wiki, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_view_wiki == item.getItemId()) {
            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(BuildProp.THANOX_URL_DOCS_PROFILE));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent, ""));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static RuleListViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(RuleListViewModel.class);
    }

}
