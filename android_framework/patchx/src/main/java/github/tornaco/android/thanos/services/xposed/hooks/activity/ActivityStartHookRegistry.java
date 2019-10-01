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
public class ActivityStartHookRegistry implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    public ActivityStartHookRegistry() {
        IActivityStackSupervisor verifier = BootStrap.THANOS_X.getActivityStackSupervisor();
        int sdkVersion = Build.VERSION.SDK_INT;
        switch (sdkVersion) {
            case 21:
                activityStartHookImpl = new ActivityStartHookV21(verifier);
                break;
            case 22:
                activityStartHookImpl = new ActivityStartHookV22(verifier);
                break;
            case 23:
                activityStartHookImpl = new ActivityStartHookV23(verifier);
                break;
            case 24:
                activityStartHookImpl = new ActivityStartHookV24(verifier);
                break;
            case 25:
                activityStartHookImpl = new ActivityStartHookV25(verifier);
                break;
            case 26:
                activityStartHookImpl = new ActivityStartHookV26(verifier);
                break;
            case 27:
                activityStartHookImpl = new ActivityStartHookV27(verifier);
                break;
            case 28:
                activityStartHookImpl = new ActivityStartHookV28(verifier);
                break;
            case 29:
                activityStartHookImpl = new ActivityStartHookV29(verifier);
                break;
            default:
                activityStartHookImpl = new ActivityStartHookEmpty();
                break;
        }
    }

    @Delegate
    private IXposedHookLoadPackage activityStartHookImpl;

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        // Noop.
    }
}
