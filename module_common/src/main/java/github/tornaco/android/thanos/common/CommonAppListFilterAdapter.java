package github.tornaco.android.thanos.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.module.common.R;
import github.tornaco.android.thanos.module.common.databinding.ItemCommonAppBinding;
import lombok.Getter;
import lombok.Setter;
import util.Consumer;

public class CommonAppListFilterAdapter extends RecyclerView.Adapter<CommonAppListFilterAdapter.VH>
        implements Consumer<List<AppListModel>>,
        FastScrollRecyclerView.SectionedAdapter,
        FastScrollRecyclerView.MeasurableAdapter<CommonAppListFilterAdapter.VH> {

    @Getter
    private final List<AppListModel> listModels = new ArrayList<>();

    @Nullable
    private final AppItemViewClickListener itemViewClickListener;

    @Setter
    private boolean iconCheckable = false;

    public CommonAppListFilterAdapter(
            @Nullable AppItemViewClickListener itemViewClickListener) {
        this.itemViewClickListener = itemViewClickListener;
    }

    public CommonAppListFilterAdapter(
            @Nullable AppItemViewClickListener itemViewClickListener,
            boolean iconCheckable) {
        this.itemViewClickListener = itemViewClickListener;
        this.iconCheckable = iconCheckable;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemCommonAppBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        AppListModel model = listModels.get(position);
        holder.binding.setApp(model.appInfo);
        holder.binding.setIsLastOne(false);
        holder.binding.setListener(appInfo -> {
            if (iconCheckable) {
                appInfo.setSelected(!appInfo.isSelected());
                holder.binding.icon.toggle();
            }
            if (itemViewClickListener != null) itemViewClickListener.onAppItemClick(appInfo);
        });
        holder.binding.setBadge1(model.badge);
        holder.binding.setBadge2(model.badge2);
        if (iconCheckable) {
            holder.binding.icon.setChecked(model.appInfo.isSelected(), false);
        }
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }

    @Override
    public void accept(List<AppListModel> processModels) {
        this.listModels.clear();
        this.listModels.addAll(processModels);
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeHeight(RecyclerView recyclerView, @Nullable VH viewHolder, int viewType) {
        return recyclerView.getResources().getDimensionPixelSize(R.dimen.common_list_item_height);
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        AppListModel model = listModels.get(position);
        String appName = model.appInfo.getAppLabel();
        if (appName == null
                || appName.length() < 1) {
            appName = model.appInfo.getPkgName();
        }
        if (appName == null) {
            return "*";
        }
        return String.valueOf(appName.charAt(0));
    }

    @Getter
    static final class VH extends RecyclerView.ViewHolder {
        private ItemCommonAppBinding binding;

        VH(@NonNull ItemCommonAppBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
