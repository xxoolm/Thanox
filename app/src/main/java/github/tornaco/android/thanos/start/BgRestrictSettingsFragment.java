package github.tornaco.android.thanos.start;

import android.os.Bundle;
import androidx.preference.DropDownPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;

import java.util.Objects;

public class BgRestrictSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.bg_restrict_pref, rootKey);
    }

    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        ThanosManager thanos = ThanosManager.from(getContext());
        if (!thanos.isServiceInstalled()) {
            getPreferenceScreen().setEnabled(false);
            return;
        }
        DropDownPreference delayPref = findPreference(getString(R.string.key_bg_screen_off_clean_up_delay));
        long delay = thanos.getActivityManager().getBgTaskCleanUpDelayTimeMills();
        int min = (int) (delay / (60 * 1000));
        Objects.requireNonNull(delayPref).setValue(String.valueOf(min));
        delayPref.setOnPreferenceChangeListener((preference, newValue) -> {
            int newMin = Integer.parseInt(String.valueOf(newValue));
            thanos.getActivityManager().setBgTaskCleanUpDelayTimeMills(newMin * 1000 * 60);
            return true;
        });

        SwitchPreference skipAudio = findPreference(getString(R.string.key_bg_screen_off_clean_up_skip_audio));
        boolean enabledA = thanos.getActivityManager().isBgTaskCleanUpSkipAudioFocusedAppEnabled();
        Objects.requireNonNull(skipAudio).setChecked(enabledA);
        skipAudio.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            thanos.getActivityManager().setBgTaskCleanUpSkipAudioFocusedAppEnabled(checked);
            return true;
        });

        SwitchPreference skipN = findPreference(getString(R.string.key_bg_screen_off_clean_up_skip_notification));
        boolean enabledN = thanos.getActivityManager().isBgTaskCleanUpSkipWhichHasNotificationEnabled();
        Objects.requireNonNull(skipN).setChecked(enabledN);
        skipN.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            thanos.getActivityManager().setBgTaskCleanUpSkipWhichHasNotificationEnabled(checked);
            return true;
        });
    }
}
