package github.tornaco.permission.compiler;

import java.util.Arrays;

public class XposedHookInfo {

    private String className;
    private int[] targetSdkVersion;
    private boolean active;

    public XposedHookInfo(String className, int[] targetSdkVersion, boolean active) {
        this.className = className;
        this.targetSdkVersion = targetSdkVersion;
        this.active = active;
    }

    public String getClassName() {
        return className;
    }

    public int[] getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "XposedHookInfo{" +
                "className='" + className + '\'' +
                ", targetSdkVersion=" + Arrays.toString(targetSdkVersion) +
                ", active=" + active +
                '}';
    }
}
