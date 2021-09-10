package competition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import competition.Utils.Constanst;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prf;

    /**
     * 菜单标题
     */
    private final int[] TAB_TITLES = new int[]{R.string.menu_setting, R.string.menu_animal, R.string.menu_search, R.string.menu_me};
    /**
     * 菜单图标
     */
    private final int[] TAB_IMGS = new int[]{R.drawable.tab_main_setting_selector, R.drawable.tab_main_animal_selector, R.drawable.tab_main_search_selector
            , R.drawable.tab_main_mine_selector};

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    /**
     * 页卡适配器
     */
    private PagerAdapter adapter;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prf = PreferenceManager.getDefaultSharedPreferences(this);
        Constanst.token=prf.getString("token","");

        ButterKnife.bind(this);

        // 初始化页卡
        initPager();

        setTabs(tabLayout, getLayoutInflater(), TAB_TITLES, TAB_IMGS);
    }

    /**
     * 设置页卡显示效果
     * @param tabLayout
     * @param inflater
     * @param tabTitlees
     * @param tabImgs
     */
    private void setTabs(TabLayout tabLayout, LayoutInflater inflater, int[] tabTitlees, int[] tabImgs) {
        for (int i = 0; i < tabImgs.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = inflater.inflate(R.layout.item_main_view, null);
            // 使用自定义视图，目的是为了便于修改，也可使用自带的视图
            tab.setCustomView(view);

            TextView tvTitle = (TextView) view.findViewById(R.id.txt_tab);
            tvTitle.setText(tabTitlees[i]);
            ImageView imgTab = (ImageView) view.findViewById(R.id.img_tab);
            imgTab.setImageResource(tabImgs[i]);
            tabLayout.addTab(tab);
        }
    }

    private void initPager() {
        adapter = new MainFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // 关联切换
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 取消平滑切换
                viewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}