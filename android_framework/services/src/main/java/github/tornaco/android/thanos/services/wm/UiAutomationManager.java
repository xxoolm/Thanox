package github.tornaco.android.thanos.services.wm;

import android.accessibilityservice.IAccessibilityServiceClient;
import android.accessibilityservice.IAccessibilityServiceConnection;
import android.annotation.SuppressLint;
import android.app.UiAutomation;
import android.app.UiAutomationConnection;
import android.graphics.Region;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityInteractionClient;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.concurrent.atomic.AtomicInteger;

import de.robv.android.xposed.XposedHelpers;
import github.tornaco.android.thanos.core.util.Timber;

class UiAutomationManager extends IAccessibilityServiceClient.Stub {

    private final AtomicInteger connectionId = new AtomicInteger(AccessibilityInteractionClient.NO_ID);
    private UiAutomationConnection connection;

    @SuppressLint("InlinedApi")
    void connect() {
        Timber.v("connect");
        if (connectionId.get() != AccessibilityInteractionClient.NO_ID) {
            // Already has a connection.
            return;
        }
        connection = new UiAutomationConnection();
        connection.connect(this, UiAutomation.FLAG_DONT_SUPPRESS_ACCESSIBILITY_SERVICES);
    }

    void disconnect() {
        Timber.v("disconnect");
        if (connectionId.get() != AccessibilityInteractionClient.NO_ID) {
            // Already has a connection.
            return;
        }
        if (connection == null) {
            return;
        }
        connection.disconnect();
        connectionId.set(AccessibilityInteractionClient.NO_ID);
    }

    public AccessibilityNodeInfo getRootInActiveWindow() {
        if (connectionId.get() == AccessibilityInteractionClient.NO_ID) {
            Timber.e("getRootInActiveWindow, not connected...");
            return null;
        }
        return AccessibilityInteractionClient.getInstance().getRootInActiveWindow(connectionId.get());
    }

    @Override
    public void init(IAccessibilityServiceConnection connection, int id, IBinder iBinder) {
        if (connection != null) {
            try {
                connectionId.set(id);
                XposedHelpers.callStaticMethod(AccessibilityInteractionClient.class,
                        "addConnection",
                        id, connection);
            } catch (Throwable e) {
                Timber.e(e);
                try {
                    AccessibilityInteractionClient.getInstance().addConnection(id, connection);
                } catch (Throwable e2) {
                    Timber.e(e2);
                }
            }
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent, boolean b) {

    }

    @Override
    public void onInterrupt() throws RemoteException {

    }

    @Override
    public void onGesture(int i) throws RemoteException {

    }

    @Override
    public void clearAccessibilityCache() throws RemoteException {

    }

    @Override
    public void onKeyEvent(KeyEvent keyEvent, int i) throws RemoteException {

    }

    @Override
    public void onMagnificationChanged(Region region, float v, float v1, float v2) throws RemoteException {

    }

    @Override
    public void onSoftKeyboardShowModeChanged(int i) throws RemoteException {

    }

    @Override
    public void onPerformGestureResult(int i, boolean b) throws RemoteException {

    }

    @Override
    public void onFingerprintCapturingGesturesChanged(boolean b) throws RemoteException {

    }

    @Override
    public void onFingerprintGesture(int i) throws RemoteException {

    }

    @Override
    public void onAccessibilityButtonClicked() throws RemoteException {

    }

    @Override
    public void onAccessibilityButtonAvailabilityChanged(boolean b) throws RemoteException {

    }
}
