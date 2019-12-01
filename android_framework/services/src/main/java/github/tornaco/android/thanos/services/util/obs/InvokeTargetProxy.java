package github.tornaco.android.thanos.services.util.obs;

import de.robv.android.xposed.XposedHelpers;
import github.tornaco.android.thanos.core.util.Timber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by guohao4 on 2018/1/23.
 * Email: Tornaco@163.com
 */
@AllArgsConstructor
@Getter
public class InvokeTargetProxy<T> {

    public @interface Target {
        String value();
    }

    @Setter
    private T host;

    @SuppressWarnings({"unchecked", "SameParameterValue", "UnusedReturnValue"})
    protected <X> X invokeMethod(Object obj, String methodName, Object... args) {
        if (host == null) {
            Timber.wtf("InvokeTargetProxy invokeMethod while host is null- " + getClass());
            return null;
        }
        try {
            Object res = XposedHelpers.callMethod(obj, methodName, args);
            return (X) res;
        } catch (Throwable e) {
            Timber.wtf("InvokeTargetProxy invokeMethod fail: " + "method: " + methodName + " class: " + getClass() + "\n" + e);
            return null;
        }
    }

    @SuppressWarnings({"SameParameterValue", "UnusedReturnValue"})
    protected <X> X invokeMethod(String methodName, Object... args) {
        return invokeMethod(host, methodName, args);
    }

    protected boolean setObjectField(String name, Object value) {
        try {
            XposedHelpers.setObjectField(getHost(), name, value);
            return true;
        } catch (Exception e) {
            Timber.wtf("InvokeTargetProxy setObjectField fail: " + "name: " + name + " class: " + getClass() + "\n" + e);
            return false;
        }
    }

    protected boolean setBooleanField(String name, boolean value) {
        try {
            XposedHelpers.setBooleanField(getHost(), name, value);
            return true;
        } catch (Exception e) {
            Timber.wtf("InvokeTargetProxy setBooleanField fail: " + "name: " + name + " class: " + getClass() + "\n" + e);
            return false;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "host=" + host +
                '}';
    }
}
