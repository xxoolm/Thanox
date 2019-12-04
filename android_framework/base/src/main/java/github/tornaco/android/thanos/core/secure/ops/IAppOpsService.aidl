package github.tornaco.android.thanos.core.secure.ops;

import android.os.Bundle;

interface IAppOpsService {
    void setMode(int code, int uid, String packageName, int mode);

    void resetAllModes(int reqUserId, String reqPackageName);

    int checkOperation(int code, int uid, String packageName);

    boolean isOpsEnabled();
    void setOpsEnabled(boolean enabled);

    void onStartOp(in IBinder token, int code, int uid, String packageName);
    void onFinishOp(in IBinder token, int code, int uid, String packageName);

    void setOpRemindEnable(int code, boolean enable);
    boolean isOpRemindEnabled(int code);

    void setPkgOpRemindEnable(String pkg, boolean enable);
    boolean isPkgOpRemindEnable(String pkg);
}
