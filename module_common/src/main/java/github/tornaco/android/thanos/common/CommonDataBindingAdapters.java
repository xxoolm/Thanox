package github.tornaco.android.thanos.common;

import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.Switch;
import androidx.annotation.ColorRes;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.GenericTransitionOptions;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.module.common.R;
import github.tornaco.android.thanos.util.GlideApp;
import github.tornaco.java.common.util.Consumer;

import java.util.List;

public class CommonDataBindingAdapters {

    @BindingAdapter("android:iconThemeColor")
    public static void setIconTint(ImageView imageView, @ColorRes int res) {
        if (res == 0) return;
        imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), res));
    }

    @BindingAdapter("android:appIcon")
    public static void setAppIcon(ImageView imageView, AppInfo appInfo) {
        GlideApp.with(imageView)
                .load(appInfo)
                .error(R.mipmap.ic_fallback_app_icon)
                .fallback(R.mipmap.ic_fallback_app_icon)
                .transition(GenericTransitionOptions.with(R.anim.grow_fade_in))
                .into(imageView);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @BindingAdapter({"android:listModels"})
    public static void setProcessModels(RecyclerView view, List<AppListModel> models) {
        Consumer<List<AppListModel>> consumer = (Consumer<List<AppListModel>>) view.getAdapter();
        consumer.accept(models);
    }

    @BindingAdapter({"android:switchApp", "android:switchListener"})
    public static void setSwitchAppAndListener(Switch view, AppInfo appInfo,
                                               final AppItemViewActionListener listener) {
        view.setOnClickListener((b) -> listener.onAppItemSwitchStateChange(appInfo, ((Checkable) b).isChecked()));
    }

    @BindingAdapter({"android:switchApp", "android:switchListener"})
    public static void setSwitchAppAndListener(SwitchCompat view, AppInfo appInfo,
                                               final AppItemViewActionListener listener) {
        view.setOnClickListener((b) -> listener.onAppItemSwitchStateChange(appInfo, ((Checkable) b).isChecked()));
    }
}
