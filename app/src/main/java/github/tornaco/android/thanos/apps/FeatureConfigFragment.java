package github.tornaco.android.thanos.apps;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.util.OsUtils;
import lombok.AllArgsConstructor;

import java.util.Objects;

public class FeatureConfigFragment extends PreferenceFragmentCompat {

    private AppInfo appInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appInfo = Objects.requireNonNull(getArguments()).getParcelable("app");
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_feature_config, rootKey);
    }

    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        ThanosManager thanos = ThanosManager.from(getContext());
        if (!thanos.isServiceInstalled()) {
            getPreferenceScreen().setEnabled(false);
            return;
        }

        new StartRestrictPref(Objects.requireNonNull(getContext())).bind();
        new BgRestrictPref(getContext()).bind();
        new TaskCleanUp(getContext()).bind();
        new PrivacyCheat(getContext()).bind();
    }

    class StartRestrictPref extends FeaturePref {

        StartRestrictPref(Context context) {
            super(context.getString(R.string.key_app_feature_config_start_restrict));
        }

        @Override
        boolean current() {
            return !ThanosManager.from(getContext())
                    .getActivityManager()
                    .isPkgStartBlocking(appInfo.getPkgName());
        }

        @Override
        void setTo(boolean value) {
            ThanosManager.from(getContext())
                    .getActivityManager()
                    .setPkgStartBlockEnabled(appInfo.getPkgName(), !value);
        }
    }

    class BgRestrictPref extends FeaturePref {

        BgRestrictPref(Context context) {
            super(context.getString(R.string.key_app_feature_config_bg_restrict));
        }

        @Override
        boolean current() {
            return !ThanosManager.from(getContext())
                    .getActivityManager()
                    .isPkgBgRestricted(appInfo.getPkgName());
        }

        @Override
        void setTo(boolean value) {
            ThanosManager.from(getContext())
                    .getActivityManager()
                    .setPkgBgRestrictEnabled(appInfo.getPkgName(), !value);
        }
    }

    class PrivacyCheat extends FeaturePref {

        PrivacyCheat(Context context) {
            super(context.getString(R.string.key_app_feature_config_privacy_cheat));
        }

        @Override
        boolean current() {
            return ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .isPkgPrivacyDataCheat(appInfo.getPkgName());
        }

        @Override
        void setTo(boolean value) {
            ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .setPkgPrivacyDataCheat(appInfo.getPkgName(), value);
        }
    }

    class TaskCleanUp extends FeaturePref {

        TaskCleanUp(Context context) {
            super(context.getString(R.string.key_app_feature_config_task_clean_up));
        }

        @Override
        boolean current() {
            return ThanosManager.from(getContext())
                    .getActivityManager()
                    .isPkgCleanUpOnTaskRemovalEnabled(appInfo.getPkgName());
        }

        @Override
        void setTo(boolean value) {
            ThanosManager.from(getContext())
                    .getActivityManager()
                    .setPkgCleanUpOnTaskRemovalEnabled(appInfo.getPkgName(), value);
        }

        @Override
        boolean visible() {
            return !OsUtils.isMIUI();
        }
    }

    @AllArgsConstructor
    abstract class FeaturePref {
        private String key;

        abstract boolean current();

        abstract void setTo(boolean value);

        boolean visible() {
            return true;
        }

        void bind() {
            SwitchPreference preference = findPreference(key);
            Objects.requireNonNull(preference).setVisible(visible());
            Objects.requireNonNull(preference).setChecked(current());
            preference.setOnPreferenceChangeListener((preference1, newValue) -> {
                boolean checked = (boolean) newValue;
                setTo(checked);
                return true;
            });
        }
    }
}
