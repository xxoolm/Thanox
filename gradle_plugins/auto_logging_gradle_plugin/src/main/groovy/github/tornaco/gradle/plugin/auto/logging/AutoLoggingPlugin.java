package github.tornaco.gradle.plugin.auto.logging;

import com.android.build.gradle.AppExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class AutoLoggingPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        AppExtension android = target.getExtensions().getByType(AppExtension.class);
        android.registerTransform(new AutoLoggingTransform(target));
    }
}
