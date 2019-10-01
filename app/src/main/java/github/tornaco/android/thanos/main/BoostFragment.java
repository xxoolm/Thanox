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
import github.tornaco.android.thanos.databinding.FragmentBoostBinding;
import github.tornaco.android.thanos.process.ProcessManageActivity;

public class BoostFragment extends NavFragment {
    private FragmentBoostBinding boostBinding;
    private NavViewModel navViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        boostBinding = FragmentBoostBinding.inflate(inflater, container, false);
        setupView();
        setupViewModel();
        return boostBinding.getRoot();
    }

    private void setupView() {
        boostBinding.features.setLayoutManager(new GridLayoutManager(getContext(), 1));
        boostBinding.features.setAdapter(new DashboardAdapter());
        boostBinding.statusCard.setOnClickListener(view -> ProcessManageActivity.start(getContext()));
    }

    @Override
    String getNavTitle() {
        return getString(R.string.nav_title_boost);
    }

    @SuppressWarnings("ConstantConditions")
    private void setupViewModel() {
        navViewModel = obtainViewModel(getActivity());
        boostBinding.setViewmodel(navViewModel);
        boostBinding.executePendingBindings();
    }

    private static NavViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(NavViewModel.class);
    }
}
