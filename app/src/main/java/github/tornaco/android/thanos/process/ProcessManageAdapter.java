package github.tornaco.android.thanos.process;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.common.AppItemViewClickListener;
import github.tornaco.android.thanos.common.AppItemViewLongClickListener;
import github.tornaco.android.thanos.databinding.ItemProcessManageBinding;
import lombok.Getter;
import util.Consumer;

public class ProcessManageAdapter extends RecyclerView.Adapter<ProcessManageAdapter.VH>
        implements Consumer<List<ProcessModel>> {

    private final List<ProcessModel> processModels = new ArrayList<>();
    private final AppItemViewClickListener listener;
    private final AppItemViewLongClickListener longClickListener;

    ProcessManageAdapter(AppItemViewClickListener listener,
                         AppItemViewLongClickListener longClickListener) {
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemProcessManageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ProcessModel model = processModels.get(position);
        holder.binding.setProcess(model);
        holder.binding.setIsLastOne(false);
        holder.binding.setBadge1(model.getBadge1());
        holder.binding.setBadge2(model.getBadge2());
        holder.binding.setListener(view -> listener.onAppItemClick(model.getAppInfo()));
        holder.binding.appItemRoot.setOnLongClickListener(v -> {
            longClickListener.onAppItemLongClick(model.getAppInfo());
            return true;
        });
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return processModels.size();
    }

    @Override
    public void accept(List<ProcessModel> processModels) {
        this.processModels.clear();
        this.processModels.addAll(processModels);
        notifyDataSetChanged();
    }

    @Getter
    static final class VH extends RecyclerView.ViewHolder {
        private ItemProcessManageBinding binding;

        VH(@NonNull ItemProcessManageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
