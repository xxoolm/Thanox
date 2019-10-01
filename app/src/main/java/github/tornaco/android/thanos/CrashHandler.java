package github.tornaco.android.thanos;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import com.google.common.io.Files;
import com.osama.firecrasher.CrashLevel;
import com.osama.firecrasher.FireCrasher;
import github.tornaco.android.thanos.core.util.DateUtils;
import github.tornaco.android.thanos.core.util.DevNull;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

class CrashHandler {
    private static int crashTimes;

    static void install(Application application) {
        FireCrasher.INSTANCE.install(application, (throwable, activity) -> {
            Toast.makeText(activity, "CRASHED... :(", Toast.LENGTH_LONG).show();
            Object dev = Completable.fromRunnable(() -> {
                try {
                    File logFile = new File(application.getExternalCacheDir(), "logs/" + "CRASH_" + DateUtils.formatForFileName(System.currentTimeMillis()));
                    Files.createParentDirs(logFile);
                    Files.asByteSink(logFile)
                            .asCharSink(Charset.defaultCharset())
                            .write(Log.getStackTraceString(throwable));
                } catch (IOException ignored) {
                } finally {
                }
            }).subscribeOn(Schedulers.io()).subscribe(() -> {
                FireCrasher.INSTANCE.recover(CrashLevel.LEVEL_THREE, activity1 -> null);
            });
            DevNull.accept(dev);
        });
    }
}
