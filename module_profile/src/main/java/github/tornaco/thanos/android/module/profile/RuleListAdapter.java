package github.tornaco.thanos.android.module.profile;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.core.profile.RuleInfo;
import github.tornaco.thanos.android.module.profile.databinding.ModuleProfileRuleListItemBinding;
import lombok.Getter;
import util.Consumer;

class RuleListAdapter extends RecyclerView.Adapter<RuleListAdapter.VH>
        implements Consumer<List<RuleInfo>> {

    private final List<RuleInfo> ruleInfoList = new ArrayList<>();

    private RuleItemClickListener listener;

    RuleListAdapter(RuleItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ModuleProfileRuleListItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        RuleInfo model = ruleInfoList.get(position);
        holder.itemBinding.setRule(model);
        holder.itemBinding.setListener(listener);
        holder.itemBinding.setIsLastOne(position == getItemCount() - 1);
        holder.itemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return this.ruleInfoList.size();
    }

    @Override
    public void accept(List<RuleInfo> componentReplacements) {
        this.ruleInfoList.clear();
        this.ruleInfoList.addAll(componentReplacements);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        @Getter
        private ModuleProfileRuleListItemBinding itemBinding;

        VH(@NonNull ModuleProfileRuleListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }
}
