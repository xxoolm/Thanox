package github.tornaco.gradle.plugin.auto.logging;

import com.android.build.api.transform.*;
import com.android.build.gradle.internal.pipeline.TransformManager;
import org.gradle.api.Project;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

class AutoLoggingTransform extends Transform {
    private final Project project;

    AutoLoggingTransform(Project project) {
        this.project = project;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        for (TransformInput input : inputs) {
            handleDirectoryInputs(input.getDirectoryInputs());
            handleJarInputs(input.getJarInputs());
        }
    }

    private void handleDirectoryInputs(Collection<DirectoryInput> inputs) {
        for (DirectoryInput directoryInput : inputs) {
            log(directoryInput.getFile().getAbsolutePath());
        }
    }

    private void handleJarInputs(Collection<JarInput> inputs) {
        for (JarInput jarInput : inputs) {
            log(jarInput.getFile().getAbsolutePath());
        }
    }

    @Override
    public String getName() {
        return "AutoLogging";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    private void log(String format, Object... args) {
        project.getLogger().debug(format, args);
    }
}
