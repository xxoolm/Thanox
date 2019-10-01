package github.tornaco.android.thanos.app.donate;

import android.content.Context;
import android.preference.PreferenceManager;
import github.tornaco.android.thanos.BuildProp;

import java.util.Observable;

/**
 * Created by guohao4 on 2017/10/19.
 * Email: Tornaco@163.com
 */

public class DonateSettings extends Observable {
    private static final String DONATED = "donated";

    private static DonateSettings sMe = new DonateSettings();

    private DonateSettings() {
    }

    public static DonateSettings get() {
        return sMe;
    }

    public static boolean isDonated(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(DONATED, BuildProp.DOG_FOOD);
    }

    static void setDonated(Context context, boolean donated) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putBoolean(DONATED, donated).apply();
    }
}
