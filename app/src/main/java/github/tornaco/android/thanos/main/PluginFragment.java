package github.tornaco.android.thanos.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.dashboard.DashboardAdapter;
import github.tornaco.android.thanos.databinding.FragmentPluginBinding;

public class PluginFragment extends NavFragment {

    private FragmentPluginBinding pluginBinding;
    private NavViewModel navViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        pluginBinding = FragmentPluginBinding.inflate(inflater, container, false);
        setupView();
        setupViewModel();
        return pluginBinding.getRoot();
    }

    private void setupView() {
        pluginBinding.features.setLayoutManager(new GridLayoutManager(getContext(), 1));
        pluginBinding.features.setAdapter(new DashboardAdapter());
    }

    @Override
    String getNavTitle() {
        return getString(R.string.nav_title_plugin);
    }

    @SuppressWarnings("ConstantConditions")
    private void setupViewModel() {
        navViewModel = obtainViewModel(getActivity());
        pluginBinding.setViewmodel(navViewModel);
        pluginBinding.executePendingBindings();
    }

    private static NavViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(NavViewModel.class);
    }
}
