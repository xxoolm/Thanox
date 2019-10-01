package github.tornaco.android.thanos.theme;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import github.tornaco.android.thanos.BaseDefaultMenuItemHandlingAppCompatActivity;
import github.tornaco.android.thanos.core.util.Timber;

import java.util.Observer;

@SuppressLint("Registered")
public class ThemeActivity extends BaseDefaultMenuItemHandlingAppCompatActivity {

    private final Observer themeObserver = (observable, o) -> onThemeChanged();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppThemePreferences.getInstance().addObserver(themeObserver);
        Theme theme = AppThemePreferences.getInstance().getTheme(this);
        setTheme(getThemeRes(theme));
    }

    @StyleRes
    protected int getThemeRes(Theme theme) {
        return theme.themeRes;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppThemePreferences.getInstance().deleteObserver(themeObserver);
    }

    protected void onThemeChanged() {
        Timber.v("onThemeChanged.");
        finish();
    }
}
