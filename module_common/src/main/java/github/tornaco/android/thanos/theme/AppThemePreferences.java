package github.tornaco.android.thanos.theme;

import android.content.Context;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import github.tornaco.java.common.util.Singleton;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Observable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppThemePreferences extends Observable {

    private static final String PREF_KEY_APP_THEME = "PREF_KEY_APP_THEME";
    private static final String PREF_KEY_APP_ICON_PACK = "PREF_KEY_APP_ICON_PACK";

    private static Singleton<AppThemePreferences> sPref = new Singleton<AppThemePreferences>() {
        @Override
        protected AppThemePreferences create() {
            return new AppThemePreferences();
        }
    };

    public static AppThemePreferences getInstance() {
        return sPref.get();
    }

    public Theme getTheme(@NonNull Context context) {
        return Theme.valueOf(PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_KEY_APP_THEME, Theme.Light.name()));
    }

    public void setTheme(@NonNull Context context, @NonNull Theme theme) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_KEY_APP_THEME, theme.name())
                .apply();
        setChanged();
        notifyObservers();
    }

    public String getIconPack(@NonNull Context context, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_KEY_APP_ICON_PACK, defaultValue);
    }

    public void setIconPack(@NonNull Context context, @NonNull String iconPkg) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_KEY_APP_ICON_PACK, iconPkg)
                .apply();
    }
}
