package github.tornaco.android.thanos.services.profile.handle;

public interface ITask {

    void removeTasksForPackage(String pkgName);

    boolean hasTaskFromPackage(String pkgName);

    void clearBackgroundTasks();
}
