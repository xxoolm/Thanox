package github.tornaco.android.thanos.services.app.task;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.java.common.util.ObjectsUtils;

public class RecentTasks {

    private final List<ComponentName> recentTasks = new ArrayList<>();

    @TargetApi(Build.VERSION_CODES.O)
    public void add(ComponentName componentName) {
        Timber.v("add: %s", componentName);
        if (componentName != null && !recentTasks.contains(componentName))
            recentTasks.add(componentName);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void remove(ComponentName componentName) {
        Timber.v("remove: %s", componentName);
        if (componentName != null) recentTasks.remove(componentName);
    }

    public boolean hasRecentTaskForPkg(String pkg) {
        for (ComponentName componentName : recentTasks.toArray(new ComponentName[0])) {
            if (ObjectsUtils.equals(pkg, componentName.getPackageName())) {
                Timber.v("There is a/(maybe not just one) task for package: %s", componentName);
                return true;
            }
        }
        return false;
    }
}
