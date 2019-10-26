package github.tornaco.android.thanos.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import github.tornaco.android.thanos.module.common.databinding.ItemCommonAppBinding;
import github.tornaco.java.common.util.Consumer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

class CommonAppListFilterAdapter extends RecyclerView.Adapter<CommonAppListFilterAdapter.VH>
        implements Consumer<List<AppListModel>> {

    private final List<AppListModel> processModels = new ArrayList<>();

    @Nullable
    private final AppItemViewClickListener itemViewClickListener;

    CommonAppListFilterAdapter(
            @Nullable AppItemViewClickListener itemViewClickListener) {
        this.itemViewClickListener = itemViewClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemCommonAppBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        AppListModel model = processModels.get(position);
        holder.binding.setApp(model.appInfo);
        holder.binding.setIsLastOne(false);
        holder.binding.setListener(itemViewClickListener);
        holder.binding.setBadge1(model.badge);
        holder.binding.setBadge2(model.badge2);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return processModels.size();
    }

    @Override
    public void accept(List<AppListModel> processModels) {
        this.processModels.clear();
        this.processModels.addAll(processModels);
        notifyDataSetChanged();
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
