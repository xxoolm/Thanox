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
import github.tornaco.android.thanos.databinding.FragmentExpBinding;

public class ExpFragment extends NavFragment {

    private FragmentExpBinding expBinding;
    private NavViewModel navViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        expBinding = FragmentExpBinding.inflate(inflater, container, false);
        setupView();
        setupViewModel();
        return expBinding.getRoot();
    }

    private void setupView() {
        expBinding.features.setLayoutManager(new GridLayoutManager(getContext(), 1));
        expBinding.features.setAdapter(new DashboardAdapter());
    }

    @Override
    String getNavTitle() {
        return getString(R.string.nav_title_exp);
    }

    @SuppressWarnings("ConstantConditions")
    private void setupViewModel() {
        navViewModel = obtainViewModel(getActivity());
        expBinding.setViewmodel(navViewModel);
        expBinding.executePendingBindings();
    }

    private static NavViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(NavViewModel.class);
    }
}
