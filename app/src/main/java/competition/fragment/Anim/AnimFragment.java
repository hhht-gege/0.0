package competition.fragment.Anim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import competition.R;
import competition.Service.tempService;
import competition.Utils.API;
import competition.Utils.Constanst;
import competition.Utils.RetrofitManager;
import competition.fragment.Anim.Adapter.AnimAdapter;
import competition.fragment.Anim.JavaBean.Allanim_javabean;
import competition.fragment.Anim.JavaBean.Anim_ip_javabean;
import competition.fragment.Anim.JavaBean.Anim_javabean;
import competition.fragment.Anim.View.DraggingButton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnimFragment extends Fragment {
    private static String TAG = "AnimFragment";

    private Button Anim_add;
    private RecyclerView recyclerView;
    private static List<Anim> animList;
    private AnimAdapter adapter;
    public static List<Integer> petIds;

    private int anims;


    public AnimFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anim, container, false);
        InitView(view);
        adapter = new AnimAdapter(animList, getContext());
        adapter.setOnItemClickListener(new AnimAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                try {
                    Into_anim(position);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        //线性管理器,支持横向纵向
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        animList.clear();
        try {
            InitAnim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    private void InitView(View view) {
        petIds=new ArrayList<>();
        Anim_add = (Button) view.findViewById(R.id.anim_add0);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        animList = new ArrayList<>();
        Anim_add.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AnimAdd.class);
            startActivity(intent);
        });
    }

    public void InitAnim() throws InterruptedException {
        petIds.clear();
        String token = Constanst.token;
        Gson gson = new Gson();
        API api = RetrofitManager.getRetrofit().create(API.class);
        //同步请求,在Android 4.0以上，网络连接不能放在主线程上，不然就会报错
        Call<ResponseBody> task = api.get_addall(token);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = task.execute();
                    String json = response.body().string();
                    Log.d(TAG, json);
                    Allanim_javabean body = gson.fromJson(json, Allanim_javabean.class);
                    int code = body.getCode();
                    if (code == 200) {
                        List<Allanim_javabean.DataBean.PetsBean> lists = body.getData().getPets();
                        anims=lists.size();
                        for (int i = 0; i < lists.size(); i++) {
                            petIds.add(lists.get(i).getPetId());
                            Anim anims = new Anim(lists.get(i).getPetId(), lists.get(i).getPetName(), lists.get(i).getPetBreed(), lists.get(i).getPetPortraitPath(),lists.get(i).isOvert());
                            animList.add(anims);
                        }
                    } else {
                        String message = body.getMsg();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(getActivity(), "服务器异常...请稍后再试", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
        thread.start();

        thread.join();
    }

    public void Into_anim(int position) throws InterruptedException {
        int petId=animList.get(position).getPetId();
        String token = Constanst.token;
        Gson gson = new Gson();
        API api = RetrofitManager.getRetrofit().create(API.class);
        //同步请求,在Android 4.0以上，网络连接不能放在主线程上，不然就会报错
        Call<ResponseBody> task = api.into_anim(token,petId);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = task.execute();
                    String json = response.body().string();
                    Log.d(TAG, json);
                    Anim_javabean body = gson.fromJson(json, Anim_javabean.class);
                    int code = body.getCode();
                    if (code == 200) {
                        Intent intent = new Intent(getActivity(), Animshow.class);
                        Constanst.anim=new Anim_javabean();
                        Constanst.anim.setData(body.getData());
                        Log.d("anim",Constanst.anim.getData().getPet().getPetId()+"");
                        startActivity(intent);
                    } else {
                        String message = body.getMsg();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    show(getActivity());
                }
            }
        });
        thread.start();
        thread.join();
    }
    public static void show(Context context){
        Toast.makeText(context, "服务器异常...请稍后再试", Toast.LENGTH_LONG).show();
    }

}
