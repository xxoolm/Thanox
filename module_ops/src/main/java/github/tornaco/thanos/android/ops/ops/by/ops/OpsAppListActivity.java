package github.tornaco.thanos.android.ops.ops.by.ops;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.common.OnAppItemSelectStateChangeListener;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.SwitchBar;
import github.tornaco.thanos.android.ops.model.Op;
import github.tornaco.thanos.android.ops.ops.repo.OpsPackageLoader;

public class OpsAppListActivity extends CommonFuncToggleAppListFilterActivity {
    private Op op;

    public static void start(Context context, Op op) {
        Bundle data = new Bundle();
        data.putParcelable("op", op);
        ActivityUtils.startActivity(context, OpsAppListActivity.class, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.op = getIntent().getParcelableExtra("op");
        if (op == null) {
            finish();
        }

        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    protected String getTitleString() {
        return op.getTitle();
    }

    @NonNull
    @Override
    protected OnAppItemSelectStateChangeListener onCreateAppItemSelectStateChangeListener() {
        return (appInfo, selected) -> ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager -> thanosManager.getAppOpsManager()
                        .setMode(op.getCode(), appInfo.getUid(), appInfo.getPkgName(),
                                selected ? AppOpsManager.MODE_ALLOWED : AppOpsManager.MODE_IGNORED));
    }

    @NonNull
    @Override
    protected CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return new OpsPackageLoader(getApplicationContext(), op);
    }

    @Override
    protected void onSetupSwitchBar(SwitchBar switchBar) {
        super.onSetupSwitchBar(switchBar);
        switchBar.hide();
    }
}
