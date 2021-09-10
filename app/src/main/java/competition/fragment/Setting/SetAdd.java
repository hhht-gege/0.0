package competition.fragment.Setting;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.TimeUnit;

import competition.R;
import competition.Socket.MSocket;
import competition.Utils.API;
import competition.Utils.Constanst;
import competition.Utils.RetrofitManager;
import competition.fragment.Setting.Javabean.bindcollar_javabean;
import competition.fragment.Setting.Javabean.bindcollar_request;
import competition.fragment.Setting.Javabean.status_javabean;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetAdd extends AppCompatActivity {
    private static final String TAG = "SetAdd";

    private OptionsPickerView Opicker;
    private String name;
    private EditText set_edit;
    private Button set_add;
    private Button back;
    private String set_edit_str;
    private List<String> petIds;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {//消息机制，用来在子线程中更新UI
            Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_set_add);
        InitView();

    }

    public void InitView(){
        set_edit=(EditText) findViewById(R.id.set_edit);
        set_add=(Button) findViewById(R.id.set_add);
        back=(Button) findViewById(R.id.set_add_back);
    }
    public void InitData(){
        set_edit_str=set_edit.getText().toString().trim();
    }

    public void set_add(View view) throws InterruptedException{
        InitData();
        Gson gson=new Gson();
        API api = RetrofitManager.getRetrofit().create(API.class);
        Call<ResponseBody> task=api.collar_status(Constanst.token,set_edit_str);
        Log.d(TAG,set_edit_str);
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
                        Request request = new Request.Builder().url("ws://112.74.42.232:8083/websocket/" + set_edit_str).build();
                        MSocket socketListener = new MSocket(SetAdd.this, set_edit_str);
                        mOkHttpClient.newWebSocket(request, socketListener);
                        //调用dispatcher的executorService的shutDown方法。这样后面的所有网络访问也就会被拒绝了。
                        mOkHttpClient.dispatcher().executorService().shutdown();
                        Message message=Message.obtain();
                        message.obj="服务器连接成功";
                        handler.sendMessage(message);
                        finish();
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
        }catch (InterruptedException e){
            e.printStackTrace();
            Message message=Message.obtain();
            message.obj="服务器异常join请稍后再试";
            handler.sendMessage(message);
        }
    }

    public void add_back(View view){
        finish();
    }

}
