package github.tornaco.android.thanox.module.activity.trampoline;

import android.app.Application;
import android.content.ComponentName;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.component.ComponentReplacement;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.util.Rxs;
import github.tornaco.java.common.util.CollectionUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

public class TrampolineViewModel extends AndroidViewModel {

    @Getter
    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    private final List<Disposable> disposables = new ArrayList<>();
    @Getter
    private final ObservableArrayList<ActivityTrampolineModel> replacements = new ObservableArrayList<>();

    private TrampolineLoader trampolineLoader = () -> {
        List<ActivityTrampolineModel> res = new ArrayList<>();
        ThanosManager.from(getApplication())
                .ifServiceInstalled(thanosManager -> CollectionUtils.consumeRemaining(thanosManager.getActivityStackSupervisor()
                        .getComponentReplacements(), componentReplacement -> {
                    AppInfo appInfo = thanosManager.getPkgManager().getAppInfo(componentReplacement.from.getPackageName());
                    ActivityTrampolineModel model = new ActivityTrampolineModel(componentReplacement, appInfo);
                    res.add(model);
                }));
        return res;
    };

    public TrampolineViewModel(@NonNull Application application) {
        super(application);
        registerEventReceivers();
    }

    public void start() {
        loadModels();
    }

    private void loadModels() {
        if (isDataLoading.get()) return;
        isDataLoading.set(true);
        disposables.add(Single
                .create((SingleOnSubscribe<List<ActivityTrampolineModel>>) emitter ->
                        emitter.onSuccess(Objects.requireNonNull(trampolineLoader.load())))
                .flatMapObservable((Function<List<ActivityTrampolineModel>,
                        ObservableSource<ActivityTrampolineModel>>) Observable::fromIterable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> replacements.clear())
                .subscribe(replacements::add, Rxs.ON_ERROR_LOGGING, () -> isDataLoading.set(false)));
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

    void onRequestAddNewReplacement(ComponentName f, ComponentName t) {
        ThanosManager.from(getApplication())
                .ifServiceInstalled(thanosManager -> {
                    thanosManager.getActivityStackSupervisor()
                            .addComponentReplacement(new ComponentReplacement(f, t));
                    // Reload.
                    loadModels();
                });
    }

    void onRequestRemoveNewReplacement(ComponentName f, ComponentName t) {
        ThanosManager.from(getApplication())
                .ifServiceInstalled(thanosManager -> {
                    thanosManager.getActivityStackSupervisor()
                            .removeComponentReplacement(new ComponentReplacement(f, t));
                    // Reload.
                    loadModels();
                });
    }

    public interface TrampolineLoader {
        @WorkerThread
        List<ActivityTrampolineModel> load();
    }
}
