package github.tornaco.android.thanos.databinding;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.matrixxun.starry.badgetextview.MaterialBadgeTextView;

import java.util.List;
import java.util.Objects;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.dashboard.DashboardAdapter;
import github.tornaco.android.thanos.dashboard.Tile;
import github.tornaco.android.thanos.main.State;
import github.tornaco.android.thanos.process.ProcessModel;
import util.Consumer;

public class DataBindingAdapters {

    @BindingAdapter("android:activeStatusFabImage")
    public static void setActiveStatusFabImage(FloatingActionButton fab, ObservableField<State> state) {
        if (state.get() == null) {
            return;
        }
        switch (Objects.requireNonNull(state.get())) {
            case Active:
                fab.setImageResource(R.drawable.ic_checkbox_circle_fill);
                fab.setSupportBackgroundTintList(ColorStateList.valueOf(fab.getResources().getColor(R.color.accent)));
                break;
            case InActive:
                fab.setImageResource(R.drawable.ic_forbid_fill);
                fab.setSupportBackgroundTintList(ColorStateList.valueOf(fab.getResources().getColor(R.color.md_red_600)));
                break;
            case RebootNeeded:
                fab.setImageResource(R.drawable.ic_information_fill);
                fab.setSupportBackgroundTintList(ColorStateList.valueOf(fab.getResources().getColor(R.color.md_amber_600)));
                break;
            default:
                break;
        }
    }

    @BindingAdapter("android:boostStatusAppsCount")
    public static void setBoostStatusAppsCount(TextView textView, ObservableInt count) {
        textView.setText(String.valueOf(count.get()));
    }

    @BindingAdapter("android:privacyAppsCount")
    public static void setPrivacyAppsCount(TextView textView, ObservableInt count) {
        textView.setText(String.valueOf(count.get()));
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("android:boostStatusMemPercent")
    public static void setBoostStatusMemPercent(TextView textView, ObservableInt percent) {
        textView.setText(percent.get() + "%");
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("android:boostStatusStoragePercent")
    public static void setBoostStatusStoragePercent(TextView textView, ObservableInt percent) {
        textView.setText(percent.get() + "%");
    }

    @BindingAdapter("android:features")
    public static void setFeatures(RecyclerView recyclerView, ObservableList<Tile> features) {
        DashboardAdapter adapter = (DashboardAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.replaceData(features);
        }
    }

    @BindingAdapter("android:featureIcon")
    public static void setFeatureIcon(ImageView imageView, @DrawableRes int res) {
        imageView.setImageResource(res);
    }

    @BindingAdapter("android:iconThemeColor")
    public static void setIconTint(ImageView imageView, @ColorRes int res) {
        if (res == 0) return;
        imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), res));
    }


    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @BindingAdapter({"android:appsAllowed", "android:appsDisAllowed"})
    public static void setAppInfoList(RecyclerView recyclerView,
                                      ObservableList appsAllowed,
                                      ObservableList appsDisAllowed) {
        Consumer<Pair<List<AppInfo>, List<AppInfo>>> adapter = (Consumer) recyclerView.getAdapter();
        adapter.accept(Pair.create(appsAllowed, appsDisAllowed));
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @BindingAdapter({"android:processModels"})
    public static void setProcessModels(RecyclerView view, List<ProcessModel> processModels) {
        Consumer<List<ProcessModel>> consumer = (Consumer<List<ProcessModel>>) view.getAdapter();
        consumer.accept(processModels);
    }

    @BindingAdapter({"android:thanosStateText"})
    public static void setThanosStateText(TextView view, @NonNull ObservableField<State> state) {
        switch (Objects.requireNonNull(state.get())) {
            case Active:
                view.setText(view.getResources().getString(R.string.status_active));
                break;
            case InActive:
                view.setText(view.getResources().getString(R.string.status_not_active));
                break;
            case RebootNeeded:
                view.setText(view.getResources().getString(R.string.status_need_reboot));
                break;
            default:
                view.setText(view.getResources().getString(R.string.status_active));
                break;
        }
    }

    @BindingAdapter({"android:thanosStateText"})
    public static void setThanosStateText(MaterialBadgeTextView view, @NonNull ObservableField<State> state) {
        setThanosStateText((TextView) view, state);
    }

    @BindingAdapter({"android:thanosStateTint"})
    public static void setThanosStateTint(ImageView view, @NonNull ObservableField<State> state) {
        switch (Objects.requireNonNull(state.get())) {
            case Active:
                setIconTint(view, R.color.md_green_500);
                break;
            case InActive:
                setIconTint(view, R.color.md_red_500);
                break;
            case RebootNeeded:
                setIconTint(view, R.color.md_amber_500);
                break;
        }
    }
}
