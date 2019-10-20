package github.tornaco.android.thanos.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import github.tornaco.android.thanos.module.common.R;
import github.tornaco.android.thanos.module.common.databinding.ActivityCommonFuncToggleListFilterBinding;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.widget.SwitchBar;


public abstract class CommonFuncToggleAppListFilterActivity extends ThemeActivity {

    private CommonFuncToggleAppListFilterViewModel commonFuncToggleListFilterViewModel;
    private ActivityCommonFuncToggleListFilterBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommonFuncToggleListFilterBinding.inflate(
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

        //Creating the ArrayAdapter instance having the category list
        String[] category = getResources().getStringArray(R.array.common_app_categories);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.ghost_text_view, category);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.spinner.setAdapter(arrayAdapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                setTitle(String.format("%s - %s", getTitleString(), category[index]));
                commonFuncToggleListFilterViewModel.setAppCategoryFilter(index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // List.
        binding.apps.setLayoutManager(new LinearLayoutManager(this));
        binding.apps.setAdapter(new CommonFuncToggleAppListFilterAdapter(onCreateAppItemSelectStateChangeListener(), null));
        binding.swipe.setOnRefreshListener(() -> commonFuncToggleListFilterViewModel.start());
        binding.swipe.setColorSchemeColors(getResources().getIntArray(R.array.common_swipe_refresh_colors));

        // Switch.
        onSetupSwitchBar(binding.switchBar);

        // Search.
        binding.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                commonFuncToggleListFilterViewModel.setSearchText(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                commonFuncToggleListFilterViewModel.setSearchText(newText);
                return true;
            }
        });

        binding.searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                // Noop.
            }

            @Override
            public void onSearchViewClosed() {
                commonFuncToggleListFilterViewModel.clearSearchText();
            }
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

    @StringRes
    protected int getTitleRes() {
        return 0;
    }

    @NonNull
    protected abstract String getTitleString();

    @NonNull
    protected abstract OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener();

    private void setupViewModel() {
        commonFuncToggleListFilterViewModel = obtainViewModel(this);
        commonFuncToggleListFilterViewModel.setListModelLoader(onCreateListModelLoader());
        commonFuncToggleListFilterViewModel.setSelectStateChangeListener(onCreateAppItemSelectStateChangeListener());
        commonFuncToggleListFilterViewModel.start();

        binding.setViewModel(commonFuncToggleListFilterViewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @NonNull
    protected abstract CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home == item.getItemId()) finish();
        if (R.id.action_select_all == item.getItemId()) {
            commonFuncToggleListFilterViewModel.selectAll();
            return true;
        }
        if (R.id.action_un_select_all == item.getItemId()) {
            commonFuncToggleListFilterViewModel.unSelectAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        binding.toolbar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        onInflateOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_search);
        binding.searchView.setMenuItem(item);
        return true;
    }

    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_common_func_toggle_list_filter, menu);
    }

    @Override
    public void onBackPressed() {
        if (binding.searchView.isSearchOpen()) {
            binding.searchView.closeSearch();
            return;
        }
        super.onBackPressed();
    }

    public static CommonFuncToggleAppListFilterViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(CommonFuncToggleAppListFilterViewModel.class);
    }
}
