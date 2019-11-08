package github.tornaco.android.thanos.services.xposed.hooks;

import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.xposed.annotation.XposedHook;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._23;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

// Listen for app process added.
@XposedHook(targetSdkVersion = {_23})
@Beta
public class AMSStartProcessLockedRegistryM extends AMSStartProcessLockedRegistryO {
}
