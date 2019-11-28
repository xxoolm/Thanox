package github.tornaco.android.thanos.services.profile.handle;

public interface IUI {

    void showShortToast(String msg);

    void showLongToast(String msg);

    void showDialog(String title, String msg, String yes);

    void showNotification(String title, String msg, boolean headsUp);
}
