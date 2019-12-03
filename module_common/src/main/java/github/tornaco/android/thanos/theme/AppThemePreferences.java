package github.tornaco.android.thanos.theme;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Observable;

import github.tornaco.android.thanos.core.app.ThanosManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import util.Singleton;

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
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return Theme.Light;
        try {
            return Theme.valueOf(
                    thanos.getPrefManager().getString(PREF_KEY_APP_THEME,
                            Theme.Light.name()));
        } catch (Throwable e) {
            return Theme.Light;
        }
    }


    public void setTheme(@NonNull Context context, @NonNull Theme theme) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return;
        thanos.getPrefManager().putString(PREF_KEY_APP_THEME, theme.name());
        setChanged();
        notifyObservers();
    }

    public String getIconPack(@NonNull Context context, String defaultValue) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return defaultValue;
        try {
            return thanos.getPrefManager()
                    .getString(PREF_KEY_APP_ICON_PACK, defaultValue);
        } catch (Throwable e) {
            return defaultValue;
        }
    }

    public void setIconPack(@NonNull Context context, @NonNull String iconPkg) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return;
        thanos.getPrefManager().putString(PREF_KEY_APP_ICON_PACK, iconPkg);
    }
}
