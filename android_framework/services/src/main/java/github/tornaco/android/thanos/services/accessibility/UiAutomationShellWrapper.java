package github.tornaco.android.thanos.services.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.UiAutomation;
import android.app.UiAutomationConnection;
import android.os.HandlerThread;

import github.tornaco.android.thanos.core.util.Timber;

@SuppressWarnings("WeakerAccess")
public class UiAutomationShellWrapper {

    private static final String HANDLER_THREAD_NAME = "UiAutomatorHandlerThread";

    private final HandlerThread handlerThread = new HandlerThread(HANDLER_THREAD_NAME);

    private UiAutomation uiAutomation;

    public void connect() {
        if (handlerThread.isAlive()) {
            throw new IllegalStateException("Already connected!");
        }
        handlerThread.start();
        uiAutomation = new UiAutomation(handlerThread.getLooper(),
                new UiAutomationConnection());
        uiAutomation.setOnAccessibilityEventListener(accessibilityEvent -> Timber.d("OnAccessibilityEvent: %s", accessibilityEvent));
        uiAutomation.connect();
    }

    public void disconnect() {
        if (!handlerThread.isAlive()) {
            throw new IllegalStateException("Already disconnected!");
        }
        uiAutomation.disconnect();
        handlerThread.quit();
    }

    public UiAutomation getUiAutomation() {
        return uiAutomation;
    }

    public void setCompressedLayoutHierarchy(boolean compressed) {
        AccessibilityServiceInfo info = uiAutomation.getServiceInfo();
        if (compressed)
            info.flags &= ~AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        else
            info.flags |= AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        uiAutomation.setServiceInfo(info);
    }
}
