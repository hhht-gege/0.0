package competition;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import competition.fragment.Anim.AnimFragment;
import competition.fragment.Look.LookFragment;
import competition.fragment.Me.MeFragment;
import competition.fragment.Search.SearFragment;
import competition.fragment.Setting.SetFragment;

public class MainFragmentAdapter extends FragmentPagerAdapter {
    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        switch (i) {
            case 0:
                fragment = new SetFragment();
                break;
            case 1:
                fragment = new AnimFragment();
                break;
            case 2:
                fragment = new LookFragment();
                break;
            case 3:
                fragment = new MeFragment();
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
