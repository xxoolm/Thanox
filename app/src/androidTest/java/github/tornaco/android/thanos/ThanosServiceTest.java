package github.tornaco.android.thanos;

import android.os.RemoteException;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static github.tornaco.android.thanos.core.app.ThanosManager.from;

@RunWith(AndroidJUnit4.class)
public class ThanosServiceTest {

    @Test
    public void testServicesRegistered() throws RemoteException {
        Assert.assertNotNull("Thanos not registered", from(InstrumentationRegistry.getTargetContext()));
        Assert.assertNotNull("PrefManager not registered", from(InstrumentationRegistry.getTargetContext()).getPrefManager());
        Assert.assertNotNull("ServiceManager not registered", from(InstrumentationRegistry.getTargetContext()).getServiceManager());
        Assert.assertNotNull("PkgManager not registered", from(InstrumentationRegistry.getTargetContext()).getPkgManager());
        Assert.assertNotNull("ActivityStackSupervisor not registered", from(InstrumentationRegistry.getTargetContext()).getActivityStackSupervisor());
        Assert.assertNotNull("AppOpsManager not registered", from(InstrumentationRegistry.getTargetContext()).getAppOpsManager());
        Assert.assertNotNull("PushManager not registered", from(InstrumentationRegistry.getTargetContext()).getPushManager());
    }
}
