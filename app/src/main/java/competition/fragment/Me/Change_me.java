package competition.fragment.Me;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.gson.Gson;

import java.io.IOException;

import competition.R;
import competition.Utils.API;
import competition.Utils.Constanst;
import competition.Utils.RetrofitManager;
import competition.Utils.UU;
import competition.fragment.Anim.JavaBean.AnimAdd_request;
import competition.fragment.Me.JavaBean.profile_javabean;
import competition.fragment.Me.JavaBean.profile_request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Change_me extends AppCompatActivity {
    private AppCompatEditText username;
    private AppCompatEditText email;
    private AppCompatEditText introduction;
    private String user_str;
    private String email_str;
    private String intro_str;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_changeme);
        InitView();

    }

    public void InitView() {
        username = (AppCompatEditText) findViewById(R.id.me_username);
        username.setText(getIntent().getStringExtra("name"));
        email = (AppCompatEditText) findViewById(R.id.me_email);
        email.setText(UU.userEmailAddress);
        introduction = (AppCompatEditText) findViewById(R.id.me_intro);
        introduction.setText(getIntent().getStringExtra("intro"));
    }

    public void InitData() {
        user_str = username.getText().toString().trim();
        email_str = email.getText().toString().trim();
        intro_str = introduction.getText().toString().trim();
    }

    public void change(View view) {
        InitData();
        Gson gson = new Gson();
        String token = Constanst.token;
        profile_request request = new profile_request(user_str, email_str, intro_str);
        API api = RetrofitManager.getRetrofit().create(API.class);
        Call<ResponseBody> task = api.change_message(request, token);
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("Change_me", "onResponse:" + response.code());
                try {
                    String json = response.body().string();
                    profile_javabean body = gson.fromJson(json, profile_javabean.class);
                    int code = body.getCode();
                    if (code == 200) {
                        finish();
                    } else {
                        String message = body.getMsg();
                        Toast.makeText(Change_me.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Change_me.this, "服务器异常...请稍后再试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Change_me", "onFailure:" + t.getMessage());

            }
        });
    }
}
