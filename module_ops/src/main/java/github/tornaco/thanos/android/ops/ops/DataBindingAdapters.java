package github.tornaco.thanos.android.ops.ops;

import android.widget.Checkable;
import android.widget.ImageView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.thanos.android.ops.model.Op;
import github.tornaco.thanos.android.ops.model.OpGroup;
import github.tornaco.thanos.android.ops.ops.by.app.OpItemSwitchChangeListener;
import util.Consumer;

public class DataBindingAdapters {

    @BindingAdapter("android:opIcon")
    public static void setAppIcon(ImageView imageView, Op op) {
        imageView.setImageResource(op.getIconRes());
    }

    @BindingAdapter("android:opGroups")
    public static void setOpGroups(RecyclerView recyclerView, List<OpGroup> groups) {
        @SuppressWarnings("unchecked")
        Consumer<List<OpGroup>> consumer = (Consumer<List<OpGroup>>) recyclerView.getAdapter();
        Objects.requireNonNull(consumer).accept(groups);
    }

    @BindingAdapter({"android:switchOp", "android:switchApp", "android:switchListener"})
    public static void setSwitchAppAndListener(SwitchCompat view,
                                               Op op,
                                               AppInfo appInfo,
                                               final OpItemSwitchChangeListener listener) {
        view.setOnClickListener((b) -> listener.onOpItemSwitchChanged(op, appInfo, ((Checkable) b).isChecked()));
    }
}
