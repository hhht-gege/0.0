package competition.fragment.Setting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.internal.Constants;
import competition.R;
import competition.Service.railingService;
import competition.Service.tempService;
import competition.Socket.MSocket;
import competition.Utils.API;
import competition.Utils.Constanst;
import competition.Utils.RetrofitManager;
import competition.Utils.UU;
import competition.fragment.Setting.Adapter.SetAdapter;
import competition.fragment.Setting.Javabean.Pet_ip_javabean;
import competition.fragment.Setting.Javabean.bindcollar_javabean;
import competition.fragment.Setting.Javabean.bindcollar_request;
import competition.fragment.Setting.Javabean.collars_javabean;
import competition.fragment.Setting.Javabean.status_javabean;
import competition.zxing.android.CaptureActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetFragment extends Fragment {
    private static String TAG = "SetFragment";

    private Button Set_add;
    private RecyclerView recyclerView;
    private List<Setitem> setList;
    private SetAdapter adapter;
    private SharedPreferences prf;

    private int collars;

    public String url = "";
    public String name = " ";


    public SetFragment() {
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        prf = PreferenceManager.getDefaultSharedPreferences(getActivity());
        InitView(view);
        adapter = new SetAdapter(setList, getContext());
        adapter.setOnItemClickListener(new SetAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(), Set_show.class);
                intent.putExtra("ip", setList.get(position).getCollarId());
                intent.putExtra("petName",setList.get(position).getName());
                startActivity(intent);
            }
        });
        //线性管理器,支持横向纵向
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void InitView(View view) {
        Set_add = (Button) view.findViewById(R.id.set_add0);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview_set);
        setList = new ArrayList<>();
        Set_add.setOnClickListener(v -> {
            //动态权限申请
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            } else {
                //扫码
                goScan();
            }
        });
    }

    /**
     * 跳转到扫码界面扫码
     */
    private void goScan(){
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, Constanst.REQ_QR_CODE);
    }

    public void InitSocket() {
        int flag=0;
        Constanst.tmp++;
        for (Setitem item : setList) {
            if (item.getPetId() > 0) {
                flag=1;
            }
            if(flag==1)
                InitService();
            Initsocket(item);
            Log.d(TAG, "InitSocket: " + item.getCollarId());

        }
        Constanst.firstip = setList.get(0).getCollarId();
        Log.d(Constanst.firstip, "ip");
    }

    public void Initsocket(Setitem item) {
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)//设置读取超时时间
                .connectTimeout(5, TimeUnit.SECONDS)//设置连接超时时间
                .build();
        Request request = new Request.Builder().url("ws://112.74.42.232:8083/websocket/" + item.getCollarId()).build();
        MSocket socketListener = new MSocket(getActivity(), item.getCollarId());
        mOkHttpClient.newWebSocket(request, socketListener);
        //调用dispatcher的executorService的shutDown方法。这样后面的所有网络访问也就会被拒绝了。
        mOkHttpClient.dispatcher().executorService().shutdown();
    }


    public void InitService() {
        Intent intent = new Intent(getActivity(), railingService.class);
        getActivity().startService(intent);
        Intent intentT = new Intent(getActivity(), tempService.class);
        getActivity().startService(intentT);
    }


    public void Initcollar() {
        String token = Constanst.token;
        Gson gson = new Gson();
        API api = RetrofitManager.getRetrofit().create(API.class);
        //同步请求,在Android 4.0以上，网络连接不能放在主线程上，不然就会报错
        Call<ResponseBody> task = api.get_collar(token);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = task.execute();
                    String json = response.body().string();
                    Log.d(TAG, json);
                    collars_javabean body = gson.fromJson(json, collars_javabean.class);
                    int code = body.getCode();
                    if (code == 200) {
                        List<collars_javabean.DataBean.CollarsBean> lists = body.getData().getCollars();
                        collars = lists.size();
                        for (int i = 0; i < lists.size(); i++) {
                            Setitem sets = new Setitem(lists.get(i).getCollarId(), lists.get(i).getCollarIntroduction(), lists.get(i).getUserId(), lists.get(i).getPetId());
                            getpet(lists.get(i).getCollarId());
                            sets.setUrl(url);
                            sets.setName(name);
                            setList.add(sets);
                            url = "";
                            name = " ";
                        }
                    } else {
                        Message message = Message.obtain();
                        message.obj = body.getMsg();
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "服务器异常...请稍后再试", Toast.LENGTH_LONG).show();
                }
            }
        });
        thread.start();

        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            Message message = Message.obtain();
            message.obj = "服务器异常.....";
            handler.sendMessage(message);
        }
    }

    public void getpet(String ip) throws InterruptedException {
        //  Log.d(TAG, "getpet: " + ip);
        Gson gson = new Gson();
        API api = RetrofitManager.getRetrofit().create(API.class);
        Call<ResponseBody> task = api.getPetByIp(ip);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = task.execute();
                    String json = response.body().string();
                    // Log.d(TAG, "onResponse: " + json);
                    Pet_ip_javabean bean = gson.fromJson(json, Pet_ip_javabean.class);
                    int code = bean.getCode();
                    if (code == 200) {
                        url = bean.getData().getPet().getPetPortraitPath();
                        name = bean.getData().getPet().getPetName();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.join();
    }

    public void set_add(String ip) throws InterruptedException{
        Gson gson=new Gson();
        API api = RetrofitManager.getRetrofit().create(API.class);
        Call<ResponseBody> task=api.collar_status(Constanst.token,ip);
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response= task.execute();
                    String json=response.body().string();
                    Log.d(TAG,json);
                    status_javabean body=gson.fromJson(json,status_javabean.class);
                    int code=body.getCode();
                    if(code==203){
                        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                                .readTimeout(5, TimeUnit.SECONDS)//设置读取超时时间
                                .connectTimeout(5, TimeUnit.SECONDS)//设置连接超时时间
                                .build();
                        Request request = new Request.Builder().url("ws://112.74.42.232:8083/websocket/" + ip).build();
                        MSocket socketListener = new MSocket(getActivity(), ip);
                        mOkHttpClient.newWebSocket(request, socketListener);
                        //调用dispatcher的executorService的shutDown方法。这样后面的所有网络访问也就会被拒绝了。
                        mOkHttpClient.dispatcher().executorService().shutdown();

                        //更新adapter
                        Setitem item=new Setitem(ip,"", UU.userId,0);
                        setList.add(item);

                        Message message=Message.obtain();
                        message.obj="服务器连接成功";
                        handler.sendMessage(message);

                    }else{
                        Message message=Message.obtain();
                        message.obj=body.getMsg();
                        handler.sendMessage(message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Message message=Message.obtain();
                    message.obj="服务器异常...请稍后再试";
                    handler.sendMessage(message);
                }
            }
        });
        thread.start();
        try {
            thread.join();
            adapter.notifyDataSetChanged();
        }catch (InterruptedException e){
            e.printStackTrace();
            Message message=Message.obtain();
            message.obj="服务器异常join请稍后再试";
            handler.sendMessage(message);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setList.clear();
        Initcollar();
        adapter.notifyDataSetChanged();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                InitSocket();
            }
        });
        if (Constanst.tmp == 0 && collars > 0)
            t.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //读取二维码
        if (requestCode == Constanst.REQ_QR_CODE && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constanst.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            Log.d(TAG, "onActivityResult: "+scanResult);
               try {
                    set_add(scanResult);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }
}
