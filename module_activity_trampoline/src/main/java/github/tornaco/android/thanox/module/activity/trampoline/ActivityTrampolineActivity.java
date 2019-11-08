package github.tornaco.android.thanox.module.activity.trampoline;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
import github.tornaco.java.common.util.Consumer;

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
                    .ifServiceInstalled(new Consumer<ThanosManager>() {
                        @Override
                        public void accept(ThanosManager thanosManager) {
                            ComponentName from = ComponentName.unflattenFromString("com.xiaomi.xmsf/com.xiaomi.xmsf.push.service.HttpService");
                            ComponentName to = ComponentName.unflattenFromString("com.xiaomi.xmsf/com.xiaomi.xmsf.push.service.HttpService");
                            thanosManager.getActivityStackSupervisor().addComponentReplacement(new ComponentReplacement(from, to));
                        }
                    });
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

    public static TrampolineViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(TrampolineViewModel.class);
    }
}
