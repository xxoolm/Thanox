package github.tornaco.android.thanos.privacy;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Objects;

import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.util.Optional;
import lombok.AllArgsConstructor;

public class CheatFieldSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.cheat_field_pref, rootKey);
    }

    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        ThanosManager thanos = ThanosManager.from(getContext());
        if (!thanos.isServiceInstalled()) {
            getPreferenceScreen().setEnabled(false);
            return;
        }

        new Line1Num(getString(R.string.key_cheat_field_line1_num), false).bind();
        new SimSerial(getString(R.string.key_cheat_field_sim_serial), false).bind();
        new DeviceId(getString(R.string.key_cheat_field_device_id), false).bind();
        new AndroidId(getString(R.string.key_cheat_field_android_id), false).bind();

        new Line1Num(getString(R.string.key_original_field_line1_num), true).bind();
        new SimSerial(getString(R.string.key_original_field_sim_serial), true).bind();
        new DeviceId(getString(R.string.key_original_field_device_id), true).bind();
        new AndroidId(getString(R.string.key_original_field_android_id), true).bind();

        // Bind cheat imei.
        if (thanos.hasFeature(BuildProp.THANOX_FEATURE_PRIVACY_FIELD_IMEI)) {
            int phoneCount = thanos.getPrivacyManager().getPhoneCount();
            for (int i = 1; i < phoneCount; i++) {
                new Imei(getString(R.string.key_cheat_field_imei_slot_) + i, false, i).bind();
                new Imei(getString(R.string.key_original_field_imei_slot_) + i, true, i).bind();
            }
        }

        // Bind cheat meid.
        if (thanos.hasFeature(BuildProp.THANOX_FEATURE_PRIVACY_FIELD_MEID)) {
            int phoneCount = thanos.getPrivacyManager().getPhoneCount();
            for (int i = 1; i < phoneCount; i++) {
                new Meid(getString(R.string.key_cheat_field_meid_slot_) + i, false, i).bind();
                new Meid(getString(R.string.key_original_field_meid_slot_) + i, true, i).bind();
            }
        }


        SwitchPreferenceCompat showNotificationPref = findPreference(getString(R.string.key_cheat_field_show_cheated_app_notifications));
        Objects.requireNonNull(showNotificationPref).setChecked(thanos.getPrivacyManager().isPrivacyNotificationEnabled());
        showNotificationPref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (boolean) newValue;
            thanos.getPrivacyManager().setPrivacyNotificationEnabled(checked);
            return true;
        });
    }

    class Line1Num extends FieldPrefHandler {

        Line1Num(String key, boolean isOriginal) {
            super(key, isOriginal);
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            return ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .getCheatedLine1NumberForPkg("*");
        }

        @Override
        boolean updateValue(@NonNull String value) {
            ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .setCheatedLine1NumberForPkg("*", value.trim());
            return true;
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .getOriginalLine1Number();
        }
    }

    class SimSerial extends FieldPrefHandler {

        SimSerial(String key, boolean isOriginal) {
            super(key, isOriginal);
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            return ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .getCheatedSimSerialNumberForPkg("*");
        }

        @Override
        boolean updateValue(@NonNull String value) {
            ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .setCheatedSimSerialNumberForPkg("*", value.trim());
            return true;
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .getOriginalSimSerialNumber();
        }
    }

    class DeviceId extends FieldPrefHandler {

        DeviceId(String key, boolean isOriginal) {
            super(key, isOriginal);
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            return ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .getCheatedDeviceIdForPkg("*");
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .getOriginalDeviceId();
        }

        @Override
        boolean updateValue(@NonNull String value) {
            ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .setCheatedDeviceIdForPkg("*", value.trim());
            return true;
        }
    }


    class AndroidId extends FieldPrefHandler {

        AndroidId(String key, boolean isOriginal) {
            super(key, isOriginal);
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            return ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .getCheatedAndroidIdForPkg("*");
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .getOriginalAndroidId();
        }

        @Override
        boolean updateValue(@NonNull String value) {
            ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .setCheatedAndroidIdForPkg("*", value.trim());
            return true;
        }
    }

    class Imei extends FieldPrefHandler {
        private final int slotIndex;

        Imei(String key, boolean isOriginal, int slotIndex) {
            super(key, isOriginal);
            this.slotIndex = slotIndex;
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            return ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .getCheatedImeiForPkg("*", slotIndex);
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .getOriginalImei(slotIndex);
        }

        @Override
        boolean updateValue(@NonNull String value) {
            ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .setCheatedImeiForPkg("*", value.trim(), slotIndex);
            return true;
        }

        @Override
        void onBind(EditTextPreference editTextPreference) {
            editTextPreference.setVisible(true);
            super.onBind(editTextPreference);
        }
    }

    class Meid extends FieldPrefHandler {
        private final int slotIndex;

        Meid(String key, boolean isOriginal, int slotIndex) {
            super(key, isOriginal);
            this.slotIndex = slotIndex;
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            return ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .getCheatedMeidForPkg("*", slotIndex);
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .getOriginalMeid(slotIndex);
        }

        @Override
        boolean updateValue(@NonNull String value) {
            ThanosManager.from(getContext())
                    .getPrivacyManager()
                    .setCheatedMeidForPkg("*", value.trim(), slotIndex);
            return true;
        }

        @Override
        void onBind(EditTextPreference editTextPreference) {
            editTextPreference.setVisible(true);
            super.onBind(editTextPreference);
        }
    }

    @AllArgsConstructor
    abstract class FieldPrefHandler {
        private String key;
        private boolean isOriginal;

        void bind() {
            Optional.ofNullable((EditTextPreference) findPreference(key)).ifPresent(this::onBind);
        }

        void onBind(EditTextPreference editTextPreference) {
            String currentValue = getCurrentCheatValue();
            editTextPreference.setSummary(
                    isOriginal ? getOriginalValue()
                            : (TextUtils.isEmpty(currentValue) ? getString(R.string.pre_title_cheat_not_set) : currentValue));
            if (!isOriginal) {
                editTextPreference.setOnBindEditTextListener(editText -> editText.setHint(getCurrentCheatValue()));
                editTextPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    boolean res = updateValue(String.valueOf(newValue.toString()));
                    if (res) {
                        editTextPreference.setSummary(getCurrentCheatValue());
                    }
                    return res;
                });
            } else {
                editTextPreference.setEnabled(false);
            }
        }

        @Nullable
        abstract String getCurrentCheatValue();

        @Nullable
        abstract String getOriginalValue();

        abstract boolean updateValue(@NonNull String value);
    }
}
