package github.tornaco.thanos.android.module.profile;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import github.tornaco.android.thanos.core.profile.RuleInfo;
import util.Consumer;

public class DataBindingAdapters {

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @BindingAdapter("android:ruleInfoList")
    public static void setRuleInfoList(RecyclerView view, List<RuleInfo> models) {
        Consumer<List<RuleInfo>> consumer = (Consumer<List<RuleInfo>>) view.getAdapter();
        consumer.accept(models);
    }
}
