package github.tornaco.android.thanos.services.xposed.hooks.activity;

import android.os.Build;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.xposed.annotation.XposedHook;
import lombok.experimental.Delegate;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions.*;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */
@XposedHook(targetSdkVersion = {_21, _22, _23, _24, _25, _26, _27, _28, _29})
public class TaskMoverHookRegistry implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    @Delegate
    private TaskMoverHook taskMoverHook;

    public TaskMoverHookRegistry() {
        IActivityStackSupervisor verifier = BootStrap.THANOS_X
                .getActivityStackSupervisor();
        int sdkVersion = Build.VERSION.SDK_INT;
        switch (sdkVersion) {
            case 21:
                taskMoverHook = new TaskMoverHookV21(verifier);
                break;
            case 22:
                taskMoverHook = new TaskMoverHookV22(verifier);
                break;
            case 23:
                taskMoverHook = new TaskMoverHookV23(verifier);
                break;
            case 24:
                taskMoverHook = new TaskMoverHookV24(verifier);
                break;
            case 25:
                taskMoverHook = new TaskMoverHookV25(verifier);
                break;
            case 26:
                taskMoverHook = new TaskMoverHookV26(verifier);
                break;
            case 27:
                taskMoverHook = new TaskMoverHookV27(verifier);
                break;
            case 28:
                taskMoverHook = new TaskMoverHookV28(verifier);
                break;
            case 29:
                taskMoverHook = new TaskMoverHookV29(verifier);
                break;
            default:
                taskMoverHook = new TaskMoverHookEmpty(verifier);
                break;

        }
    }
}
