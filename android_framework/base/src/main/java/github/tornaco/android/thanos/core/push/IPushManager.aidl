package github.tornaco.android.thanos.core.push;

interface IPushManager {
    void registerChannel(in PushChannel channel);
    void unRegisterChannel(in PushChannel channel);
}