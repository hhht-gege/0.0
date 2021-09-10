package competition.fragment.Look;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.viewpager.widget.PagerAdapter;

import competition.R;
import competition.Utils.View.CustomViewPager;
import competition.databinding.FragmentLookBinding;
import competition.fragment.Search.Search_near;

public class LookFragment extends Fragment {

    private Button lookTemp;
    private Button lookAnim;
    private Button lookHeart;
    private Button lookKind;

    private PagerAdapter adapter;
    private CustomViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_look, container, false);
        lookTemp = view.findViewById(R.id.look_temp);
        lookAnim = view.findViewById(R.id.look_anim);
        lookHeart = view.findViewById(R.id.look_heart);
        lookKind = view.findViewById(R.id.look_kind);
        viewPager = view.findViewById(R.id.look_viewpager);
        initPager();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initPager() {
        adapter = new LookFragmentAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0,false);

        // 关联切换
        lookTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2,false);
            }
        });
        lookAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Search_near.class);
                startActivity(intent);
            }
        });
        lookHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3,false);
            }
        });
        lookKind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1,false);
            }
        });
    }
}
