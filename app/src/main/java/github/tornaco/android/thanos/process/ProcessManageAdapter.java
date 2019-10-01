package github.tornaco.android.thanos.process;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import github.tornaco.android.thanos.databinding.ItemProcessManageBinding;
import github.tornaco.java.common.util.Consumer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ProcessManageAdapter extends RecyclerView.Adapter<ProcessManageAdapter.VH>
        implements Consumer<List<ProcessModel>> {

    private final List<ProcessModel> processModels = new ArrayList<>();

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
        holder.binding.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
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
