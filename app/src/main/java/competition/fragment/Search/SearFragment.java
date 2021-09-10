package competition.fragment.Search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import competition.R;
import competition.Utils.API;
import competition.Utils.Constanst;
import competition.Utils.RetrofitManager;
import competition.Utils.View.SpacesItemDecoration;
import competition.fragment.Search.Adapter.SearchAdapter;
import competition.fragment.Search.JavaBean.end_javabean;
import competition.fragment.Search.JavaBean.top_javabean;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class SearFragment extends Fragment {
    private final String TAG = "SearFragment";

    private Button search_btn;
    private AppCompatImageButton add_btn;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    public static List<Search> searchList;
    private SearchAdapter adapter;
    private boolean refreshType;
    private int current;
    private int nextOffset;

    private Button near_btn;

    public SearFragment() {
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {//消息机制，用来在子线程中更新UI
            Toast.makeText(getContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        InitView(view);
        initData();
        adapter = new SearchAdapter(searchList, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.addItemDecoration(new SpacesItemDecoration(20));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void InitView(View view) {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        recyclerView = view.findViewById(R.id.search_recyclerview);
        searchList = new ArrayList<>();

        search_btn = (Button) view.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        add_btn = (AppCompatImageButton) view.findViewById(R.id.search_add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchAdd.class);
                startActivity(intent);
            }
        });

        near_btn=(Button) view.findViewById(R.id.search_near_anim);
        near_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Search_near.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    public void initData() {
        current=0;
        // 开启自动加载功能（非必须）
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setRefreshHeader(new MaterialHeader(getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        //上拉刷新数据
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("refresh","onclick");
                        refreshType = true;
                        rfreshe();
                        refreshLayout.finishRefresh();
                        refreshLayout.resetNoMoreData();//setNoMoreData(false);
                    }
                }, 2000);
            }
        });

        //下拉加载数据
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshType = false;
                        if (nextOffset<0) {
                            Message message=Message.obtain();
                            message.obj="暂无更多的数据啦";
                            handler.sendMessage(message);
                            // 将不会再次触发加载更多事件
                            refreshLayout.finishLoadMoreWithNoMoreData();
                            return;
                        }
                        parsingMovieListJson();
                        refreshLayout.setEnableLoadMore(true);
                        refreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });
        //触发自动刷新
        refreshLayout.autoRefresh();
    }

    //下拉加载数据
    private void parsingMovieListJson() {
        try {
            String token = Constanst.token;
            Gson gson = new Gson();
            API api = RetrofitManager.getRetrofit().create(API.class);
            Call<ResponseBody> task = api.get_dynamics_end(nextOffset, token);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response<ResponseBody> response = task.execute();
                        String json = response.body().string();
                     //  Log.d(TAG, json);
                        end_javabean body = gson.fromJson(json, end_javabean.class);
                        int code = body.getCode();
                        if (code == 200) {
                            nextOffset=body.getData().getNextOffset();
                            List<end_javabean.DataBean.DynamicsBean> lists = body.getData().getDynamics();
                            for (int i = 0; i < lists.size(); i++) {
                                end_javabean.DataBean.DynamicsBean.PublisherBean pubilisher = lists.get(i).getPublisher();
                                List<String> pics = lists.get(i).getPicturesUrl();
                                Search search = new Search(pubilisher.getUserPortraitPath(), pubilisher.getUserId(), pubilisher.getUsername(),
                                        pics, lists.get(i).getContent(), lists.get(i).getPublishTime(), lists.get(i).getTopic());
                                searchList.add(search);
                            }
                        } else {
                            String message = body.getMsg();
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Message message=Message.obtain();
                        message.obj="服务器异常.....";
                        handler.sendMessage(message);
                    }
                }
            });
            thread.start();
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            Message message=Message.obtain();
            message.obj="服务器异常.....";
            handler.sendMessage(message);
        }
        adapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
    }

    //上拉刷新动态
    public void rfreshe(){
        try {
            String token = Constanst.token;
            Gson gson = new Gson();
            API api = RetrofitManager.getRetrofit().create(API.class);
            Call<ResponseBody> task = api.get_dynamics_top(current, token);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response<ResponseBody> response = task.execute();
                        String json = response.body().string();
                       //Log.d(TAG, json);
                        top_javabean body = gson.fromJson(json, top_javabean.class);
                        int code = body.getCode();
                        if (code == 200) {
                            current=body.getData().getHistory_offset();
                            List<top_javabean.DataBean.DynamicsBean> lists = body.getData().getDynamics();
                            nextOffset=lists.get(lists.size()-1).getDynamicId();
                            searchList.clear();
                            for (int i = 0; i < lists.size(); i++) {
                                top_javabean.DataBean.DynamicsBean.PublisherBean pubilisher = lists.get(i).getPublisher();
                                List<String> pics = lists.get(i).getPicturesUrl();
                                Search search = new Search(pubilisher.getUserPortraitPath(), pubilisher.getUserId(), pubilisher.getUsername(),
                                        pics, lists.get(i).getContent(), lists.get(i).getPublishTime(), lists.get(i).getTopic());
                                searchList.add(search);
                            }
                        } else {
                            String message = body.getMsg();
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Message message=Message.obtain();
                        message.obj="服务器异常.....";
                        handler.sendMessage(message);
                    }
                }
            });
            thread.start();
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            Message message=Message.obtain();
            message.obj="服务器异常.....";
            handler.sendMessage(message);
        }
        adapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
    }



}
