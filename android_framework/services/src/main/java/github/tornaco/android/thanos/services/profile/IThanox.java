package github.tornaco.android.thanos.services.profile;

interface IThanox {

    boolean killPkg(String pkgName);

    boolean killApp(String appLabel);

    void showShortToast(String msg);

    void showLongToast(String msg);

    String getFrontPkg();

    String getFrontComponentName();

}
