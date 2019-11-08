package github.tornaco.android.thanos.core.util;


import android.util.Log;

public abstract class AbstractSafeR implements Runnable {
    @Override
    public final void run() {
        try {
            runSafety();
        } catch (Throwable err) {
            Log.e("AbstractSafeR", "AbstractSafeR err: " + Log.getStackTraceString(err));
        }
    }

    public abstract void runSafety();
}
