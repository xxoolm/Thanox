package github.tornaco.android.thanos.core.secure.ops;

import android.os.Bundle;

interface IAppOpsService {
    // Remaining methods are only used in Java.
    int checkPackage(int uid, String packageName);

    List<PackageOps> getPackagesForOps(in int[] ops);
    List<PackageOps> getOpsForPackage(int uid, String packageName, in int[] ops);
    List<PackageOps> getUidOps(int uid, in int[] ops);

    void setUidMode(int code, int uid, int mode);
    void setMode(int code, int uid, String packageName, int mode);

    void resetAllModes(int reqUserId, String reqPackageName);

    int checkOperation(int code, int uid, String packageName);

    void setUserRestrictions(in Bundle restrictions, IBinder token, int userHandle);
    void setUserRestriction(int code, boolean restricted, IBinder token, int userHandle, in String[] exceptionPackages);

    void removeUser(int userHandle);

    boolean isOpsEnabled();
    void setOpsEnabled(boolean enabled);

    void onStartOp(in IBinder token, int code, int uid, String packageName);
    void onFinishOp(in IBinder token, int code, int uid, String packageName);

    void setOpRemindEnable(int code, boolean enable);
    boolean isOpRemindEnabled(int code);

    void setPkgOpRemindEnable(String pkg, boolean enable);
    boolean isPkgOpRemindEnable(String pkg);
}
