package github.tornaco.android.thanos.main;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import github.tornaco.android.thanos.core.util.Timber;

class NavFragment extends Fragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((FragmentAttachListener) context).onFragmentAttach(this);
        Timber.d("onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.d("onActivityCreated");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy");
    }

    String getNavTitle() {
        return null;
    }

    interface FragmentAttachListener {
        void onFragmentAttach(NavFragment fragment);
    }
}
