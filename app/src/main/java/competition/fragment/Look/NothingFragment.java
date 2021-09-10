package competition.fragment.Look;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import competition.databinding.FragmentLookBinding;
import competition.databinding.FragmentLookNothingBinding;

public class NothingFragment extends Fragment {
    private FragmentLookNothingBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      binding =FragmentLookNothingBinding.inflate(inflater,container,false);
      return binding.getRoot();
    }

}
