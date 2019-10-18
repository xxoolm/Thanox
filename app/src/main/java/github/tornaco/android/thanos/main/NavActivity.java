package github.tornaco.android.thanos.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.app.donate.DonateActivity;
import github.tornaco.android.thanos.databinding.ActivityNavBinding;
import github.tornaco.android.thanos.pref.AppPreference;
import github.tornaco.android.thanos.settings.SettingsActivity;
import github.tornaco.android.thanos.theme.ThemeActivity;

public class NavActivity extends ThemeActivity implements NavFragment.FragmentAttachListener {

    private ActivityNavBinding binding;
    private NavViewModel navViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavBinding.inflate(
                LayoutInflater.from(this), null, false);
        setContentView(binding.getRoot());
        setupView();
        setupPagers();
        setupViewModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFirstRun();
        navViewModel.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                SettingsActivity.start(this);
                return true;
        }
        return false;
    }

    private void initFirstRun() {
        if (AppPreference.isFirstRun(getApplication())) {
            showAppNoticeDialog();
        }
    }

    private void showAppNoticeDialog() {
        new AlertDialog.Builder(NavActivity.this)
                .setTitle(R.string.title_app_notice)
                .setMessage(R.string.message_app_notice)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> AppPreference.setFirstRun(getApplication(), false))
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> AppPreference.setFirstRun(getApplication(), true))
                .create()
                .show();
    }

    private void setupViewModel() {
        navViewModel = obtainViewModel(this);
        binding.setViewmodel(navViewModel);
        binding.setStateBadgeClickListener(view -> {
            if (navViewModel.getState().get() == State.RebootNeeded) {
                showRebootDialog();
            }
            if (navViewModel.getState().get() == State.InActive) {
                showActiveDialog();
            }
        });
        binding.setTryingBadgeClickListener(view -> DonateActivity.start(NavActivity.this));
        binding.executePendingBindings();
    }

    private void setupView() {
        setSupportActionBar(binding.toolbar);
    }

    private void setupPagers() {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs) {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setTitle(sectionsPagerAdapter.getPageTitle(position));
            }
        });
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        // Default title.
        setTitle(sectionsPagerAdapter.getPageTitle(0));
    }

    private void showRebootDialog() {
        new AlertDialog.Builder(NavActivity.this)
                .setTitle(R.string.reboot_needed)
                .setMessage(R.string.message_reboot_needed)
                .setPositiveButton(R.string.reboot_now, (dialogInterface, i) -> navViewModel.reboot())
                .setNegativeButton(R.string.reboot_later, (dialogInterface, i) -> {
                    // Noop.
                })
                .create()
                .show();
    }

    private void showActiveDialog() {
        new AlertDialog.Builder(NavActivity.this)
                .setTitle(R.string.status_not_active)
                .setMessage(R.string.message_active_needed)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    // Noop.
                })
                .create()
                .show();
    }

    public static NavViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(NavViewModel.class);
    }

    @Override
    public void onFragmentAttach(NavFragment fragment) {
    }
}
