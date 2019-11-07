package github.tornaco.android.thanos.services.util;

import android.content.pm.ApplicationInfo;

import java.util.Objects;

import de.robv.android.xposed.XposedHelpers;
import github.tornaco.android.thanos.core.process.ProcessRecord;
import github.tornaco.android.thanos.core.util.Timber;

public class ProcessRecordUtils {
    private ProcessRecordUtils() {
    }

    public static ProcessRecord fromLegacy(Object legacy) {
        if (legacy == null) {
            return null;
        }
        if (!Objects.equals(legacy.getClass().getName(), "com.android.server.am.ProcessRecord")) {
            Timber.e("ProcessRecordUtils fromLegacy error, class is not ProcessRecord: %s", legacy.getClass());
            return null;
        }
        ApplicationInfo applicationInfo = (ApplicationInfo) XposedHelpers
                .getObjectField(legacy, "info");
        String processName = (String) XposedHelpers
                .getObjectField(legacy, "processName");
        int pid = XposedHelpers.getIntField(legacy, "pid");
        return new ProcessRecord(applicationInfo.packageName, processName, pid, false, false);
    }
}
