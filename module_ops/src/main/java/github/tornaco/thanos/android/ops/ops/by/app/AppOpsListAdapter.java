package github.tornaco.thanos.android.ops.ops.by.app;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.widget.section.SectioningAdapter;
import github.tornaco.java.common.util.Consumer;
import github.tornaco.thanos.android.ops.databinding.ModuleOpsItemFooterBinding;
import github.tornaco.thanos.android.ops.databinding.ModuleOpsItemHeaderBinding;
import github.tornaco.thanos.android.ops.databinding.ModuleOpsItemOpsCheckableBinding;
import github.tornaco.thanos.android.ops.model.Op;
import github.tornaco.thanos.android.ops.model.OpGroup;
import lombok.Getter;

public class AppOpsListAdapter extends SectioningAdapter implements Consumer<List<OpGroup>> {

    private final OpItemSwitchChangeListener itemSwitchChangeListener;
    private final AppInfo appInfo;

    private final List<OpGroup> opGroups = new ArrayList<>();

    AppOpsListAdapter(@NonNull OpItemSwitchChangeListener itemSwitchChangeListener,
                      @NonNull AppInfo appInfo) {
        this.itemSwitchChangeListener = itemSwitchChangeListener;
        this.appInfo = appInfo;
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemUserType) {
        return new IVH(ModuleOpsItemOpsCheckableBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerUserType) {
        return new HVH(ModuleOpsItemHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public FooterViewHolder onCreateFooterViewHolder(ViewGroup parent, int footerUserType) {
        return new FVH(ModuleOpsItemFooterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int sectionIndex, int headerUserType) {
        super.onBindHeaderViewHolder(viewHolder, sectionIndex, headerUserType);
        HVH hvh = (HVH) viewHolder;
        hvh.binding.setGroup(opGroups.get(sectionIndex));
        hvh.binding.executePendingBindings();
    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemUserType) {
        super.onBindItemViewHolder(viewHolder, sectionIndex, itemIndex, itemUserType);
        IVH ivh = (IVH) viewHolder;
        OpGroup opGroup = opGroups.get(sectionIndex);
        Op op = opGroup.getOpList().get(itemIndex);
        ivh.binding.setOp(op);
        ivh.binding.setClickListener(clickedOp -> ivh.binding.itemSwitch.performClick());
        ivh.binding.executePendingBindings();
        ivh.binding.setSwitchListener(itemSwitchChangeListener);
        ivh.binding.setApp(appInfo);
    }

    @Override
    public void onBindFooterViewHolder(FooterViewHolder viewHolder, int sectionIndex, int footerUserType) {
        super.onBindFooterViewHolder(viewHolder, sectionIndex, footerUserType);
    }

    @Override
    public void accept(List<OpGroup> opGroups) {
        this.opGroups.clear();
        this.opGroups.addAll(opGroups);
        notifyAllSectionsDataSetChanged();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return opGroups.get(sectionIndex).getOpList().size();
    }

    @Override
    public int getNumberOfSections() {
        return opGroups.size();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public boolean doesSectionHaveFooter(int sectionIndex) {
        return true;
    }

    @Getter
    final static class HVH extends HeaderViewHolder {
        private ModuleOpsItemHeaderBinding binding;

        HVH(ModuleOpsItemHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Getter
    final static class IVH extends ItemViewHolder {
        private ModuleOpsItemOpsCheckableBinding binding;

        IVH(ModuleOpsItemOpsCheckableBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Getter
    final static class FVH extends FooterViewHolder {
        private ModuleOpsItemFooterBinding binding;

        FVH(ModuleOpsItemFooterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
