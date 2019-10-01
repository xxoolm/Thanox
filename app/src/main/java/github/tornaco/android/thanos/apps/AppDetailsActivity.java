package github.tornaco.android.thanos.apps;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.CategoryIndex;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.databinding.ActivityAppDetailsBinding;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;

import java.util.Objects;

public class AppDetailsActivity extends ThemeActivity {
    private ActivityAppDetailsBinding binding;
    private AppInfo appInfo;
    private AppDetailsViewModel viewModel;

    public static void start(Context context, AppInfo appInfo) {
        Bundle data = new Bundle();
        data.putParcelable("app", appInfo);
        ActivityUtils.startActivity(context, AppDetailsActivity.class, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!resolveIntent()) {
            finish();
            return;
        }
        binding = ActivityAppDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initView();
        initViewModel();

        if (savedInstanceState == null) {
            FeatureConfigFragment featureConfigFragment = new FeatureConfigFragment();
            Bundle data = new Bundle();
            data.putParcelable("app", appInfo);
            featureConfigFragment.setArguments(data);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, featureConfigFragment)
                    .commit();
        }
    }

    private boolean resolveIntent() {
        if (getIntent() == null) {
            return false;
        }
        appInfo = getIntent().getParcelableExtra("app");
        return appInfo != null;
    }

    private void initView() {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setTitle(null);
        binding.setApp(appInfo);

        CategoryIndex categoryIndex = CategoryIndex.fromFlags(appInfo.getFlags());
        binding.setCate(categoryIndex);
    }

    private void initViewModel() {
        viewModel = obtainViewModel(this);
        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    public static AppDetailsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(AppDetailsViewModel.class);
    }
}
