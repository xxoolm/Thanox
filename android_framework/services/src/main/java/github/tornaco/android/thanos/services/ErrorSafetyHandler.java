package github.tornaco.android.thanos.services;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import io.reactivex.Completable;

public class ErrorSafetyHandler extends Handler {
    public ErrorSafetyHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        Completable.fromAction(() -> super.handleMessage(msg)).subscribe();
    }

    @Override
    public void dispatchMessage(Message msg) {
        Completable.fromAction(() -> super.dispatchMessage(msg)).subscribe();
    }
}
