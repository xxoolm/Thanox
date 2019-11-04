package github.tornaco.android.thanos;

import android.os.RemoteException;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.push.PushChannel;
import github.tornaco.android.thanos.core.push.PushManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static github.tornaco.android.thanos.core.app.ThanosManager.from;

@RunWith(AndroidJUnit4.class)
public class PushManagerTest {

    @Test
    public void testRepo() throws RemoteException {
        Assert.assertNotNull("PushManager not registered",
                from(InstrumentationRegistry.getTargetContext())
                        .getPushManager());

        PushManager pushManager = ThanosManager.from(InstrumentationRegistry.getTargetContext())
                .getPushManager();

        pushManager.registerChannel(PushChannel.FCM_GCM);
        pushManager.registerChannel(PushChannel.FCM_GCM);
        pushManager.registerChannel(PushChannel.MIPUSH);
        pushManager.registerChannel(PushChannel.MIPUSH);

        pushManager.unRegisterChannel(PushChannel.MIPUSH);

        pushManager.registerChannel(new PushChannel(new String[]{"Noop"}, "test:pc1", UUID.randomUUID().toString()));
    }
}
