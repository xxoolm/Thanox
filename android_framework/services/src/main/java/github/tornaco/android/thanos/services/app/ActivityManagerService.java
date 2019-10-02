package github.tornaco.android.thanos.services.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.*;
import android.app.usage.IUsageStatsManager;
import android.content.*;
import android.content.pm.ApplicationInfo;
import android.os.Process;
import android.os.*;
import android.text.TextUtils;
import android.util.Log;
import com.google.common.io.Files;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.core.Res;
import github.tornaco.android.thanos.core.T;
import github.tornaco.android.thanos.core.annotation.NonNull;
import github.tornaco.android.thanos.core.annotation.Nullable;
import github.tornaco.android.thanos.core.app.AppResources;
import github.tornaco.android.thanos.core.app.IActivityManager;
import github.tornaco.android.thanos.core.app.event.IEventSubscriber;
import github.tornaco.android.thanos.core.app.event.ThanosEvent;
import github.tornaco.android.thanos.core.app.start.StartReason;
import github.tornaco.android.thanos.core.app.start.StartRecord;
import github.tornaco.android.thanos.core.app.start.StartResult;
import github.tornaco.android.thanos.core.app.start.StartResultExt;
import github.tornaco.android.thanos.core.compat.NotificationCompat;
import github.tornaco.android.thanos.core.compat.NotificationManagerCompat;
import github.tornaco.android.thanos.core.persist.RepoFactory;
import github.tornaco.android.thanos.core.persist.i.SetRepo;
import github.tornaco.android.thanos.core.pref.IPrefChangeListener;
import github.tornaco.android.thanos.core.process.ProcessRecord;
import github.tornaco.android.thanos.core.util.*;
import github.tornaco.android.thanos.services.SystemService;
import github.tornaco.android.thanos.services.*;
import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.android.thanos.services.apihint.ExecuteBySystemHandler;
import github.tornaco.android.thanos.services.app.start.StartRecorder;
import github.tornaco.android.thanos.services.app.task.TaskMapping;
import github.tornaco.android.thanos.services.n.NotificationHelper;
import github.tornaco.android.thanos.services.n.NotificationIdFactory;
import github.tornaco.android.thanos.services.n.SystemUI;
import github.tornaco.android.thanos.services.perf.PreferenceManagerService;
import github.tornaco.java.common.util.CollectionUtils;
import github.tornaco.java.common.util.ObjectsUtils;
import io.reactivex.Observable;
import io.reactivex.*;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static github.tornaco.android.thanos.core.T.Actions.ACTION_RUNNING_PROCESS_CLEAR;
import static github.tornaco.android.thanos.core.T.Actions.ACTION_RUNNING_PROCESS_VIEWER;
import static github.tornaco.android.thanos.core.T.Tags.N_TAG_BG_RESTRICT_APPS_CHANGED;
import static github.tornaco.android.thanos.core.compat.NotificationCompat.VISIBILITY_PUBLIC;

public class ActivityManagerService extends SystemService implements IActivityManager {

    private static ThreadPriorityBooster sThreadPriorityBooster
            = new ThreadPriorityBooster(Process.THREAD_PRIORITY_FOREGROUND, Process.THREAD_GROUP_RT_APP);

    private final S s;
    private final StartRecorder startRecorder = new StartRecorder();

    private final ProcessStartCheckHelper processStartCheckHelper = new ProcessStartCheckHelper();

    @Getter
    private final TaskMapping taskMapping;
    @Getter
    private final NotificationHelper notificationHelper;

    private boolean startBlockerEnabled;
    private boolean cleanUpOnTaskRemovalEnabled;
    private boolean bgRestrictEnabled;
    private boolean bgRestrictNotificationEnabled;

    private boolean bgTaskCleanUpSkipAudioFocused;
    private boolean bgTaskCleanUpSkipNotificationFocused;
    private long bgTaskCleanUpDelayMills;

    private SetRepo<String> startBlockingApps;
    private SetRepo<String> bgRestrictApps;
    private SetRepo<String> cleanUpTaskRemovalApps;

    private Map<String, ProcessRecordList> processMap = new ConcurrentHashMap<>();

    private Set<String> startBlockCallerWhiteList = new HashSet<>();

    private final IEventSubscriber thanosEventsSubscriber = new IEventSubscriber.Stub() {
        @Override
        public void onEvent(ThanosEvent e) {
            if (ObjectsUtils.equals(Intent.ACTION_SCREEN_OFF, e.getIntent().getAction())) {
                onScreenOff();
            }
        }
    };

    private final BroadcastReceiver thanosBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ObjectsUtils.equals(ACTION_RUNNING_PROCESS_CLEAR, intent.getAction())) {
                cleanUpBgTasks(false, 0);
            }
        }
    };

    public ActivityManagerService(S s) {
        this.s = s;
        this.taskMapping = new TaskMapping();
        this.notificationHelper = new NotificationHelper();
    }

    @Override
    public void onStart(Context context) {
        super.onStart(context);
        this.startBlockingApps = RepoFactory.get().getOrCreateStringSetRepo(T.startBlockerRepoFile().getPath());
        this.bgRestrictApps = RepoFactory.get().getOrCreateStringSetRepo(T.bgRestrictRepoFile().getPath());
        this.cleanUpTaskRemovalApps = RepoFactory.get().getOrCreateStringSetRepo(T.cleanUpOnTaskRemovalRepoFile().getPath());
    }

    @Override
    public void systemReady() {
        super.systemReady();

        initPrefs();
        registerReceivers();
    }

    private void registerReceivers() {
        EventBus.getInstance().registerEventSubscriber(new IntentFilter(Intent.ACTION_SCREEN_OFF), thanosEventsSubscriber);
        Objects.requireNonNull(getContext()).registerReceiver(thanosBroadcastReceiver, new IntentFilter(ACTION_RUNNING_PROCESS_CLEAR));
    }

    private void initPrefs() {
        readPrefs();
        listenToPrefs();
    }

    private void readPrefs() {
        AppResources appResources = new AppResources(getContext(), BuildProp.THANOS_APP_PKG_NAME);
        this.startBlockCallerWhiteList.addAll(Arrays.asList(appResources.getStringArray(Res.Strings.STRING_START_BLOCKER_CALLER_WHITELIST)));
        Timber.d("startBlockCallerWhiteList:\n%s", Arrays.toString(startBlockCallerWhiteList.toArray()));

        PreferenceManagerService preferenceManagerService = s.getPreferenceManagerService();
        this.startBlockerEnabled = preferenceManagerService.getBoolean(
                T.Settings.PREF_START_BLOCKER_ENABLED.getKey(),
                T.Settings.PREF_START_BLOCKER_ENABLED.getDefaultValue());
        this.cleanUpOnTaskRemovalEnabled = preferenceManagerService.getBoolean(
                T.Settings.PREF_CLEAN_UP_ON_TASK_REMOVED.getKey(),
                T.Settings.PREF_CLEAN_UP_ON_TASK_REMOVED.getDefaultValue());
        this.bgRestrictEnabled = preferenceManagerService.getBoolean(
                T.Settings.PREF_BG_RESTRICT_ENABLED.getKey(),
                T.Settings.PREF_BG_RESTRICT_ENABLED.getDefaultValue());
        this.bgRestrictNotificationEnabled = preferenceManagerService.getBoolean(
                T.Settings.PREF_SHOW_BG_RESTRICT_APPS_NOTIFICATION_ENABLED.getKey(),
                T.Settings.PREF_SHOW_BG_RESTRICT_APPS_NOTIFICATION_ENABLED.getDefaultValue());
        this.bgTaskCleanUpSkipAudioFocused = preferenceManagerService.getBoolean(
                T.Settings.PREF_BG_TASK_CLEAN_UP_SKIP_AUDIO_FOCUSED.getKey(),
                T.Settings.PREF_BG_TASK_CLEAN_UP_SKIP_AUDIO_FOCUSED.getDefaultValue());
        this.bgTaskCleanUpSkipNotificationFocused = preferenceManagerService.getBoolean(
                T.Settings.PREF_BG_TASK_CLEAN_UP_SKIP_NOTIFICATION.getKey(),
                T.Settings.PREF_BG_TASK_CLEAN_UP_SKIP_NOTIFICATION.getDefaultValue());
        this.bgTaskCleanUpDelayMills = preferenceManagerService.getLong(
                T.Settings.PREF_BG_TASK_CLEAN_UP_DELAY_MILLS.getKey(),
                T.Settings.PREF_BG_TASK_CLEAN_UP_DELAY_MILLS.getDefaultValue());
    }

    private void listenToPrefs() {
        PreferenceManagerService preferenceManagerService = s.getPreferenceManagerService();
        preferenceManagerService.registerSettingsChangeListener(new IPrefChangeListener.Stub() {
            @Override
            public void onPrefChanged(String key) {
                if (ObjectsUtils.equals(T.Settings.PREF_START_BLOCKER_ENABLED.getKey(), key)
                        || (ObjectsUtils.equals(T.Settings.PREF_BG_RESTRICT_ENABLED.getKey(), key)
                        || (ObjectsUtils.equals(T.Settings.PREF_CLEAN_UP_ON_TASK_REMOVED.getKey(), key)
                        || (ObjectsUtils.equals(T.Settings.PREF_BG_TASK_CLEAN_UP_DELAY_MILLS.getKey(), key)
                        || (ObjectsUtils.equals(T.Settings.PREF_SHOW_BG_RESTRICT_APPS_NOTIFICATION_ENABLED.getKey(), key)
                ))))) {
                    Timber.i("Pref changed, reload.");
                    readPrefs();
                }
            }
        });
    }

    @Override
    public boolean checkBroadcastingIntent(Intent intent) {
        Completable.fromRunnable(() -> publishEventToSubscribersAsync(new ThanosEvent(intent))).subscribeOn(ThanosSchedulers.serverThread()).subscribe();
        return true;
    }

    @Override
    public boolean checkService(final Intent intent, ComponentName service, int callerUid) {
        String[] callerPkgNameArr = s.getPkgManagerService().getPkgNameForUid(callerUid);
        String callerPkgName = ArrayUtils.isEmpty(callerPkgNameArr) ? null : callerPkgNameArr[0];
        StartResult res = Single
                .create((SingleOnSubscribe<StartResultExt>) emitter -> emitter.onSuccess(checkServiceInternal(service, callerPkgName)))
                .doOnSuccess(startResult -> {
                    if (startResult.getPackageName() != null) {
                        Runnable recorder = () -> startRecorder.add(StartRecord.builder()
                                .packageName(startResult.getPackageName())
                                .result(startResult.getStartResult())
                                .method(StartReason.SERVICE)
                                .starterPackageName(callerPkgName)
                                .whenByMills(System.currentTimeMillis())
                                .checker("checkService")
                                .build());
                        Completable.fromRunnable(recorder).subscribeOn(Schedulers.io()).subscribe();
                    }
                })
                .compose(fallbackStartResult())
                .blockingGet()
                .getStartResult();
        return res.res;
    }

    private StartResultExt checkServiceInternal(ComponentName service, String callerPkg) {
        if (service == null) return new StartResultExt(StartResult.BY_PASS_BAD_ARGS, null);

        final String servicePkg = service.getPackageName();
        if (TextUtils.isEmpty(servicePkg)) return new StartResultExt(StartResult.BY_PASS_BAD_ARGS, null);

        // Whitelist.
        if (s.getPkgManagerService().isPkgInWhiteList(servicePkg)) {
            return new StartResultExt(StartResult.BY_PASS_WHITE_LISTED, servicePkg);
        }

        if (!TextUtils.isEmpty(callerPkg) && startBlockCallerWhiteList.contains(callerPkg)) {
            return new StartResultExt(StartResult.BY_PASS_CALLER_WHITE_LISTED, servicePkg);
        }

        // Enabled?
        if (!startBlockerEnabled) {
            return new StartResultExt(StartResult.BY_PASS_START_BLOCKED_DISABLED, servicePkg);
        }

        // UI present?
        if (ObjectsUtils.equals(s.getActivityStackSupervisor().getCurrentFrontApp(), servicePkg)) {
            return new StartResultExt(StartResult.BY_PASS_UI_PRESENT, servicePkg);
        }

        // Has process?
        if (isPackageRunning(servicePkg)) {
            return new StartResultExt(StartResult.BY_PASS_PROCESS_RUNNING, servicePkg);
        }

        // In list?
        if (startBlockingApps.has(servicePkg)) {
            return new StartResultExt(StartResult.BLOCKED_IN_BLOCK_LIST, servicePkg);
        }

        return new StartResultExt(StartResult.BY_PASS_DEFAULT, servicePkg);
    }

    @Override
    public boolean checkRestartService(final String packageName, ComponentName service) {
        StartResult res = Single
                .create((SingleOnSubscribe<StartResultExt>) emitter -> emitter.onSuccess(checkServiceInternal(service, null)))
                .doOnSuccess(startResult -> {
                    if (startResult.getPackageName() != null) {
                        Runnable recorder = () -> startRecorder.add(StartRecord.builder()
                                .packageName(startResult.getPackageName())
                                .result(startResult.getStartResult())
                                .method(StartReason.RESTART_SERVICE)
                                .whenByMills(System.currentTimeMillis())
                                .checker("checkRestartService")
                                .build());
                        Completable.fromRunnable(recorder).subscribeOn(Schedulers.io()).subscribe();
                    }
                })
                .compose(fallbackStartResult())
                .blockingGet()
                .getStartResult();
        return res.res;
    }

    @Override
    public boolean checkBroadcast(final Intent intent, int receiverUid, int callerUid) {
        StartResult res = Single
                .create((SingleOnSubscribe<StartResultExt>) emitter -> {
                    // TODO Just use the first one.
                    String[] receiverPkgNameArr = s.getPkgManagerService().getPkgNameForUid(receiverUid);
                    String receiverPkgName = ArrayUtils.isEmpty(receiverPkgNameArr) ? null : receiverPkgNameArr[0];
                    StartResultExt result = checkBroadcastInternal(intent, receiverUid, receiverPkgName, callerUid);
                    emitter.onSuccess(result);
                })
                .doOnSuccess(startResult -> {
                    // Logging.
                    if (startResult.getPackageName() != null) {
                        Runnable recorder = () -> startRecorder.add(StartRecord.builder()
                                .packageName(startResult.getPackageName())
                                .method(StartReason.BROADCAST)
                                .whenByMills(System.currentTimeMillis())
                                .result(startResult.getStartResult())
                                .checker("checkBroadcast")
                                .build());
                        Completable.fromRunnable(recorder).subscribeOn(Schedulers.io()).subscribe();
                    }
                })
                .compose(fallbackStartResult())
                .blockingGet()
                .getStartResult();
        return res.res;
    }

    private SingleTransformer<StartResultExt, StartResultExt> fallbackStartResult() {
        return upstream -> upstream.onErrorReturnItem(new StartResultExt(StartResult.BY_PASS_DEFAULT_THANOS_ERROR, null))
                // Give us 100ms to handle everything.
                // do not drain too much.
                .timeout(100, TimeUnit.MILLISECONDS, observer -> observer.onSuccess(new StartResultExt(StartResult.BY_PASS_DEFAULT_THANOS_TIMEOUT, null)));
    }

    private StartResultExt checkBroadcastInternal(final Intent intent, int receiverUid, String receiverPkgName, int callerUid) {
        Timber.v("checkBroadcastInternal: %s %s %s %s", intent, receiverUid, receiverPkgName, callerUid);
        if (receiverUid == callerUid) return new StartResultExt(StartResult.BY_PASS_SAME_CALLING_UID, null);

        if (TextUtils.isEmpty(receiverPkgName)) return new StartResultExt(StartResult.BY_PASS_BAD_ARGS, null);

        // Whitelist.
        if (s.getPkgManagerService().isPkgInWhiteList(receiverPkgName)
                || PkgUtils.isSystemOrPhoneOrShell(receiverUid)) {
            return new StartResultExt(StartResult.BY_PASS_WHITE_LISTED, receiverPkgName);
        }

        // Enabled?
        if (!startBlockerEnabled) {
            return new StartResultExt(StartResult.BY_PASS_START_BLOCKED_DISABLED, receiverPkgName);
        }

        // UI present?
        if (ObjectsUtils.equals(s.getActivityStackSupervisor().getCurrentFrontApp(), receiverPkgName)) {
            return new StartResultExt(StartResult.BY_PASS_UI_PRESENT, receiverPkgName);
        }

        // Has process?
        if (isPackageRunning(receiverPkgName)) {
            return new StartResultExt(StartResult.BY_PASS_PROCESS_RUNNING, receiverPkgName);
        }

        // Default SMS app.
        if (PkgUtils.isDefaultSmsApp(getContext(), receiverPkgName)) {
            return new StartResultExt(StartResult.BY_PASS_SMS_APP, receiverPkgName);
        }

        // In list?
        if (startBlockingApps.has(receiverPkgName)) {
            return new StartResultExt(StartResult.BLOCKED_IN_BLOCK_LIST, receiverPkgName);
        }

        return new StartResultExt(StartResult.BY_PASS_DEFAULT, receiverPkgName);
    }

    @Override
    public boolean checkStartProcess(final ApplicationInfo applicationInfo, String hostType, String hostName) {
        StartResult res = Single
                .create((SingleOnSubscribe<StartResultExt>) emitter -> {
                    emitter.onSuccess(checkStartProcessInternal(applicationInfo, hostType));
                })
                .doOnSuccess(startResult -> {
                    if (startResult.getPackageName() != null) {
                        Runnable recorder = () -> startRecorder.add(StartRecord.builder()
                                .packageName(startResult.getPackageName())
                                .method(processStartCheckHelper.getStartReasonFromHostType(hostType))
                                .result(startResult.getStartResult())
                                .whenByMills(System.currentTimeMillis())
                                .checker("checkStartProcess")
                                .build());
                        Completable.fromRunnable(recorder).subscribeOn(Schedulers.io()).subscribe();
                    }
                })
                .compose(fallbackStartResult())
                .blockingGet()
                .getStartResult();
        return res.res;
    }

    private StartResultExt checkStartProcessInternal(final ApplicationInfo applicationInfo, String hostType) {
        String processPackage = applicationInfo.packageName;

        if (TextUtils.isEmpty(processPackage)) {
            return new StartResultExt(StartResult.BY_PASS_BAD_ARGS, null);
        }

        // Always allow for activity.
        if (!processStartCheckHelper.getProcessCheckType().contains(hostType)) {
            return new StartResultExt(StartResult.BY_PASS_DEFAULT, processPackage);
        }

        // Whitelist.
        if (s.getPkgManagerService().isPkgInWhiteList(processPackage)) {
            return new StartResultExt(StartResult.BY_PASS_WHITE_LISTED, processPackage);
        }

        // Enabled?
        if (!startBlockerEnabled) {
            return new StartResultExt(StartResult.BY_PASS_START_BLOCKED_DISABLED, processPackage);
        }

        // UI present?
        if (ObjectsUtils.equals(s.getActivityStackSupervisor().getCurrentFrontApp(), processPackage)) {
            return new StartResultExt(StartResult.BY_PASS_UI_PRESENT, processPackage);
        }

        // Has process?
        if (isPackageRunning(processPackage)) {
            return new StartResultExt(StartResult.BY_PASS_PROCESS_RUNNING, processPackage);
        }

        // Default SMS app.
        if (PkgUtils.isDefaultSmsApp(getContext(), processPackage)) {
            return new StartResultExt(StartResult.BY_PASS_SMS_APP, processPackage);
        }

        // In list?
        if (startBlockingApps.has(processPackage)) {
            return new StartResultExt(StartResult.BLOCKED_IN_BLOCK_LIST, processPackage);
        }
        return new StartResultExt(StartResult.BY_PASS_DEFAULT, processPackage);
    }

    @Override
    public void onStartProcessLocked(ProcessRecord processRecord) {
        Timber.v("onStartProcessLocked, processRecord: %s", processRecord);
    }

    @Override
    public void addProcessNameLocked(ProcessRecord processRecord) {
        Completable.fromRunnable(() -> addProcessNameInternal(processRecord)).subscribeOn(ThanosSchedulers.serverThread()).subscribe();
    }

    @ExecuteBySystemHandler
    private void addProcessNameInternal(ProcessRecord processRecord) {
        Timber.v("addProcessNameInternal, processRecord: %s", processRecord);
        if (processRecord.getPid() == 0) {
            Timber.e("Invalid pid=0, processRecord %s", processRecord);
            return;
        }
        if (processRecord.getPackageName() != null) {
            ProcessRecordList records = processMap.get(processRecord.getPackageName());
            if (records == null) records = new ProcessRecordList(processRecord.getPackageName());
            records.addProcessRecord(processRecord);
            processMap.put(processRecord.getPackageName(), records);

            onRunningProcessChanged();
        }
    }

    @Override
    public void removeProcessNameLocked(ProcessRecord processRecord) {
        Completable.fromRunnable(() -> removeProcessNameInternal(processRecord)).subscribeOn(ThanosSchedulers.serverThread()).subscribe();
    }

    @ExecuteBySystemHandler
    private void removeProcessNameInternal(ProcessRecord processRecord) {
        Timber.v("removeProcessNameInternal, processRecord: %s", processRecord);
        if (processRecord.getPackageName() != null) {
            ProcessRecordList records = processMap.get(processRecord.getPackageName());
            if (records == null) records = new ProcessRecordList(processRecord.getPackageName());
            boolean removed = records.removeProcessRecord(processRecord);
            Timber.v("removeProcessNameLocked, %s  removed ? %s", processRecord, removed);
            processMap.put(processRecord.getPackageName(), records);

            onRunningProcessChanged();
        }
    }

    @ExecuteBySystemHandler
    private void onRunningProcessChanged() {
        EventBus.getInstance().publishEventToSubscribersAsync(
                new ThanosEvent(new Intent(T.Actions.ACTION_RUNNING_PROCESS_CHANGED)));

        Completable
                .fromRunnable(this::maybeShowBgRestrictNotification)
                .subscribeOn(ThanosSchedulers.serverThread())
                .subscribe();
    }

    private void maybeShowBgRestrictNotification() {
        if (!isNotificationPostReady()) return;
        if (!bgRestrictNotificationEnabled) return;
        if (!bgRestrictEnabled) return;

        Timber.v("Will showBgRestrictNotification");
        // Get pending.
        String[] allRunning = getRunningAppPackagesFilter(this::isPkgBgRestricted);
        if (allRunning.length == 0) {
            clearRunningAppProcessUpdateNotification();
            return;
        }
        String firstLabel = s.getPkgManagerService().getAppInfo(allRunning[0]).getAppLabel();

        // For oreo.
        notificationHelper.createSilenceNotificationChannel(getContext());

        Intent clearBroadcastIntent = new Intent(ACTION_RUNNING_PROCESS_CLEAR);
        PendingIntent clearPendingIntent = PendingIntent.getBroadcast(getContext(),
                NotificationIdFactory.getNextId(), clearBroadcastIntent, 0);

        Intent viewerIntent = new Intent(ACTION_RUNNING_PROCESS_VIEWER);
        viewerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent viewerPendingIntent = PendingIntent.getActivity(getContext(),
                NotificationIdFactory.getNextId(), viewerIntent, 0);

        AppResources appResource = new AppResources(getContext(), BuildProp.THANOS_APP_PKG_NAME);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),
                T.serviceSilenceNotificationChannel());

        SystemUI.overrideNotificationAppName(getContext(), builder,
                appResource.getString(Res.Strings.STRING_SERVICE_NOTIFICATION_OVERRIDE_THANOS));

        Notification n = builder
                .setContentTitle(appResource.getString(Res.Strings.STRING_SERVICE_NOTIFICATION_TITLE_BG_RESTRICT_PROCESS_CHANGED))
                .setContentText(appResource.getString(Res.Strings.STRING_SERVICE_NOTIFICATION_CONTENT_BG_RESTRICT_PROCESS_CHANGED,
                        firstLabel,
                        allRunning.length))
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setVisibility(VISIBILITY_PUBLIC)
                .setContentIntent(viewerPendingIntent)
                .setAutoCancel(true)
                .addAction(0, appResource.getString(Res.Strings.STRING_SERVICE_NOTIFICATION_ACTION_BG_RESTRICT_PROCESS_CHANGED_CLEAR),
                        clearPendingIntent)
                .addAction(0, appResource.getString(Res.Strings.STRING_SERVICE_NOTIFICATION_ACTION_BG_RESTRICT_PROCESS_CHANGED_VIEW),
                        viewerPendingIntent)
                .build();

        if (OsUtils.isMOrAbove()) {
            n.setSmallIcon(appResource.getIcon(Res.Drawables.DRAWABLE_ROCKET_2_FILL));
        }

        NotificationManagerCompat.from(getContext())
                .notify(NotificationIdFactory.getIdByTag(N_TAG_BG_RESTRICT_APPS_CHANGED), n);

    }

    private void clearRunningAppProcessUpdateNotification() {
        NotificationManager notificationManager = (NotificationManager)
                getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NotificationIdFactory.getIdByTag(N_TAG_BG_RESTRICT_APPS_CHANGED));
    }

    @Override
    public ProcessRecord[] getRunningAppProcess() {
        boostPriorityForLockedSection();
        Set<ProcessRecord> records = new HashSet<>();
        CollectionUtils.consumeRemaining(processMap.values(),
                processRecordList -> records.addAll(processRecordList.getProcessRecords()));
        try {
            return records.toArray(new ProcessRecord[0]);
        } finally {
            resetPriorityAfterLockedSection();
        }
    }

    @Override
    public String[] getRunningAppPackages() {
        return getRunningAppPackagesFilter(pkg ->
                !s.getPkgManagerService().isPkgInWhiteList(pkg));
    }

    private String[] getRunningAppPackagesFilter(@Nullable Predicate<String> predicate) {
        boostPriorityForLockedSection();
        List<String> records = new ArrayList<>();

        DevNull.accept(Observable.fromIterable(processMap.values())
                .distinct()
                .filter(processRecordList -> processRecordList != null && !processRecordList.isEmpty())
                .toSortedList()
                .flatMapObservable((Function<List<ProcessRecordList>, ObservableSource<ProcessRecordList>>) Observable::fromIterable)
                .map(ProcessRecordList::getPackageName)
                .distinct()
                .filter(predicate)
                .toList()
                .subscribe((Consumer<List<String>>) records::addAll));

        try {
            return records.toArray(new String[0]);
        } finally {
            resetPriorityAfterLockedSection();
        }
    }

    @Override
    public int getRunningAppsCount() {
        return getRunningAppPackages().length;
    }

    @Override
    public ProcessRecord[] getRunningAppProcessForPackage(String pkgName) {
        boostPriorityForLockedSection();
        ProcessRecordList processRecordList = processMap.get(pkgName);
        if (processRecordList == null) {
            Timber.e("processRecordList not found for pkg: %s", pkgName);
            return new ProcessRecord[0];
        }
        Set<ProcessRecord> records = new HashSet<>();
        DevNull.accept(Observable.fromIterable(processRecordList.getProcessRecords())
                .filter(processRecord -> processRecord.getPid() != 0)
                .subscribe(records::add));
        try {
            return records.toArray(new ProcessRecord[0]);
        } finally {
            resetPriorityAfterLockedSection();
        }
    }

    @Override
    public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcessLegacy() {
        ActivityManager activityManager = (ActivityManager) Objects.requireNonNull(getContext())
                .getSystemService(Context.ACTIVITY_SERVICE);
        final long id = Binder.clearCallingIdentity();
        try {
            Binder.clearCallingIdentity();
            if (activityManager != null) {
                return activityManager.getRunningAppProcesses();
            }
        } catch (Throwable e) {
            Timber.e("getRunningAppProcesses: " + Log.getStackTraceString(e));
        } finally {
            Binder.restoreCallingIdentity(id);
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<ActivityManager.RunningServiceInfo> getRunningServiceLegacy(int max) {
        ActivityManager activityManager = (ActivityManager) Objects.requireNonNull(getContext())
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            long id = Binder.clearCallingIdentity();
            try {
                /*
                 * @deprecated As of {@link android.os.Build.VERSION_CODES#O}, this method
                 * is no longer available to third party applications.  For backwards compatibility,
                 * it will still return the caller's own services.
                 * */
                return activityManager.getRunningServices(max);
            } catch (Throwable e) {
                Timber.e("Fail getRunningServices: " + Log.getStackTraceString(e));
            } finally {
                Binder.restoreCallingIdentity(id);
            }
        }

        return new ArrayList<>(0);
    }

    @Override
    public boolean isPackageRunning(String pkgName) {
        ProcessRecordList records = processMap.get(pkgName);
        return records != null && !records.isEmpty();
    }

    @Override
    public StartRecord[] getStartRecordsByPackageName(String pkgName) {
        return startRecorder.getByPackageName(pkgName);
    }

    @Override
    public long getStartRecordsBlockedCount() {
        return startRecorder.getAllBlockedTimes();
    }

    @Override
    public boolean isStartBlockEnabled() {
        return startBlockerEnabled;
    }

    @Override
    public void setStartBlockEnabled(boolean enable) {
        this.startBlockerEnabled = enable;

        PreferenceManagerService preferenceManagerService = s.getPreferenceManagerService();
        preferenceManagerService.putBoolean(
                T.Settings.PREF_START_BLOCKER_ENABLED.getKey(),
                enable);
    }

    @Override
    public void setPkgStartBlockEnabled(String pkgName, boolean enable) {
        Timber.d("setPkgStartBlockEnabled: %s %s", pkgName, enable);
        if (enable) {
            this.startBlockingApps.add(pkgName);
        } else {
            this.startBlockingApps.remove(pkgName);
        }
    }

    @Override
    public boolean isPkgStartBlocking(String pkgName) {
        return this.startBlockingApps.has(pkgName);
    }

    @Override
    public boolean isCleanUpOnTaskRemovalEnabled() {
        return cleanUpOnTaskRemovalEnabled;
    }

    @Override
    public void setCleanUpOnTaskRemovalEnabled(boolean enable) {
        cleanUpOnTaskRemovalEnabled = enable;

        PreferenceManagerService preferenceManagerService = s.getPreferenceManagerService();
        preferenceManagerService.putBoolean(
                T.Settings.PREF_CLEAN_UP_ON_TASK_REMOVED.getKey(),
                enable);
    }

    @Override
    public void setPkgCleanUpOnTaskRemovalEnabled(String pkgName, boolean enable) {
        Timber.d("setPkgCleanUpOnTaskRemovalEnabled: %s %s", pkgName, enable);
        if (enable) {
            this.cleanUpTaskRemovalApps.add(pkgName);
        } else {
            this.cleanUpTaskRemovalApps.remove(pkgName);
        }
    }

    @Override
    public boolean isPkgCleanUpOnTaskRemovalEnabled(String pkgName) {
        return cleanUpTaskRemovalApps.has(pkgName);
    }

    @Override
    public boolean isBgRestrictEnabled() {
        return bgRestrictEnabled;
    }

    @Override
    public void setBgRestrictEnabled(boolean enable) {
        this.bgRestrictEnabled = enable;

        PreferenceManagerService preferenceManagerService = s.getPreferenceManagerService();
        preferenceManagerService.putBoolean(
                T.Settings.PREF_BG_RESTRICT_ENABLED.getKey(),
                enable);
    }

    @Override
    public boolean isBgTaskCleanUpSkipAudioFocusedAppEnabled() {
        return this.bgTaskCleanUpSkipAudioFocused;
    }

    @Override
    public void setBgTaskCleanUpSkipAudioFocusedAppEnabled(boolean enable) {
        this.bgTaskCleanUpSkipAudioFocused = enable;
        PreferenceManagerService preferenceManagerService = s.getPreferenceManagerService();
        preferenceManagerService.putBoolean(
                T.Settings.PREF_BG_TASK_CLEAN_UP_SKIP_AUDIO_FOCUSED.getKey(),
                enable);
    }

    @Override
    public boolean isBgTaskCleanUpSkipWhichHasNotificationEnabled() {
        return bgTaskCleanUpSkipNotificationFocused;
    }

    @Override
    public void setBgTaskCleanUpSkipWhichHasNotificationEnabled(boolean enable) {
        this.bgTaskCleanUpSkipNotificationFocused = enable;
        PreferenceManagerService preferenceManagerService = s.getPreferenceManagerService();
        preferenceManagerService.putBoolean(
                T.Settings.PREF_BG_TASK_CLEAN_UP_SKIP_NOTIFICATION.getKey(),
                enable);
    }

    @Override
    public void setPkgBgRestrictEnabled(String pkgName, boolean enable) {
        Timber.d("setPkgBgRestrictEnabled: %s %s", pkgName, enable);
        if (enable) {
            this.bgRestrictApps.add(pkgName);
        } else {
            this.bgRestrictApps.remove(pkgName);
        }
    }

    @Override
    public boolean isPkgBgRestricted(String pkgName) {
        return this.bgRestrictApps.has(pkgName);
    }

    @Override
    public void setBgTaskCleanUpDelayTimeMills(long delayMills) {
        this.bgTaskCleanUpDelayMills = delayMills;
        PreferenceManagerService preferenceManagerService = s.getPreferenceManagerService();
        preferenceManagerService.putLong(T.Settings.PREF_BG_TASK_CLEAN_UP_DELAY_MILLS.getKey(), delayMills);
    }

    @Override
    public long getBgTaskCleanUpDelayTimeMills() {
        return this.bgTaskCleanUpDelayMills;
    }

    @Override
    public String getCurrentFrontApp() {
        return s.getActivityStackSupervisor().getCurrentFrontApp();
    }

    @Override
    @ExecuteBySystemHandler
    public void forceStopPackage(String packageName) {
        Completable.fromRunnable(() -> {
            ActivityManager am = (ActivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.ACTIVITY_SERVICE);
            am.forceStopPackage(packageName);
            Timber.d("forceStopped Package: %s", packageName);
        }).subscribeOn(ThanosSchedulers.serverThread()).subscribe();
    }

    @ExecuteBySystemHandler
    private void killProcessForPackage(String packageName) {
        DevNull.accept(Observable.fromArray(getAllPidForPackage(packageName).toArray(new Long[0]))
                .distinct()
                .filter(pid -> {
                    Timber.v("Check pid: %s", pid);
                    return true;
                })
                .filter(pid -> pid != null && pid > 10000)
                .subscribeOn(ThanosSchedulers.serverThread())
                .subscribe(pid -> {
                    Timber.d("Killing process: %s for pkg: %s", pid, packageName);
                    Process.killProcessQuiet(pid.intValue());
                }));
    }

    private Set<Long> getAllPidForPackage(String pkgName) {
        Set<Long> pid = new HashSet<>();
        ProcessRecordList processRecordList = processMap.get(pkgName);
        if (processRecordList == null || processRecordList.isEmpty()) return pid;
        CollectionUtils.consumeRemaining(processRecordList.getProcessRecords(),
                processRecord -> pid.add(processRecord.getPid()));
        return pid;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    @ExecuteBySystemHandler
    public void idlePackage(String packageName) {

        Runnable idle = () -> {
            IUsageStatsManager usm = IUsageStatsManager.Stub.asInterface(ServiceManager
                    .getService(Context.USAGE_STATS_SERVICE));
            try {
                usm.setAppInactive(packageName, true, UserHandle.getUserId(Binder.getCallingUid()));
                Timber.d("Finish idlePackage: %s", packageName);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        };

        Completable.fromRunnable(idle).subscribeOn(ThanosSchedulers.serverThread()).subscribe();
    }

    @Override
    public void onTaskRemoving(int callingUid, int taskId) {
        Timber.d("onTaskRemoving: taskId: %s, callingUid: %s", taskId, callingUid);
        Completable
                .fromRunnable(() -> onTaskRemovingInternal(callingUid, taskId))
                .subscribeOn(ThanosSchedulers.serverThread())
                .subscribe();
    }

    @ExecuteBySystemHandler
    private void onTaskRemovingInternal(int callingUid, int taskId) {
        String pkgName = taskMapping.getPackageNameForTaskId(getContext(), taskId);
        Timber.v("onTaskRemovingInternal: task pkg is: %s", pkgName);
        if (!TextUtils.isEmpty(pkgName) && cleanUpOnTaskRemovalEnabled && isPkgCleanUpOnTaskRemovalEnabled(pkgName)) {
            Timber.v("onTaskRemovingInternal: will force stop it");
            forceStopPackage(pkgName);
        }
    }

    @Override
    public void notifyTaskCreated(int taskId, ComponentName componentName) {
        Timber.v("notifyTaskCreated: taskId: %s, componentName: %s", taskId, componentName);
        DevNull.accept(taskMapping.put(taskId, componentName));
    }

    @Override
    @Beta
    public ActivityManager.MemoryInfo getMemoryInfo() {
        long ident = Binder.clearCallingIdentity();
        try {
            ActivityManager.MemoryInfo m = new ActivityManager.MemoryInfo();
            ActivityManagerNative.getDefault().getMemoryInfo(m);
            return m;
        } catch (Throwable e) {
            Timber.e("getMemoryInfo: " + Log.getStackTraceString(e));
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
        return new ActivityManager.MemoryInfo();
    }

    @Override
    public long[] getProcessPss(int[] pids) {
        long id = Binder.clearCallingIdentity();
        try {
            return ActivityManagerNative.getDefault().getProcessPss(pids);
        } catch (Throwable e) {
            Timber.e("getProcessPss: " + Log.getStackTraceString(e));
        } finally {
            Binder.restoreCallingIdentity(id);
        }
        return new long[0];
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onApplicationCrashing(String eventType, String processName, ProcessRecord process, String stackTrace) {
        Timber.e("onApplicationCrashing: %s %s %s %s", eventType, processName, process, stackTrace);
        if (BootStrap.isLoggingEnabled()) {
            Completable.fromRunnable(() -> {
                // Dump.
                File logFile = new File(T.baseServerLoggingDir(), "log/crash/"
                        + eventType + "_" + processName + "_"
                        + DateUtils.formatForFileName(System.currentTimeMillis()) + ".log");
                Timber.w("Writing to log file: %s", logFile);
                try {
                    Files.createParentDirs(logFile);
                    Files.asByteSink(logFile).asCharSink(Charset.defaultCharset()).write(stackTrace);
                    Timber.w("Write complete to log file: %s", logFile);
                } catch (IOException e) {
                    Timber.e("Fail write log file", e);
                }
            }).subscribeOn(Schedulers.io()).subscribe();
        }
    }

    @NonNull
    @Override
    protected String serviceName() {
        return "ActivityManager";
    }

    @Override
    public IBinder asBinder() {
        return Noop.notSupported();
    }

    private void onScreenOff() {
        Timber.d("Handle screen off.");
        if (bgRestrictEnabled) {
            cleanUpBgTasks(true, bgTaskCleanUpDelayMills);
        }
    }

    @ExecuteBySystemHandler
    private void cleanUpBgTasks(boolean onlyWhenIsNotInteractive, long delay) {
        PowerManager power = (PowerManager) Objects.requireNonNull(getContext()).getSystemService(Context.POWER_SERVICE);
        String[] runningPkgs = getRunningAppPackages();
        Timber.d("Cleaning up background tasks: %s", Arrays.toString(runningPkgs));

        DevNull.accept(Observable
                .fromArray(runningPkgs)
                .subscribeOn(ThanosSchedulers.serverThread())
                .delay(Math.max(0, delay), TimeUnit.MILLISECONDS)
                .distinct()
                .filter(pkg -> {

                            if (!isPkgBgRestricted(pkg)) {
                                Timber.d("Package %s is not restricted, ignore.", pkg);
                                return false;
                            }
                            if (ObjectsUtils.equals(pkg, getCurrentFrontApp())) {
                                Timber.d("Package %s is @front, ignore.", pkg);
                                return false;
                            }
                            if (s.getPkgManagerService().isPkgInWhiteList(pkg)) {
                                Timber.d("Package %s is @white-list, ignore.", pkg);
                                return false;
                            }
                            if (onlyWhenIsNotInteractive && power.isInteractive()) {
                                Timber.d("Interactive, ignore %s", pkg);
                                return false;
                            }
                            if (!isPackageRunning(pkg)) {
                                Timber.d("Package %s is not running, ignore.", pkg);
                                return false;
                            }
                            if (bgTaskCleanUpSkipAudioFocused && s.getAudioService().hasAudioFocus(pkg)) {
                                Timber.d("Package %s has audio focus, ignore.", pkg);
                                return false;
                            }
                            if (bgTaskCleanUpSkipNotificationFocused && s.getNotificationManagerService().hasNotificationRecordsForPackage(pkg)) {
                                Timber.d("Package %s has notification, ignore.", pkg);
                                return false;
                            }

                            return true;
                        }
                )
                .subscribe(pkg -> {
                    killProcessForPackage(pkg);
                    forceStopPackage(pkg);
                    Timber.d("Clean up background task: %s", pkg);
                }, Rxs.ON_ERROR_LOGGING, () -> Timber.d("Clean up background tasks complete")));
    }

    private static void boostPriorityForLockedSection() {
        sThreadPriorityBooster.boost();
    }

    private static void resetPriorityAfterLockedSection() {
        sThreadPriorityBooster.reset();
    }

    private static void publishEventToSubscribersAsync(final ThanosEvent event) {
        EventBus.getInstance().publishEventToSubscribersAsync(event);
    }
}
