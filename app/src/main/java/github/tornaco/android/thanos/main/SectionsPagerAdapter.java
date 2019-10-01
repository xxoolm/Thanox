package github.tornaco.android.thanos.main;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.google.common.collect.ImmutableList;
import github.tornaco.android.thanos.R;

import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{
            R.string.nav_title_boost,
            R.string.nav_title_secure,
            R.string.nav_title_exp};

    private final Context context;

    private final List<NavFragment> pages =
            ImmutableList.of(
                    new BoostFragment(),
                    new SecurityFragment(),
                    new ExpFragment());

    public SectionsPagerAdapter(AppCompatActivity context, FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT);
        this.context = context;
    }

    @Override
    @NonNull
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}