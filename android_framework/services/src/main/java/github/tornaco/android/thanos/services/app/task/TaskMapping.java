package github.tornaco.android.thanos.services.app.task;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.core.util.Timber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskMapping {

    @SuppressLint("UseSparseArrays")
    private final Map<Integer, ComponentName> taskIdCompMap = new HashMap<>();

    @TargetApi(Build.VERSION_CODES.O)
    public ComponentName put(int taskId, ComponentName componentName) {
        return taskIdCompMap.put(taskId, componentName);
    }

    public String getPackageNameForTaskId(Context context, int taskId) {
        String pkgOfThisTask = null;
        ComponentName targetComp = taskIdCompMap.get(taskId);
        if (targetComp != null) {
            pkgOfThisTask = targetComp.getPackageName();
        }
        // Retrieve package name for N and if no task comp got from cache.
        if (pkgOfThisTask == null) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            // Assume calling pkg has this permission.
            if (am != null) {
                List<ActivityManager.RecentTaskInfo> tasks = am.getRecentTasks(99, ActivityManager.RECENT_WITH_EXCLUDED);
                if (tasks != null) {
                    for (ActivityManager.RecentTaskInfo rc : tasks) {
                        if (rc != null && rc.persistentId == taskId) {
                            pkgOfThisTask = PkgUtils.packageNameOf(rc.baseIntent);
                            if (!TextUtils.isEmpty(pkgOfThisTask) && rc.baseIntent.getComponent() != null) {
                                // Cache.
                                taskIdCompMap.put(taskId, rc.baseIntent.getComponent());
                            }
                            break;
                        }
                    }
                }
            }
        }
        Timber.v("getPackageNameForTaskId returning: " + pkgOfThisTask);
        return pkgOfThisTask;
    }
}
