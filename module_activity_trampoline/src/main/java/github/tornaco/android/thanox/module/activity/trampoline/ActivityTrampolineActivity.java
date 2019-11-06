package github.tornaco.android.thanox.module.activity.trampoline;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;

import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanox.module.activity.trampoline.databinding.ModuleActivityTrampolineActivityBinding;

public class ActivityTrampolineActivity extends ThemeActivity {
    private ModuleActivityTrampolineActivityBinding trampolineActivityBinding;

    public static void start(Context context) {
        ActivityUtils.startActivity(context, ActivityTrampolineActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trampolineActivityBinding = ModuleActivityTrampolineActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(trampolineActivityBinding.getRoot());
    }
}
