package github.tornaco.android.thanos.process;

import android.app.Application;
import android.os.RemoteException;
import android.text.format.Formatter;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.CategoryIndex;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.process.ProcessRecord;
import github.tornaco.android.thanos.core.util.Rxs;
import github.tornaco.android.thanos.core.util.Timber;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import rx2.android.schedulers.AndroidSchedulers;
import util.CollectionUtils;

public class ProcessManageViewModel extends AndroidViewModel {
    @Getter
    private final ObservableBoolean isProcessNeedUpdate = new ObservableBoolean(false);
    @Getter
    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    private final List<Disposable> disposables = new ArrayList<>();
    @Getter
    private final ObservableList<ProcessModel> processModels = new ObservableArrayList<>();
    @Getter
    private final ObservableField<CategoryIndex> categoryIndex = new ObservableField<>(CategoryIndex._3rd);

    private ThanosManager thanos = ThanosManager.from(getApplication().getApplicationContext());

    public ProcessManageViewModel(@NonNull Application application) {
        super(application);
    }

    public void start() {
        loadProcess();
    }

    private void loadProcess() {
        if (!thanos.isServiceInstalled()) {
            return;
        }

        if (isDataLoading.get()) return;
        isDataLoading.set(true);

        String idleBadge = getApplication().getString(R.string.badge_app_idle);

        disposables.add(Single.create(
                (SingleOnSubscribe<List<String>>) emitter -> {
                    try {
                        String[] runningPkgs = thanos
                                .getActivityManager()
                                .getRunningAppPackages();
                        emitter.onSuccess(runningPkgs == null ? Collections.emptyList() : Arrays.asList(runningPkgs));
                    } catch (Throwable e) {
                        emitter.onError(e);
                    }
                })
                .map(strings -> {
                    List<ProcessModel> processModels = new ArrayList<>();
                    CollectionUtils.consumeRemaining(strings, s -> {
                        try {
                            Timber.d("loadProcess, filter index: %s", categoryIndex.get());
                            // Ignore whitelisted.
                            if (thanos.getPkgManager().isPkgInWhiteList(s)) {
                                return;
                            }

                            AppInfo appInfo = thanos.getPkgManager().getAppInfo(s);

                            // Filter.
                            CategoryIndex index = categoryIndex.get();
                            int flag = Objects.requireNonNull(index).flag;
                            if ((flag & appInfo.getFlags()) == 0) return;

                            List<ProcessRecord> processRecords = Arrays.asList(thanos
                                    .getActivityManager()
                                    .getRunningAppProcessForPackage(s));

                            if (CollectionUtils.isNullOrEmpty(processRecords)) {
                                Timber.e("Empty processRecords for %s", s);
                                return;
                            }

                            // Get mem size.
                            long size = getMemSize(processRecords);

                            String sizeStr = Formatter.formatShortFileSize(
                                    getApplication().getApplicationContext(), size);

                            ProcessModel processModel = new ProcessModel(
                                    processRecords,
                                    appInfo,
                                    size,
                                    sizeStr,
                                    null,
                                    (thanos.getActivityManager().isPackageIdle(appInfo.getPkgName()) ? idleBadge : null));
                            processModels.add(processModel);
                        } catch (RemoteException e) {
                            Timber.e(e);
                        }
                    });
                    return processModels;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(out -> {
                    processModels.clear();
                    processModels.addAll(out);
                    isDataLoading.set(false);
                    isProcessNeedUpdate.set(false);
                }, Rxs.ON_ERROR_LOGGING));
    }

    private long getMemSize(List<ProcessRecord> processRecords)
            throws RemoteException {
        int[] pids = new int[processRecords.size()];
        for (int i = 0; i < processRecords.size(); i++) {
            pids[i] = (int) processRecords.get(i).getPid();
        }
        long[] pss = thanos.getActivityManager()
                .getProcessPss(pids);
        long size = 0L;
        for (long p : pss) {
            size += p * 1024L;
        }
        return size;
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

    void killApp(AppInfo appInfo) {
        ThanosManager.from(getApplication())
                .ifServiceInstalled(thanosManager -> {
                    thanosManager.getActivityManager()
                            .forceStopPackage(appInfo.getPkgName());

                    loadProcess();
                });
    }

    void setAppCategoryFilter(int index) {
        categoryIndex.set(CategoryIndex.values()[index]);
        start();
    }
}
