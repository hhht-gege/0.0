package competition.fragment.Look;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import competition.fragment.Anim.AnimFragment;
import competition.fragment.Me.MeFragment;
import competition.fragment.Setting.SetFragment;

public class LookFragmentAdapter extends FragmentPagerAdapter {

    public LookFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        switch (i) {
            case 0:
                fragment = new NothingFragment();
                break;
            case 1:
                fragment = new KindFragment();
                break;
            case 2:
                fragment = new TempFragment();
                break;
            case 3:
                fragment = new HeartFragment();
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
