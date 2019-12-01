package github.tornaco.android.thanos.services.accessibility;

import android.graphics.Region;
import android.os.Binder;
import android.util.SparseArray;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.IAccessibilityInteractionConnection;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;

import java.util.List;

import de.robv.android.xposed.XposedHelpers;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.util.obs.InvokeTargetProxy;

public class AccessibilityManagerServiceProxy extends InvokeTargetProxy<Object> {
    public static final int ACTIVE_WINDOW_ID = Integer.MAX_VALUE;

    public AccessibilityManagerServiceProxy(Object host) {
        super(host);
    }

    public IAccessibilityInteractionConnection getConnectionLocked(int windowId) {
        SparseArray mGlobalInteractionConnections
                = (SparseArray) XposedHelpers.getObjectField(getHost(), "mGlobalInteractionConnections");
        Timber.v("mGlobalInteractionConnections: %s", mGlobalInteractionConnections);
        if (mGlobalInteractionConnections == null) return null;
        Object wrapper = mGlobalInteractionConnections.get(windowId);
        Timber.v("wrapper: %s", wrapper);
        if (wrapper == null) {
            //  wrapper = getCurrentUserStateLocked().mInteractionConnections.get(windowId);
            Object userState = invokeMethod("getCurrentUserStateLocked");
            Timber.v("userState: %s", userState);
            SparseArray mInteractionConnections = (SparseArray) XposedHelpers.getObjectField(userState, "mInteractionConnections");
            Timber.v("mInteractionConnections: %s", mInteractionConnections);
            if (mInteractionConnections == null) return null;
            wrapper = mInteractionConnections.get(windowId);
            Timber.v("wrapper: %s", wrapper);
        }

        if (wrapper != null) {
            IAccessibilityInteractionConnection connection =
                    (IAccessibilityInteractionConnection) XposedHelpers.getObjectField(wrapper, "mConnection");
            Timber.v("connection: %s", connection);
            return connection;
        }
        return null;
    }

    private int resolveAccessibilityWindowIdLocked(int accessibilityWindowId) {
        if (accessibilityWindowId == ACTIVE_WINDOW_ID) {
            Object mSecurityPolicy = XposedHelpers.getObjectField(getHost(), "mSecurityPolicy");
            Timber.v("mSecurityPolicy: %s", mSecurityPolicy);
            return invokeMethod(mSecurityPolicy, "getActiveWindowId");
        }
        return accessibilityWindowId;
    }

    public void getRootInActiveWindow() {
        findAccessibilityNodeInfoByAccessibilityId(
                ACTIVE_WINDOW_ID,
                AccessibilityNodeInfo.ROOT_NODE_ID,
                0,
                new IAccessibilityInteractionConnectionCallback.Stub() {
                    @Override
                    public void setFindAccessibilityNodeInfoResult(AccessibilityNodeInfo accessibilityNodeInfo, int i) {
                        Timber.w("setFindAccessibilityNodeInfoResult: %s", accessibilityNodeInfo);
                    }

                    @Override
                    public void setFindAccessibilityNodeInfosResult(List<AccessibilityNodeInfo> list, int i) {
                        Timber.w("setFindAccessibilityNodeInfosResult: %s", list);
                    }

                    @Override
                    public void setPerformAccessibilityActionResult(boolean b, int i) {
                        Timber.w("setPerformAccessibilityActionResult: %s %s", b, i);
                    }
                },
                AccessibilityNodeInfo.FLAG_PREFETCH_DESCENDANTS);
    }

    public boolean findAccessibilityNodeInfoByAccessibilityId(
            int accessibilityWindowId, long accessibilityNodeId, int interactionId,
            IAccessibilityInteractionConnectionCallback callback, int flags) {

        IAccessibilityInteractionConnection connection
                = getConnectionLocked(resolveAccessibilityWindowIdLocked(accessibilityWindowId));

        if (connection == null) {
            Timber.e("findAccessibilityNodeInfoByAccessibilityId, connection == null");
            return false;
        }

        Region partialInteractiveRegion = Region.obtain();
        final int interrogatingPid = Binder.getCallingPid();
        try {
            connection.findAccessibilityNodeInfoByAccessibilityId(accessibilityNodeId,
                    partialInteractiveRegion, interactionId, callback, flags,
                    interrogatingPid, Thread.currentThread().getId(), null, null);
            return true;
        } catch (Throwable e) {
            Timber.e(e, "Error connection.findAccessibilityNodeInfoByAccessibilityId@1");
            try {
                invokeMethod(connection, "findAccessibilityNodeInfoByAccessibilityId",
                        accessibilityNodeId,
                        partialInteractiveRegion, interactionId, callback, flags,
                        interrogatingPid, Thread.currentThread().getId(), null);
                return true;
            } catch (Throwable e2) {
                Timber.e(e2, "Error connection.findAccessibilityNodeInfoByAccessibilityId@2");
                return false;
            }
        } finally {
            partialInteractiveRegion.recycle();
        }
    }
}
