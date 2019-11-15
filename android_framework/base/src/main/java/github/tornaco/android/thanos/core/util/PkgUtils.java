package github.tornaco.android.thanos.core.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Process;
import android.os.UserHandle;

import github.tornaco.android.thanos.core.annotation.NonNull;
import util.ObjectsUtils;

public class PkgUtils {
    private PkgUtils() {
    }

    public static CharSequence loadNameByPkgName(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();

        try {
            ApplicationInfo info = pm.getApplicationInfo(pkg, 8192);
            return info == null ? null : info.loadLabel(pm);
        } catch (PackageManager.NameNotFoundException var4) {
            return null;
        }
    }

    public static boolean isPkgInstalled(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                info = pm.getApplicationInfo(pkg, PackageManager.MATCH_UNINSTALLED_PACKAGES);
            } else {
                info = pm.getApplicationInfo(pkg, PackageManager.GET_UNINSTALLED_PACKAGES);
            }
            return info != null;
        } catch (Throwable e) {
            return false;
        }
    }

    public static String getPathForPackage(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                applicationInfo = pm.getApplicationInfo(pkg, PackageManager.MATCH_UNINSTALLED_PACKAGES);
            } else {
                applicationInfo = pm.getApplicationInfo(pkg, PackageManager.GET_UNINSTALLED_PACKAGES);
            }
            return applicationInfo.publicSourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static boolean isHomeIntent(Intent intent) {
        return intent != null && intent.hasCategory(Intent.CATEGORY_HOME);
    }

    public static boolean isMainIntent(Intent intent) {
        return intent != null
                && Intent.ACTION_MAIN.equals(intent.getAction())
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER);
    }

    public static String packageNameOf(Intent intent) {
        if (intent == null) {
            return null;
        }
        String packageName = intent.getPackage();
        if (packageName != null) {
            return packageName;
        }
        if (intent.getComponent() == null) {
            return null;
        }
        return intent.getComponent().getPackageName();
    }

    public static boolean isLauncherApp(Context context, String packageName) {
        PackageManager pkgManager = context.getPackageManager();
        Intent mainIntent = new Intent("android.intent.action.MAIN", null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        mainIntent.setPackage(packageName);
        ResolveInfo ri = pkgManager.resolveActivity(mainIntent, 0);
        return !(ri == null || ri.activityInfo == null);
    }

    public static boolean isHomeApp(Context context, String packageName) {
        PackageManager pkgManager = context.getPackageManager();
        Intent homeIntent = new Intent("android.intent.action.MAIN");
        homeIntent.addCategory("android.intent.category.HOME");
        homeIntent.setPackage(packageName);
        ResolveInfo ri = pkgManager.resolveActivity(homeIntent, 0);
        return !(ri == null || ri.activityInfo == null);
    }

    public static boolean isInputMethodApp(Context context, String pkgName) {

        PackageManager pm = context.getPackageManager();
        boolean isIme = false;
        PackageInfo pkgInfo;
        try {
            pkgInfo = pm.getPackageInfo(pkgName, PackageManager.GET_SERVICES);
            if (pkgInfo != null) {
                ServiceInfo[] servicesInfos = pkgInfo.services;
                if (null != servicesInfos) {
                    for (ServiceInfo sInfo : servicesInfos) {
                        if (null != sInfo.permission && sInfo.permission.equals(Manifest.permission.BIND_INPUT_METHOD)) {
                            isIme = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return isIme;
    }

    // Fix dead lock issue when call this along with xxx framework patch.
    public static boolean isDefaultSmsApp(Context context, String packageName) {
        // String def = Telephony.Sms.getDefaultSmsPackage(context);
        // return def != null && def.equals(packageName);
        return false;
    }

    // Check if uid is system, shell or phone.
    public static boolean isSystemOrPhoneOrShell(int uid) {
        return uid <= 2000
                || (uid > UserHandle.PER_USER_RANGE && (uid % UserHandle.PER_USER_RANGE <= 2000));
    }

    public static boolean isSystemCall(int uid) {
        return uid == 1000
                || (uid > UserHandle.PER_USER_RANGE && (uid % UserHandle.PER_USER_RANGE == 1000));
    }

    public static boolean isSharedUserIdSystem(String sharedUid) {
        return ObjectsUtils.equals(sharedUid, github.tornaco.android.thanos.core.pm.PackageManager.sharedUserIdOfSystem());
    }

    public static boolean isSharedUserIdMedia(String sharedUid) {
        return ObjectsUtils.equals(sharedUid, github.tornaco.android.thanos.core.pm.PackageManager.sharedUserIdOfMedia());
    }

    public static boolean isSharedUserIdPhone(String sharedUid) {
        return ObjectsUtils.equals(sharedUid, github.tornaco.android.thanos.core.pm.PackageManager.sharedUserIdOfPhone());
    }

    public static String resolvePackageName(int uid, String packageName) {
        if (uid == Process.ROOT_UID) {
            return "root";
        } else if (uid == Process.SHELL_UID) {
            return "com.android.shell";
        } else if (uid == Process.MEDIA_UID) {
            return "media";
        } else if (uid == Process.AUDIOSERVER_UID) {
            return "audioserver";
        } else if (uid == Process.CAMERASERVER_UID) {
            return "cameraserver";
        } else if (uid == Process.SYSTEM_UID && packageName == null) {
            return "android";
        }
        return packageName;
    }

    public static int resolveUid(String packageName) {
        if (packageName == null) {
            return -1;
        }
        switch (packageName) {
            case "root":
                return Process.ROOT_UID;
            case "shell":
                return Process.SHELL_UID;
            case "media":
                return Process.MEDIA_UID;
            case "audioserver":
                return Process.AUDIOSERVER_UID;
            case "cameraserver":
                return Process.CAMERASERVER_UID;
        }
        return -1;
    }

    @NonNull
    public static String[] getAllDeclaredPermissions(Context context, String packageName) {
        String[] permissions = new String[0];
        if (packageName == null) return permissions;
        PackageInfo packageInfo = getPkgInfo(context, packageName);
        if (packageInfo != null) {
            if (packageInfo.requestedPermissions != null) {
                permissions = packageInfo.requestedPermissions;
            }
        }
        return permissions;
    }

    private static PackageInfo getPkgInfo(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }
}
