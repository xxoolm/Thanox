package github.tornaco.android.thanos.core.power;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class PowerManager {
    private final IPowerManager server;

    @SneakyThrows
    public void reboot() {
        server.reboot();
    }

    @SneakyThrows
    public void softReboot() {
        server.softReboot();
    }

    @SneakyThrows
    public void goToSleep() {
        server.goToSleep();
    }

}
