package github.tornaco.thanos.android.ops.ops;

import androidx.annotation.NonNull;
import github.tornaco.thanos.android.ops.model.Op;

public interface OpItemClickListener {
    void onOpItemClick(@NonNull Op op);
}
