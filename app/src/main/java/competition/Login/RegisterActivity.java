package competition.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;

import competition.Login.JavaBean.JavaBean;
import competition.Login.JavaBean.Regis_reque;
import competition.Login.JavaBean.login_RequestBody;
import competition.R;
import competition.Utils.Constanst;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private TextInputLayout layout1;
    private TextInputEditText Reaccount;
    private TextInputLayout layout2;
    private TextInputEditText Repassword;
    private TextInputLayout layout3;
    private TextInputEditText Repassword2;
    private TextInputLayout layout4;
    private TextInputEditText emailAdress;

    private String usernameStr;
    private String passwordStr;
    private String passwordStr2;
    private String emailAdres_str;

    private SharedPreferences prf;
    private SharedPreferences.Editor editor;

    private AppCompatButton Bregister;

    private final int REGISTERSUCCESS = 3;
    private final int REGISTERNOTFOUND = 4;
    private final int REGISTEREXCEPT = 5;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {//消息机制，用来在子线程中更新UI
            switch (msg.what) {//具体消息，具体显示
                case REGISTERSUCCESS:
                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case REGISTERNOTFOUND:
                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case REGISTEREXCEPT:
                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        prf= PreferenceManager.getDefaultSharedPreferences(this);
        Init();
        Bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRegister();
            }
        });

    }

    public void Init() {
        Reaccount = (TextInputEditText) findViewById(R.id.tie_reg_account);
        Repassword = (TextInputEditText) findViewById(R.id.tie_reg_password);
        Repassword2 = (TextInputEditText) findViewById(R.id.tie_reg_repassword);
        emailAdress=(TextInputEditText) findViewById(R.id.tie_reg_email);
        Bregister = (AppCompatButton) findViewById(R.id.btn_reg_commit);
        layout1=(TextInputLayout) findViewById(R.id.til_reg_account);
        layout2=(TextInputLayout) findViewById(R.id.til_reg_password);
        layout3=(TextInputLayout) findViewById(R.id.til_reg_repassword);
        layout4=(TextInputLayout) findViewById(R.id.til_reg_email);
    }

    public void checkRegister(){
            usernameStr = Reaccount.getText().toString();
            passwordStr = Repassword.getText().toString();
            passwordStr2 = Repassword2.getText().toString();
            emailAdres_str=emailAdress.getText().toString();
            if (passwordStr.equals(passwordStr2)) {
                Gson gson=new Gson();
                MediaType mediaType = MediaType.parse(Constanst.Gson_Media);

                Regis_reque rr=new Regis_reque(usernameStr,passwordStr,emailAdres_str);
                String body=gson.toJson(rr);

                Request request = new Request.Builder().url(Constanst.registerurl)
                        .post(okhttp3.RequestBody.create(mediaType, body)).addHeader("Content-Type","application/json").build();
                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d(TAG, "onFailure:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.d(TAG,response.protocol()+" "+response.code()+" "+response.message());
                        try {
                            Gson gson = new Gson();
                            String jsonString = response.body().string();
                            JavaBean result = gson.fromJson(jsonString, JavaBean.class);
                            int code = result.getCode();

                            if (code == 200) {
                                Message message = Message.obtain();
                                message.obj = result.getMsg();
                                message.what = REGISTERSUCCESS;
                                handler.sendMessage(message);

                                editor=prf.edit();
                                String token=result.getData().getToken();
                                Log.d(TAG,"token->:"+token);
                                editor.putString("token",token);
                                editor.apply();

                                Intent intent = new Intent(RegisterActivity.this, LoginMainActivity.class);
                                startActivity(intent);

                            } else {
                                Message message = Message.obtain();
                                message.obj = result.getMsg();
                                message.what = REGISTERNOTFOUND;
                                handler.sendMessage(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message message = Message.obtain();
                            message.what = REGISTEREXCEPT;
                            message.obj = "服务器异常...请稍后再试";
                            handler.sendMessage(message);
                        }
                    }
                });

        } else {
            Toast.makeText(RegisterActivity.this, "password and Repassword aren't equal", Toast.LENGTH_SHORT).show();
        }
    }
}
