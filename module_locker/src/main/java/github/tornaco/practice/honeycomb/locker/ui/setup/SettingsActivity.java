package github.tornaco.practice.honeycomb.locker.ui.setup;

import android.os.Bundle;
import androidx.annotation.Nullable;
import github.tornaco.android.thanos.BaseDefaultMenuItemHandlingAppCompatActivity;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.practice.honeycomb.locker.R;

public class SettingsActivity extends ThemeActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_locker_settings_activity);
        setSupportActionBar(findViewById(R.id.toolbar));
        showHomeAsUpNavigator();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance())
                    .commit();
        }
    }
}
