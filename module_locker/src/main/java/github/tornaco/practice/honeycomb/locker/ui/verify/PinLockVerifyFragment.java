package github.tornaco.practice.honeycomb.locker.ui.verify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import github.tornaco.practice.honeycomb.locker.databinding.ModuleLockerPinLockVerifyFragmentBinding;

import java.util.Objects;

public class PinLockVerifyFragment extends Fragment {

    private VerifyViewModel verifyViewModel;
    private ModuleLockerPinLockVerifyFragmentBinding pinLockVerifyFragmentBinding;

    public static PinLockVerifyFragment newInstance() {
        return new PinLockVerifyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        pinLockVerifyFragmentBinding = ModuleLockerPinLockVerifyFragmentBinding.inflate(inflater, container, false);
        verifyViewModel = VerifyActivity.obtainViewModel(Objects.requireNonNull(getActivity()));
        pinLockVerifyFragmentBinding.setViewmodel(verifyViewModel);
        setHasOptionsMenu(true);
        return pinLockVerifyFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        verifyViewModel.verified.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Objects.requireNonNull(getActivity()).finish();
            }
        });
        verifyViewModel.start();
    }
}
