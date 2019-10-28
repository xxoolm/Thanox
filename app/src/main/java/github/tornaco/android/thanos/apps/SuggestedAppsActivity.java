package github.tornaco.android.thanos.apps;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import github.tornaco.android.thanos.common.AppItemViewActionListener;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.databinding.ActivitySuggestAppsBinding;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;

public class SuggestedAppsActivity extends ThemeActivity {
    private SuggestedAppsViewModel viewModel;
    private ActivitySuggestAppsBinding binding;

    public static void start(Context context) {
        ActivityUtils.startActivity(context, SuggestedAppsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuggestAppsBinding.inflate(
                LayoutInflater.from(this), null, false);
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
        binding.apps.setLayoutManager(new GridLayoutManager(this, 4));
        binding.apps.setAdapter(new SuggestedAppsAdapter(new AppItemViewActionListener() {
            @Override
            public void onAppItemClick(AppInfo appInfo) {
                AppDetailsActivity.start(getApplicationContext(), appInfo);
            }

            @Override
            public void onAppItemSwitchStateChange(AppInfo appInfo, boolean checked) {

            }
        }));
        binding.textViewAllApps.setOnClickListener(view -> AppsManageActivity.start(SuggestedAppsActivity.this));
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        viewModel.start();

        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    public static SuggestedAppsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(SuggestedAppsViewModel.class);
    }
}
