package github.tornaco.android.thanos.services.profile.handle;

import android.content.Intent;

public interface ILauncher {

    boolean launchProcessForPackage(String pkgName);

    boolean launchActivity(Intent intent);

    boolean launchMainActivityForPackage(String pkgName);

    Intent getLaunchIntentForPackage(String pkgName);
}
