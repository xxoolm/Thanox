package github.tornaco.android.thanos.services.app.view;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.core.Res;
import github.tornaco.android.thanos.core.annotation.NonNull;
import github.tornaco.android.thanos.core.app.AppResources;
import github.tornaco.android.thanos.core.util.ClipboardUtils;
import lombok.Getter;

@Getter
public class CurrentComponentViewCallback implements CurrentComponentView.Callback {

    private Context context;
    private Handler handler;
    private AppResources appResources;

    public CurrentComponentViewCallback(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.appResources = new AppResources(context, BuildProp.THANOS_APP_PKG_NAME);
    }

    @Override
    public void onSingleTap(String text) {
        ClipboardUtils.copyToClipboard(getContext(), "current", text);
        showCopiedToClipboardToast();
    }

    @Override
    public void onDoubleTap() {

    }

    @Override
    public void onSwipeDirection(@NonNull CurrentComponentView.SwipeDirection direction) {

    }

    @Override
    public void onSwipeDirectionLargeDistance(@NonNull CurrentComponentView.SwipeDirection direction) {

    }

    @Override
    public void onLongPress() {

    }

    private void showCopiedToClipboardToast() {
        handler.post(() ->
                Toast
                        .makeText(
                                context,
                                appResources.getString(Res.Strings.STRING_SERVICE_TOAST_CURRENT_COMPONENT_COPIED_TO_CLIPBOARD),
                                Toast.LENGTH_SHORT)
                        .show()
        );
    }
}
