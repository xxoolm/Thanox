package github.tornaco.android.thanox.module.activity.trampoline;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.component.ComponentReplacement;
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
    protected final List<Disposable> disposables = new ArrayList<>();
    @Getter
    protected final ObservableArrayList<ComponentReplacement> listModels = new ObservableArrayList<>();

    private TrampolineLoader trampolineLoader = () -> {
        List<ComponentReplacement> res = new ArrayList<>();
        ThanosManager.from(getApplication())
                .ifServiceInstalled(thanosManager -> res.addAll(Arrays.asList(thanosManager.getActivityStackSupervisor()
                        .getComponentReplacements())));
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
                .create((SingleOnSubscribe<List<ComponentReplacement>>) emitter ->
                        emitter.onSuccess(Objects.requireNonNull(trampolineLoader.load())))
                .flatMapObservable((Function<List<ComponentReplacement>,
                        ObservableSource<ComponentReplacement>>) Observable::fromIterable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> listModels.clear())
                .subscribe(listModels::add, Rxs.ON_ERROR_LOGGING, () -> isDataLoading.set(false)));
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

    public interface TrampolineLoader {
        @WorkerThread
        List<ComponentReplacement> load();
    }
}
