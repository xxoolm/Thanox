package github.tornaco.practice.honeycomb.locker.ui.verify;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.os.CancellationSignal;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor;
import github.tornaco.android.thanos.core.app.activity.VerifyResult;
import github.tornaco.practice.honeycomb.locker.util.fingerprint.FingerprintManagerCompat;
import lombok.Setter;
import org.newstand.logger.Logger;

import java.util.Objects;

import static github.tornaco.android.thanos.core.app.activity.VerifyResult.*;

public class VerifyViewModel extends AndroidViewModel {
    private static final long PROGRESS_MAX = ActivityStackSupervisor.LOCKER_VERIFY_TIMEOUT_MILLS;
    @Setter
    private int requestCode;
    @Setter
    public String pkg;
    public ObservableBoolean verified = new ObservableBoolean(false);
    public ObservableInt progress = new ObservableInt((int) PROGRESS_MAX);
    public ObservableInt progressMax = new ObservableInt((int) PROGRESS_MAX);
    public ObservableInt failCount = new ObservableInt(0);
    private CancellationSignal cancellationSignal;

    private ActivityStackSupervisor lockerManager;

    public VerifyViewModel(@NonNull Application application) {
        super(application);
    }

    public void start() {
        if (!isCurrentLockMethodKeySet()) {
            pass(REASON_USER_KEY_NOT_SET);
            return;
        }
        setupFingerPrint();
        checkTimeout();
    }

    public void verify(String input) {
        if (!isInputCorrect(input)) {
            failOnce();
        } else {
            pass(VerifyResult.REASON_USER_INPUT_CORRECT);
        }
    }

    private void pass(int reason) {
        Objects.requireNonNull(lazyGetLockerManager()).setVerifyResult(requestCode, VerifyResult.ALLOW, reason);
        verified.set(true);
    }

    private void failOnce() {
        failCount.set(failCount.get() + 1);
    }

    public void failFinally(int reason) {
        Objects.requireNonNull(lazyGetLockerManager()).setVerifyResult(requestCode, VerifyResult.IGNORE, reason);
        verified.set(true);
    }

    public void cancel() {
        if (!verified.get()) {
            Objects.requireNonNull(lazyGetLockerManager()).setVerifyResult(requestCode,
                    VerifyResult.IGNORE, REASON_USER_CANCEL);
            verified.set(true);
        }
    }

    private int getLockMethod() {
        return Objects.requireNonNull(lazyGetLockerManager()).getLockerMethod();
    }

    private boolean isInputCorrect(String input) {
        return Objects.requireNonNull(lazyGetLockerManager()).isLockerKeyValid(getLockMethod(), input);
    }

    private boolean isCurrentLockMethodKeySet() {
        return Objects.requireNonNull(lazyGetLockerManager()).isLockerKeySet(getLockMethod());
    }

    private void setupFingerPrint() {
        if (!lazyGetLockerManager().isFingerPrintEnabled()) {
            return;
        }
        cancelFingerPrint();
        cancellationSignal = authenticateFingerPrint(
                new FingerprintManagerCompat.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(
                            FingerprintManagerCompat.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Logger.d("onAuthenticationSucceeded:" + result);
                        pass(REASON_USER_FP_INCORRECT);
                    }

                    @Override
                    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                        super.onAuthenticationHelp(helpMsgId, helpString);
                        Logger.i("onAuthenticationHelp:" + helpString);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Logger.d("onAuthenticationFailed");
                    }

                    @Override
                    public void onAuthenticationError(int errMsgId, CharSequence errString) {
                        super.onAuthenticationError(errMsgId, errString);
                        Logger.d("onAuthenticationError:" + errString);
                    }
                });
    }

    private void cancelFingerPrint() {
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
        }
    }

    private CancellationSignal authenticateFingerPrint(FingerprintManagerCompat.AuthenticationCallback callback) {
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.USE_FINGERPRINT)
                != PackageManager.PERMISSION_GRANTED) {
            Logger.w("FP Permission is missing...");
            return null;
        }
        if (!FingerprintManagerCompat.from(getApplication()).isHardwareDetected()) {
            Logger.w("FP HW is missing...");
            return null;
        }
        CancellationSignal cancellationSignal = new CancellationSignal();
        FingerprintManagerCompat.from(getApplication())
                .authenticate(null, 0, cancellationSignal, callback, null);
        Logger.i("FP authenticate...");
        return cancellationSignal;
    }

    private void checkTimeout() {
        // 60FPS
        // 1000 / 60 ~= 16.7ms
        CountDownTimer countDownTimer = new CountDownTimer(
                ActivityStackSupervisor.LOCKER_VERIFY_TIMEOUT_MILLS, 17) {
            @Override
            public void onTick(long l) {
                progress.set((int) l);
            }

            @Override
            public void onFinish() {
                onTimeout();
            }
        };
        countDownTimer.start();
        verified.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                countDownTimer.cancel();
            }
        });
    }

    private void onTimeout() {
        cancel();
    }

    private ActivityStackSupervisor lazyGetLockerManager() {
        if (lockerManager != null) return lockerManager;
        lockerManager = ThanosManager.from(getApplication().getApplicationContext())
                .getActivityStackSupervisor();
        return lockerManager;
    }
}
