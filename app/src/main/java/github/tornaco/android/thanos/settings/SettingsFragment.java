package github.tornaco.android.thanos.settings;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;
import github.tornaco.android.thanos.BuildConfig;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.ThanosApp;
import github.tornaco.android.thanos.app.donate.DonateActivity;
import github.tornaco.android.thanos.app.donate.DonateSettings;
import github.tornaco.android.thanos.apps.AppDetailsActivity;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.profile.ProfileManager;
import github.tornaco.android.thanos.core.util.ObjectToStringUtils;
import github.tornaco.android.thanos.core.util.Optional;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.module.easteregg.paint.PlatLogoActivity;
import github.tornaco.android.thanos.theme.AppThemePreferences;
import github.tornaco.android.thanos.theme.Theme;
import github.tornaco.android.thanos.util.iconpack.IconPack;
import github.tornaco.android.thanos.util.iconpack.IconPackManager;
import github.tornaco.permission.requester.RequiresPermission;
import github.tornaco.permission.requester.RuntimePermissions;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import rx2.android.schedulers.AndroidSchedulers;

@RuntimePermissions
public class SettingsFragment extends PreferenceFragmentCompat {
    private final static int REQUEST_CODE_BACKUP_FILE_PICK = 0x100;
    private final static int REQUEST_CODE_RESTORE_FILE_PICK = 0x200;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_pref, rootKey);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        ThanosManager thanos = ThanosManager.from(getContext());

        // Theme.
        Theme theme = AppThemePreferences.getInstance().getTheme(Objects.requireNonNull(this.getContext()));
        DropDownPreference themePref = findPreference(getString(R.string.key_app_theme));
        Objects.requireNonNull(themePref).setValue(theme.name());
        themePref.setOnPreferenceChangeListener((preference, newValue) -> {
            Theme selectedTheme = Theme.valueOf(String.valueOf(newValue));
            AppThemePreferences.getInstance().setTheme(Objects.requireNonNull(getContext()), selectedTheme);
            return true;
        });

        DropDownPreference iconPref = findPreference(getString(R.string.key_app_icon_pack));
        IconPackManager iconPackManager = IconPackManager.getInstance();
        final List<IconPack> packs = iconPackManager.getAvailableIconPacks(getContext());
        final List<String> entries = new ArrayList<>();
        final List<String> values = new ArrayList<>();
        for (IconPack pack : packs) {
            entries.add(String.valueOf(pack.label));
            values.add(String.valueOf(pack.packageName));
        }
        // Default
        entries.add("Noop");
        values.add("Noop");

        iconPref.setEntries(entries.toArray(new String[0]));
        iconPref.setEntryValues(values.toArray(new String[0]));
        String current = AppThemePreferences.getInstance().getIconPack(getContext(), null);

        iconPref.setSummary("Noop");
        iconPref.setValue("Noop");
        if (current != null) {
            IconPack pack = IconPackManager.getInstance().getIconPackage(getContext(), current);
            if (pack != null) {
                iconPref.setSummary(pack.label);
            }
        }
        iconPref.setOnPreferenceChangeListener((preference, newValue) -> {
            AppThemePreferences.getInstance().setIconPack(getContext(), String.valueOf(newValue));
            Completable.fromRunnable(() -> Glide.get(getContext()).clearDiskCache()).subscribeOn(Schedulers.io()).subscribe();
            IconPack pack = IconPackManager.getInstance().getIconPackage(getContext(), String.valueOf(newValue));
            if (pack != null) {
                preference.setSummary(pack.label);
            } else {
                preference.setSummary("Noop");
            }
            return true;
        });

        // Build.
        findPreference(getString(R.string.key_build_info_app))
                .setSummary(BuildConfig.VERSION_NAME + "\n" + BuildProp.FINGERPRINT + "\n" + BuildProp.BUILD_DATE.toString());
        findPreference(getString(R.string.key_build_info_app))
                .setOnPreferenceClickListener(preference -> {
                    PlatLogoActivity.start(getActivity());
                    Toast.makeText(getActivity(), "Thanox is build against Android 10", Toast.LENGTH_LONG).show();
                    return true;
                });
        if (thanos.isServiceInstalled()) {
            findPreference(getString(R.string.key_build_info_server))
                    .setSummary(thanos.getVersionName() + "\n" + thanos.fingerPrint());
        } else {
            findPreference(getString(R.string.key_build_info_server))
                    .setSummary(R.string.status_not_active);
        }

        // Logging.
        SwitchPreferenceCompat loggingPref = findPreference(getString(R.string.key_enable_logging));
        if (thanos.isServiceInstalled()) {
            loggingPref.setChecked(thanos.isLoggingEnabled());
            loggingPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = (boolean) newValue;
                thanos.setLoggingEnabled(checked);
                return true;
            });

        } else {
            loggingPref.setEnabled(false);
        }

        SwitchPreferenceCompat showCurrentActivityPref = findPreference(getString(R.string.key_show_current_activity));
        if (thanos.isServiceInstalled()) {
            showCurrentActivityPref.setChecked(thanos.getActivityStackSupervisor().isShowCurrentComponentViewEnabled());
            showCurrentActivityPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = (boolean) newValue;
                thanos.getActivityStackSupervisor().setShowCurrentComponentViewEnabled(checked);
                return true;
            });

        } else {
            showCurrentActivityPref.setEnabled(false);
        }

        // About.
        Preference donatePref = findPreference(getString(R.string.key_donate));
        donatePref.setOnPreferenceClickListener(preference -> {
            DonateActivity.start(getActivity());
            return true;
        });
        if (DonateSettings.isDonated(getContext())) {
            donatePref.setSummary(R.string.module_donate_donated);
        }

        // Backup
        findPreference(getString(R.string.key_data_backup)).setOnPreferenceClickListener(preference -> {
            SettingsFragmentPermissionRequester.backupRequestedChecked(SettingsFragment.this);
            return true;
        });
        findPreference(getString(R.string.key_data_restore)).setOnPreferenceClickListener(preference -> {
            SettingsFragmentPermissionRequester.restoreRequestedChecked(SettingsFragment.this);
            return true;
        });

        // Auto config.
        SwitchPreferenceCompat autoConfigPref = findPreference(getString(R.string.key_new_installed_apps_config_enabled));
        autoConfigPref.setChecked(thanos.isServiceInstalled() && thanos.getProfileManager().isAutoApplyForNewInstalledAppsEnabled());
        autoConfigPref.setOnPreferenceChangeListener((preference, newValue) -> {
            if (thanos.isServiceInstalled()) {
                boolean checked = (boolean) newValue;
                thanos.getProfileManager().setAutoApplyForNewInstalledAppsEnabled(checked);
            }
            return true;
        });

        Preference preference = findPreference(getString(R.string.key_new_installed_apps_config));
        preference.setOnPreferenceClickListener(preference1 -> {
            AppInfo appInfo = new AppInfo();
            appInfo.setSelected(false);
            appInfo.setPkgName(ProfileManager.PROFILE_AUTO_APPLY_NEW_INSTALLED_APPS_CONFIG_PKG_NAME);
            appInfo.setAppLabel(getString(R.string.pref_title_new_installed_apps_config));
            appInfo.setDummy(true);
            appInfo.setVersionCode(-1);
            appInfo.setVersionCode(-1);
            appInfo.setUid(-1);
            AppDetailsActivity.start(getActivity(), appInfo);
            return true;
        });

        Preference licensePref = findPreference(getString(R.string.key_open_source_license));
        licensePref.setOnPreferenceClickListener(preference12 -> {
            showLicenseDialog();
            return true;
        });

        findPreference(getString(R.string.key_contributors))
                .setSummary(BuildProp.THANOX_CONTRIBUTORS);

        // Market feature control.
        // Wish more row users.
        themePref.setVisible(!ThanosApp.isPrc() || DonateSettings.isDonated(getContext()));
        iconPref.setVisible(!ThanosApp.isPrc() || DonateSettings.isDonated(getContext()));
        themePref.getParent().setVisible(!ThanosApp.isPrc() || DonateSettings.isDonated(getContext()));
        donatePref.setVisible(ThanosApp.isPrc());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.d("onActivityResult: %s %s %s", requestCode, resultCode, ObjectToStringUtils.intentToString(data));
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_RESTORE_FILE_PICK) {
            if (data == null) {
                Timber.e("No data.");
                return;
            }

            if (getActivity() == null) return;

            Uri uri = data.getData();
            if (uri == null) {
                Toast.makeText(getActivity(), "uri == null", Toast.LENGTH_LONG).show();
                Timber.e("No uri.");
                return;
            }

            Optional.ofNullable(getActivity())
                    .ifPresent(fragmentActivity -> obtainViewModel(fragmentActivity)
                            .performRestore(new SettingsViewModel.RestoreListener() {
                                @Override
                                public void onSuccess() {
                                    if (getActivity() == null) return;
                                    Completable.fromAction(() ->
                                            new AlertDialog.Builder(getActivity())
                                                    .setMessage(getString(R.string.pre_message_restore_success))
                                                    .setCancelable(false)
                                                    .setPositiveButton(android.R.string.ok, null)
                                                    .show())
                                            .subscribeOn(AndroidSchedulers.mainThread())
                                            .subscribe();
                                }

                                @Override
                                public void onFail(String errMsg) {
                                    Completable.fromAction(() ->
                                            Toast.makeText(
                                                    fragmentActivity.getApplicationContext(),
                                                    errMsg, Toast.LENGTH_LONG).show())
                                            .subscribeOn(AndroidSchedulers.mainThread())
                                            .subscribe();
                                }
                            }, uri));
        }
    }

    @RequiresPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void backupRequested() {
        Optional.ofNullable(getActivity())
                .ifPresent(fragmentActivity -> obtainViewModel(fragmentActivity).performBackup(new SettingsViewModel.BackupListener() {
                    @Override
                    public void onSuccess(File dest) {
                        if (getActivity() == null) return;
                        new AlertDialog.Builder(getActivity())
                                .setMessage(getString(R.string.pre_message_backup_success) + "\n" + dest.getAbsolutePath())
                                .setCancelable(true)
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    }

                    @Override
                    public void onFail(String errMsg) {
                        Toast.makeText(fragmentActivity.getApplicationContext(), errMsg, Toast.LENGTH_LONG).show();
                    }
                }));
    }

    @RequiresPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void restoreRequested() {
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE_RESTORE_FILE_PICK);
    }

    private void showLicenseDialog() {
        final Notices notices = new Notices();

        notices.addNotice(
                new Notice(
                        "Thanox",
                        "https://github.com/Tornaco/Thanox",
                        null,
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "X-APM",
                        "https://github.com/Tornaco/X-APM",
                        null,
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "Lombok",
                        "https://projectlombok.org/",
                        " Copyright © 2009-2018 The Project Lombok Authors",
                        new MITLicense()));

        notices.addNotice(
                new Notice(
                        "guava",
                        "https://github.com/google/guava",
                        null,
                        new MITLicense()));

        notices.addNotice(
                new Notice(
                        "retrofit",
                        "https://github.com/square/retrofit",
                        "Copyright 2013 Square, Inc.",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "RxJava",
                        "https://github.com/ReactiveX/RxJava",
                        "Copyright (c) 2016-present, RxJava Contributors.",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "RxAndroid",
                        "https://github.com/ReactiveX/RxAndroid",
                        "Copyright 2015 The RxAndroid authors",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "RecyclerView-FastScroll",
                        "https://github.com/timusus/RecyclerView-FastScroll",
                        null,
                        new MITLicense()));

        notices.addNotice(
                new Notice(
                        "glide",
                        "https://github.com/bumptech/glide",
                        null,
                        new GlideLicense()));

        notices.addNotice(
                new Notice(
                        "material-searchview",
                        "https://github.com/Shahroz16/material-searchview",
                        " Copyright (C) 2016 Tim Malseed",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "PatternLockView",
                        "https://github.com/aritraroy/PatternLockView",
                        null,
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "PinLockView",
                        "https://github.com/aritraroy/PinLockView",
                        null,
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "MPAndroidChart",
                        "https://github.com/PhilJay/MPAndroidChart",
                        "Copyright 2019 Philipp Jahoda",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "easy-rules",
                        "https://github.com/j-easy/easy-rules",
                        "Copyright (c) 2019 Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)",
                        new MITLicense()));

        notices.addNotice(
                new Notice(
                        "easy-rules",
                        "https://github.com/JakeWharton/timber",
                        "Copyright 2013 Jake Wharton",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "FireCrasher",
                        "https://github.com/osama-raddad/FireCrasher",
                        "Copyright 2019, Osama Raddad",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "Xposed",
                        "https://github.com/rovo89/Xposed",
                        null,
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "lombok",
                        "https://github.com/rzwitserloot/lombok",
                        null,
                        new MITLicense()));

        notices.addNotice(
                new Notice(
                        "MaterialBadgeTextView",
                        "https://github.com/matrixxun/MaterialBadgeTextView",
                        null,
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "FuzzyDateFormatter",
                        "https://github.com/izacus/FuzzyDateFormatter",
                        "Copyright 2015 Jernej Virag",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "MaterialSearchView",
                        "https://github.com/MiguelCatalan/MaterialSearchView",
                        "Copyright 2015 Miguel Catalan Bañuls",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "zip4j",
                        "https://github.com/srikanth-lingala/zip4j",
                        null,
                        new ApacheSoftwareLicense20()));

        new LicensesDialog.Builder(Objects.requireNonNull(getActivity()))
                .setNotices(notices)
                .setIncludeOwnLicense(true)
                .build()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SettingsFragmentPermissionRequester.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static SettingsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(SettingsViewModel.class);
    }
}
