package github.tornaco.android.thanox.module.activity.trampoline;

import android.content.Context;

import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;

public class ActivityTrampolineActivity extends ThemeActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, ActivityTrampolineActivity.class);
    }

}
