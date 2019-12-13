package github.tornaco.android.thanos.main;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import androidx.databinding.ObservableLong;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.ThanosApp;
import github.tornaco.android.thanos.app.donate.DonateSettings;
import github.tornaco.android.thanos.apps.SuggestedAppsActivity;
import github.tornaco.android.thanos.core.T;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.dashboard.Tile;
import github.tornaco.android.thanos.notification.ScreenOnNotificationActivity;
import github.tornaco.android.thanos.power.SmartStandbyActivity;
import github.tornaco.android.thanos.privacy.DataCheatActivity;
import github.tornaco.android.thanos.start.BackgroundRestrictActivity;
import github.tornaco.android.thanos.start.StartRestrictActivity;
import github.tornaco.android.thanos.task.CleanUpOnTaskRemovedActivity;
import github.tornaco.android.thanos.task.RecentTaskBlurListActivity;
import github.tornaco.android.thanox.module.activity.trampoline.ActivityTrampolineActivity;
import github.tornaco.thanos.android.module.profile.RuleListActivity;
import github.tornaco.thanos.android.ops.ops.by.app.AppListActivity;
import github.tornaco.thanos.android.ops.ops.by.ops.AllOpsListActivity;
import github.tornaco.thanos.android.ops.ops.remind.RemindOpsActivity;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import rx2.android.schedulers.AndroidSchedulers;
import util.CollectionUtils;
import util.ObjectsUtils;

public class NavViewModel extends AndroidViewModel {
    @Getter
    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    @Getter
    private final ObservableField<State> state = new ObservableField<>(State.InActive);
    @Getter
    private final ObservableInt runningAppsCount = new ObservableInt(0);
    @Getter
    private final ObservableInt memUsagePercent = new ObservableInt(24);
    @Getter
    private final ObservableLong blockedStartCount = new ObservableLong(0L);
    @Getter
    private final ObservableInt storageUsagePercent = new ObservableInt(24);

    @Getter
    private final ObservableInt privacyAppsCount = new ObservableInt(0);
    @Getter
    private final ObservableLong privacyRequestCount = new ObservableLong(0L);

    @Getter
    private final ObservableField<String> channel = new ObservableField<>();
    @Getter
    private final ObservableBoolean isPaid = new ObservableBoolean(false);
    @Getter
    private final ObservableBoolean hasFrameworkError = new ObservableBoolean(false);

    private final List<Disposable> disposables = new ArrayList<>();

    @Getter
    private final ObservableList<Tile> boostFeatures = new ObservableArrayList<>();
    @Getter
    private final ObservableList<Tile> secureFeatures = new ObservableArrayList<>();
    @Getter
    private final ObservableList<Tile> expFeatures = new ObservableArrayList<>();
    @Getter
    private final ObservableList<Tile> pluginFeatures = new ObservableArrayList<>();

    public NavViewModel(@NonNull Application application) {
        super(application);
        registerEventReceivers();
    }

    void start() {
        loadState();

        loadBoostFeatures();
        loadSecureFeatures();
        loadExpFeatures();
        loadPluginFeatures();

        loadRunningApps();
        loadMemoryUsagePercent();
        loadStartBlockCount();

        loadPrivacyAppsCount();
        loadPrivacyRequestCount();
    }

    void reboot() {
        ThanosManager.from(getApplication())
                .ifServiceInstalled(thanosManager -> thanosManager.getPowerManager().reboot());
    }

    private void loadState() {
        ThanosManager thanos = ThanosManager.from(getApplication());
        if (!thanos.isServiceInstalled()) {
            state.set(State.InActive);
        } else if (!ObjectsUtils.equals(BuildProp.FINGERPRINT, thanos.fingerPrint())) {
            state.set(State.RebootNeeded);
        } else {
            state.set(State.Active);
        }

        // Init app info.
        hasFrameworkError.set(thanos.isServiceInstalled() && thanos.hasFrameworkInitializeError());
        channel.set(getChannelString());
        isPaid.set(!ThanosApp.isPrc() || DonateSettings.isDonated(getApplication().getApplicationContext()));
        Timber.v("isPaid? %s", isPaid.get());
    }

    private void loadRunningApps() {
        isDataLoading.set(true);
        disposables.add(Single.create((SingleOnSubscribe<Integer>) singleEmitter -> {
            ThanosManager thanosManager = ThanosManager.from(getApplication());
            if (thanosManager.isServiceInstalled()) {
                try {
                    singleEmitter.onSuccess(thanosManager.getActivityManager().getRunningAppsCount());
                } catch (Throwable e) {
                    singleEmitter.onError(e);
                }
            } else {
                isDataLoading.set(false);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    runningAppsCount.set(integer);
                    isDataLoading.set(false);
                }, Timber::e));
    }

    private void loadMemoryUsagePercent() {
        disposables.add(Single.create((SingleOnSubscribe<ActivityManager.MemoryInfo>) singleEmitter -> {
            ThanosManager thanosManager = ThanosManager.from(getApplication());
            if (thanosManager.isServiceInstalled()) {
                try {
                    ActivityManager.MemoryInfo memoryInfo = thanosManager.getActivityManager().getMemoryInfo();
                    if (memoryInfo != null) singleEmitter.onSuccess(memoryInfo);
                    else
                        singleEmitter.onError(new NullPointerException("Null memoryInfo from server."));
                } catch (Throwable e) {
                    singleEmitter.onError(e);
                }
            } else {
                isDataLoading.set(false);
            }
        })
                .map(this::getMemoryUsagePercent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    memUsagePercent.set(integer == null ? 0 : integer);
                }, Timber::e));
    }

    private int getMemoryUsagePercent(ActivityManager.MemoryInfo m) {
        if (m != null) {
            return (int) (100 * (((float) (m.totalMem - m.availMem) / (float) m.totalMem)));
        }
        return 0;
    }

    private void loadPrivacyAppsCount() {
        isDataLoading.set(true);
        disposables.add(Single.create((SingleOnSubscribe<Integer>) singleEmitter -> {
            ThanosManager thanosManager = ThanosManager.from(getApplication());
            if (thanosManager.isServiceInstalled()) {
                try {
                    singleEmitter.onSuccess(thanosManager.getPrivacyManager().getPrivacyDataCheatPkgCount());
                } catch (Throwable e) {
                    singleEmitter.onError(e);
                }
            } else {
                isDataLoading.set(false);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    privacyAppsCount.set(integer);
                    isDataLoading.set(false);
                }, Timber::e));
    }

    private void loadPrivacyRequestCount() {
        isDataLoading.set(true);
        disposables.add(Single.create((SingleOnSubscribe<Long>) singleEmitter -> {
            ThanosManager thanosManager = ThanosManager.from(getApplication());
            if (thanosManager.isServiceInstalled()) {
                try {
                    singleEmitter.onSuccess(thanosManager.getPrivacyManager().getPrivacyDataCheatRequestCount());
                } catch (Throwable e) {
                    singleEmitter.onError(e);
                }
            } else {
                isDataLoading.set(false);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    privacyRequestCount.set(integer);
                    isDataLoading.set(false);
                }, Timber::e));
    }

    private void loadBoostFeatures() {
        Resources resources = getApplication().getResources();
        disposables.add(Observable
                .just(
                        Tile.builder()
                                .iconRes(R.drawable.ic_forbid_fill)
                                .title(resources.getString(R.string.feature_title_start_restrict))
                                .summary(resources.getString(R.string.feature_desc_start_restrict))
                                .category(resources.getString(R.string.feature_category_start_manage))
                                .themeColor(R.color.md_blue_500)
                                .onClickListener(view -> StartRestrictActivity.start(getApplication()))
                                .build(),

                        Tile.builder()
                                .iconRes(R.drawable.ic_refresh_fill)
                                .title(resources.getString(R.string.feature_title_bg_restrict))
                                .summary(resources.getString(R.string.feature_desc_bg_restrict))
                                .themeColor(R.color.md_green_500)
                                .onClickListener(view -> BackgroundRestrictActivity.start(getApplication()))
                                .build(),

                        Tile.builder()
                                .iconRes(R.drawable.ic_clear_all_black_24dp)
                                .title(resources.getString(R.string.feature_title_clean_when_task_removed))
                                .summary(resources.getString(R.string.feature_desc_clean_when_task_removed))
                                .category(resources.getString(R.string.feature_category_app_clean_up))
                                .themeColor(R.color.md_grey_700)
                                .onClickListener(view -> CleanUpOnTaskRemovedActivity.start(getApplication()))
                                .build(),

                        Tile.builder()
                                .iconRes(R.drawable.ic_apps_fill)
                                .title(resources.getString(R.string.feature_title_apps_manager))
                                .summary(resources.getString(R.string.feature_summary_apps_manager))
                                .category(resources.getString(R.string.feature_category_app_manage))
                                .themeColor(R.color.md_pink_300)
                                .onClickListener(view -> SuggestedAppsActivity.start(getApplication()))
                                .build()
                )
                .filter(tile -> !tile.isDisabled())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(tile -> {
                    if (!boostFeatures.contains(tile)) {
                        boostFeatures.add(tile);
                    }
                })

        );
    }

    private void loadSecureFeatures() {
        ThanosManager thanos = ThanosManager.from(getApplication());
        Resources resources = getApplication().getResources();
        disposables.add(Observable
                .just(
                        Tile.builder()
                                .iconRes(R.drawable.ic_focus_2_line)
                                .title(resources.getString(R.string.feature_title_data_cheat))
                                .summary(thanos.isServiceInstalled()
                                        ? resources.getString(R.string.secure_privacy_request_count,
                                        String.valueOf(thanos.getPrivacyManager().getPrivacyDataCheatRequestCount()))
                                        : resources.getString(R.string.feature_desc_data_cheat))
                                .category(resources.getString(R.string.feature_category_privacy))
                                .themeColor(R.color.md_grey_500)
                                .onClickListener(view -> DataCheatActivity.start(getApplication()))
                                .build(),

                        Tile.builder()
                                .iconRes(R.drawable.module_ops_ic_shield_cross_fill)
                                .title(resources.getString(R.string.module_ops_feature_title_app_ops_list))
                                .summary(resources.getString(R.string.module_ops_feature_summary_app_ops_list))
                                .themeColor(R.color.md_lime_600)
                                // Dup with apps manager.
                                .disabled(true)
                                .onClickListener(view -> AppListActivity.start(getApplication()))
                                .build(),

                        Tile.builder()
                                .iconRes(R.drawable.ic_shield_star_fill)
                                .title(resources.getString(R.string.module_ops_feature_title_ops_app_list))
                                .summary(resources.getString(R.string.module_ops_feature_summary_ops_app_list))
                                .themeColor(R.color.md_teal_500)
                                .onClickListener(view -> AllOpsListActivity.start(getApplication()))
                                .build(),

                        Tile.builder()
                                .iconRes(R.drawable.ic_lock_fill)
                                .title(resources.getString(R.string.feature_title_app_lock))
                                .summary(resources.getString(R.string.feature_summary_app_lock))
                                .themeColor(R.color.md_deep_orange_500)
                                .onClickListener(view -> {
                                    Intent intent = new Intent(BuildProp.ACTION_APP_LOCK);
                                    intent.setPackage(BuildProp.APP_LOCK_PKG_NAME);
                                    getApplication().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                })
                                .disabled(!PkgUtils.isPkgInstalled(getApplication(), BuildProp.APP_LOCK_PKG_NAME))
                                .build(),

                        Tile.builder()
                                .iconRes(R.drawable.ic_paint_brush_fill)
                                .title(resources.getString(R.string.feature_title_recent_task_blur))
                                .summary(resources.getString(R.string.feature_summary_recent_task_blur))
                                .themeColor(R.color.md_cyan_300)
                                .onClickListener(view -> RecentTaskBlurListActivity.start(getApplication()))
                                .build(),

                        Tile.builder()
                                .iconRes(R.drawable.module_ops_ic_alarm_warning_fill)
                                .category(resources.getString(R.string.feature_category_remind))
                                .title(resources.getString(R.string.module_ops_feature_title_ops_remind_list))
                                .summary(resources.getString(R.string.module_ops_feature_summary_ops_remind))
                                .themeColor(R.color.md_pink_300)
                                .onClickListener(view -> RemindOpsActivity.start(getApplication()))
                                .build()
                )
                .filter(tile -> !tile.isDisabled())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(tile -> {
                    if (!secureFeatures.contains(tile)) {
                        secureFeatures.add(tile);
                    }
                })

        );
    }

    private void loadExpFeatures() {
        ThanosManager thanosManager = ThanosManager.from(getApplication());
        Resources resources = getApplication().getResources();
        disposables.add(Observable
                .just(
                        Tile.builder()
                                .iconRes(R.drawable.ic_notification_badge_fill)
                                .title(resources.getString(R.string.feature_title_light_on_notification))
                                .summary(resources.getString(R.string.feature_summary_light_on_notification))
                                .category(resources.getString(R.string.feature_category_notification))
                                .themeColor(R.color.md_red_500)
                                .onClickListener(view -> ScreenOnNotificationActivity.start(getApplication()))
                                .build(),
                        Tile.builder()
                                .iconRes(R.drawable.ic_guide_fill)
                                .category(resources.getString(R.string.feature_category_ext))
                                .title(resources.getString(R.string.module_activity_trampoline_app_name))
                                .disabled(!thanosManager.isServiceInstalled()
                                        || !thanosManager.hasFeature(BuildProp.THANOX_FEATURE_APP_TRAMPOLINE))
                                .summary("\uD83C\uDF6D \uD83C\uDF6D \uD83C\uDF6D")
                                .themeColor(R.color.md_green_a700)
                                .onClickListener(view -> ActivityTrampolineActivity.start(getApplication()))
                                .build(),
                        Tile.builder()
                                .iconRes(R.drawable.ic_thunderstorms_fill)
                                .title(resources.getString(R.string.module_profile_feature_name))
                                .summary(resources.getString(R.string.module_profile_feature_summary))
                                .themeColor(R.color.md_indigo_300)
                                .disabled(!thanosManager.isServiceInstalled()
                                        || !thanosManager.hasFeature(BuildProp.THANOX_FEATURE_PROFILE))
                                .onClickListener(view -> RuleListActivity.start(getApplication()))
                                .build(),
                        Tile.builder()
                                .iconRes(R.drawable.ic_loader_2_fill)
                                .category(resources.getString(R.string.feature_category_power_save))
                                .title(resources.getString(R.string.feature_title_smart_app_standby))
                                .disabled(!thanosManager.isServiceInstalled()
                                        || !thanosManager.hasFeature(BuildProp.THANOX_FEATURE_APP_SMART_STAND_BY))
                                .summary(resources.getString(R.string.feature_summary_smart_app_standby))
                                .themeColor(R.color.md_amber_500)
                                .onClickListener(view -> SmartStandbyActivity.start(getApplication()))
                                .build()
                )
                .filter(tile -> !tile.isDisabled())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(tile -> {
                    if (!expFeatures.contains(tile)) {
                        expFeatures.add(tile);
                    }
                })

        );
    }

    private void loadPluginFeatures() {
        ThanosManager thanosManager = ThanosManager.from(getApplication());
        Resources resources = getApplication().getResources();
        disposables.add(Observable
                .just(
                        Tile.builder()
                                .iconRes(R.drawable.ic_cloud_fill)
                                .title(resources.getString(R.string.feature_title_push_delegate))
                                .category(resources.getString(R.string.feature_category_plugins))
                                .themeColor(R.color.md_grey_400)
                                .disabled(!thanosManager.isServiceInstalled()
                                        || !thanosManager.hasFeature(BuildProp.THANOX_FEATURE_PUSH_DELEGATE))
                                .onClickListener(view -> {
                                })
                                .build()
                )
                .filter(tile -> !tile.isDisabled())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(tile -> {
                    if (!pluginFeatures.contains(tile)) {
                        pluginFeatures.add(tile);
                    }
                })
        );
    }

    private void loadStartBlockCount() {
        disposables.add(Single.create((SingleOnSubscribe<Long>) emitter -> {
            ThanosManager thanos = ThanosManager.from(getApplication().getApplicationContext());
            if (thanos.isServiceInstalled()) {
                emitter.onSuccess(thanos.getActivityManager().getStartRecordsBlockedCount());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> blockedStartCount.set(count == null ? 0L : count)));
    }

    private void registerEventReceivers() {
    }

    private void unRegisterEventReceivers() {
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        CollectionUtils.consumeRemaining(disposables, Disposable::dispose);
        unRegisterEventReceivers();
    }

    private String getChannelString() {
        return null;
    }

    void cleanUpBackgroundTasks() {
        getApplication().sendBroadcast(new Intent(T.Actions.ACTION_RUNNING_PROCESS_CLEAR));
    }
}
