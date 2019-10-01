package github.tornaco.android.thanos.services.os;

import android.os.IBinder;
import github.tornaco.android.thanos.core.os.IServiceManager;
import github.tornaco.android.thanos.core.util.Noop;
import github.tornaco.android.thanos.services.S;
import github.tornaco.android.thanos.services.SystemService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("WeakerAccess")
public class ServiceManagerService extends SystemService implements IServiceManager {
    private final S s;

    private final ConcurrentMap<String, IBinder> serviceCache = new ConcurrentHashMap<>();

    public ServiceManagerService(S s) {
        this.s = s;
    }

    public boolean hasService(String name) {
        return serviceCache.containsKey(name);
    }

    public void addService(String name, IBinder binder) {
        if (hasService(name)) {
            throw new SecurityException("Service dup.");
        }
        serviceCache.putIfAbsent(name, binder);
    }

    public IBinder getService(String name) {
        return serviceCache.get(name);
    }

    @Override
    protected String serviceName() {
        return "ServiceManager";
    }

    @Override
    public IBinder asBinder() {
        return Noop.notSupported();
    }
}
