package github.tornaco.android.thanos.services.xposed.hooks;

import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.xposed.annotation.XposedHook;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._24;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._25;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

// Listen for app process added.
@XposedHook(targetSdkVersion = {_24, _25})
@Beta
public class AMSStartProcessLockedRegistryN extends AMSStartProcessLockedRegistryO {
}
