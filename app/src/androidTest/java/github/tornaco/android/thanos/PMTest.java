package github.tornaco.android.thanos;

import android.os.RemoteException;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static github.tornaco.android.thanos.core.app.ThanosManager.from;

@RunWith(AndroidJUnit4.class)
public class PMTest {

    @Test
    public void testBasicApi() throws RemoteException {
        Assert.assertNotNull("PM not registered",
                from(InstrumentationRegistry.getTargetContext())
                        .getPkgManager());

        Assert.assertNotNull(from(InstrumentationRegistry.getTargetContext())
                .getPkgManager().getInstalledPkgs(0));

        Assert.assertNotNull(from(InstrumentationRegistry.getTargetContext())
                .getPkgManager().getWhiteListPkgs());
    }
}
