package github.tornaco.android.thanos.services.app.task;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.core.util.Timber;
import util.CollectionUtils;
import util.ObjectsUtils;

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
                Timber.v("RecentTaskInfo tasks: %s", tasks);
                if (tasks != null) {
                    for (ActivityManager.RecentTaskInfo rc : tasks) {
                        Timber.v("RecentTaskInfo rc: %s, persistentId: %s", rc, (rc == null ? "" : rc.persistentId));
                        if (rc != null && rc.persistentId == taskId) {
                            pkgOfThisTask = PkgUtils.packageNameOf(rc.baseIntent);
                            Timber.v("RecentTaskInfo pkgOfThisTask: %s", pkgOfThisTask);
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

    public List<Integer> getTasksIdForPackage(Context context, String packageName) {
        List<Integer> res = new ArrayList<>();
        Integer[] tasksId = taskIdCompMap.keySet().toArray(new Integer[0]);
        for (int id : tasksId) {
            ComponentName componentName = taskIdCompMap.get(id);
            if (ObjectsUtils.equals(componentName.getPackageName(), packageName)) {
                res.add(id);
            }
        }
        if (CollectionUtils.isNullOrEmpty(res)) {
            Timber.w("taskIdCompMap has no task for package: %s, pull from legacy.", packageName);
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            // Assume calling pkg has this permission.
            if (am != null) {
                List<ActivityManager.RecentTaskInfo> tasks = am.getRecentTasks(99, ActivityManager.RECENT_WITH_EXCLUDED);
                Timber.v("RecentTaskInfo tasks: %s", tasks);
                if (tasks != null) {
                    for (ActivityManager.RecentTaskInfo rc : tasks) {
                        Timber.v("RecentTaskInfo rc: %s, persistentId: %s", rc, (rc == null ? "" : rc.persistentId));
                        if (rc != null && rc.baseIntent != null) {
                            String pkgOfThisTask = PkgUtils.packageNameOf(rc.baseIntent);
                            Timber.v("RecentTaskInfo pkgOfThisTask: %s", pkgOfThisTask);
                            if (ObjectsUtils.equals(pkgOfThisTask, packageName)) {
                                res.add(rc.persistentId);
                            }
                        }
                    }
                }
            }
        }
        return res;
    }
}
