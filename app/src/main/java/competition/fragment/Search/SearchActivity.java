package competition.fragment.Search;


import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import competition.R;

public class SearchActivity extends AppCompatActivity {
    private final static String TAG="SearchActivity";

    private SearchView mSearchView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initData();
        setListener();
    }

    private void initView(){
        mSearchView=findViewById(R.id.search);

    }

    private void initData(){
        mSearchView.setIconifiedByDefault(true);//设置搜索图标是否显示在搜索框内
        mSearchView.setIconified(false);
        mSearchView.requestFocusFromTouch();
       // setUnderLinetransparent(mSearchView);
    }

    private void setListener(){

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG,"onclick");
                return false;
            }

            //当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG,"ok!!!");
                return false;
            }
        });
    }

//    /**设置SearchView下划线透明**/
//    private void setUnderLinetransparent(SearchView searchView){
//        try {
//            Class<?> argClass = searchView.getClass();
//            // mSearchPlate是SearchView父布局的名字
//            Field ownField = argClass.getDeclaredField("mSearchPlate");
//            ownField.setAccessible(true);
//            View mView = (View) ownField.get(searchView);
//            mView.setBackgroundColor(Color.TRANSPARENT);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
}
