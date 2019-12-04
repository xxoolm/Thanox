package github.tornaco.thanos.android.ops.ops.by.ops;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.util.Objects;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.SwitchBar;
import github.tornaco.android.thanos.widget.section.StickyHeaderLayoutManager;
import github.tornaco.thanos.android.ops.databinding.ModuleOpsLayoutAllOpsBinding;

public class AllOpsListActivity extends ThemeActivity {

    private ModuleOpsLayoutAllOpsBinding binding;
    private AllOpsListViewModel viewModel;

    public static void start(@NonNull Context context) {
        ActivityUtils.startActivity(context, AllOpsListActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ModuleOpsLayoutAllOpsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setupView();
        setupViewModel();
    }

    private void setupView() {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        StickyHeaderLayoutManager stickyHeaderLayoutManager = new StickyHeaderLayoutManager();
        // set a header position callback to set elevation on sticky headers, because why not
        stickyHeaderLayoutManager.setHeaderPositionChangedCallback((sectionIndex, header, oldPosition, newPosition) -> {
            boolean elevated = newPosition == StickyHeaderLayoutManager.HeaderPosition.STICKY;
            header.setElevation(elevated ? 8 : 0);
        });
        binding.apps.setLayoutManager(stickyHeaderLayoutManager);
        binding.apps.setAdapter(new AllOpsListAdapter(op -> OpsAppListActivity.start(getApplicationContext(), op)));

        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(
                getResources().getIntArray(github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));

        // Switch.
        onSetupSwitchBar(binding.switchBar);
    }

    protected void onSetupSwitchBar(SwitchBar switchBar) {
        switchBar.setChecked(getSwitchBarCheckState());
        switchBar.addOnSwitchChangeListener(this::onSwitchBarCheckChanged);
    }

    protected boolean getSwitchBarCheckState() {
        return ThanosManager.from(getApplicationContext())
                .isServiceInstalled()
                && ThanosManager.from(getApplicationContext())
                .getAppOpsManager().isOpsEnabled();
    }

    protected void onSwitchBarCheckChanged(Switch switchBar, boolean isChecked) {
        ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager -> thanosManager.getAppOpsManager()
                        .setOpsEnabled(isChecked));
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.start();
    }

    public static AllOpsListViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(AllOpsListViewModel.class);
    }
}
