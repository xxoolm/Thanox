package github.tornaco.android.thanos.core.power;

interface IPowerManager {
    void reboot();
    void softReboot();

    void goToSleep();
}