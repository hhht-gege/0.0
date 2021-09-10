package competition.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import competition.Login.JavaBean.Find_JavaBean;
import competition.Login.JavaBean.Findok_JavaBean;
import competition.Login.JavaBean.Findok_request;
import competition.Utils.Constanst;
import competition.Login.JavaBean.Find_request;
import competition.R;
import competition.Utils.API;
import competition.Utils.RetrofitManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

public class FindpassActivity extends AppCompatActivity {
    private static final String TAG = "FindpassActivity";

    private EditText iden_code;
    private EditText username;
    private EditText newPassword;
    private String newPassword_str;
    private String code_str;
    private String code_real;
    private String username_str;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpass);
        code_real=null;

        iden_code = (EditText) findViewById(R.id.find_code);
        username = (EditText) findViewById(R.id.find_user);
        newPassword=(EditText) findViewById(R.id.find_newpassword);
    }

    //布局里面设置的监听方法
    public void send(View v) {
        username_str = username.getText().toString().trim();
        Gson gson=new Gson();

        Find_request find_request = new Find_request(username_str);
        API api = RetrofitManager.getRetrofit().create(API.class);
        Call<ResponseBody> task = api.post_find(find_request);

        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:" + response.code());
                try {
                    String jsonStr=response.body().string();
                    Log.d(TAG, jsonStr);
                    Find_JavaBean body=gson.fromJson(jsonStr,Find_JavaBean.class);
                    int code = body.getCode();
                    if (code == 200) {
                        code_real = body.getData().getCheckCode();
                        Toast.makeText(FindpassActivity.this, "send ok!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        String message=body.getMsg();
                        Toast.makeText(FindpassActivity.this,message,Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(FindpassActivity.this, "服务器异常...请稍后再试", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure:" + t.getMessage());
            }
        });
    }

    public void ok(View v) {
        code_str = iden_code.getText().toString().trim();
        newPassword_str=newPassword.getText().toString().trim();
        Gson gson=new Gson();
        Findok_request findok_request=new Findok_request(username_str,newPassword_str,code_str);
        API api= RetrofitManager.getRetrofit().create(API.class);
        Call<ResponseBody> task=api.post_findok(findok_request);
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:" + response.code());
                try {
                    String json_str=response.body().string();
                    Log.d(TAG,json_str);
                    Findok_JavaBean body=gson.fromJson(json_str,Findok_JavaBean.class);
                    int code=body.getCode();
                    if(code==200){
                        Toast.makeText(FindpassActivity.this,"重置成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(FindpassActivity.this,LoginMainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        String message=body.getMsg();
                        Toast.makeText(FindpassActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(FindpassActivity.this, "服务器异常...请稍后再试", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure:" + t.getMessage());
            }
        });

        /*code_str = iden_code.getText().toString().trim();
        if (code_real == code_str && code_real != null) {
            Intent intent = new Intent(FindpassActivity.this, ReviseActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(FindpassActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
        }*/
    }

}
