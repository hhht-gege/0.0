package competition.Login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

import android.os.Handler;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import competition.Login.JavaBean.JavaBean;
import competition.Login.JavaBean.login_RequestBody;
import competition.Utils.Constanst;
import competition.MainActivity;
import competition.R;
import competition.Utils.UU;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginMainActivity extends AppCompatActivity {
    private static final String TAG = "LoginMainActivity";

    private EditText username;
    private EditText password;
    private String usernameStr;
    private String passwordStr;

    private ToggleButton etp;

    private SharedPreferences prf;
    private SharedPreferences.Editor editor;


    private final int LOGINSUCCESS = 0;
    private final int LOGINNOTFOUND = 1;
    private final int LOGINEXCEPT = 2;

    private final int RESULT_CODE_STARTCAMERA = 100;
    private final int RESULT_CODE_LOCATION=101;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {//消息机制，用来在子线程中更新UI
            switch (msg.what) {//具体消息，具体显示
                case LOGINSUCCESS:
                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case LOGINNOTFOUND:
                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case LOGINEXCEPT:
                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //找到我们需要的控件
        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        prf = PreferenceManager.getDefaultSharedPreferences(this);
        etp = (ToggleButton) findViewById(R.id.togglePwd);
        etp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etp.setBackgroundResource(R.drawable.ic_eyeo);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etp.setBackgroundResource(R.drawable.ic_eyec);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        InitPermission();
        //直接进行免密登录
        Nopass();


    }

    //登录按钮的点击事件，也可以用set监听器的方法，不过这种方法简单
    public void login(View v) {
        //获取编辑框内的内容
        usernameStr = username.getText().toString().trim();
        passwordStr = password.getText().toString().trim();

        //判断是否输入为空（在这里就不再进行正则表达式判断了）
        if (usernameStr.equals("") || passwordStr.equals("")) {
            Toast.makeText(LoginMainActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        }//进行登录操作(联网操作要添加权限)
        else {
            //联网操作,Okhttp3异步操作
            checklogin();
        }
    }

    public void checklogin() {
        usernameStr = username.getText().toString().trim();
        passwordStr = password.getText().toString().trim();

        Gson gson = new Gson();
        MediaType mediaType = MediaType.parse(Constanst.Gson_Media);
        login_RequestBody loginRebody = new login_RequestBody(usernameStr, passwordStr);
        String loginbody = gson.toJson(loginRebody);
        Request request = new Request.Builder().url(Constanst.login_url)
                .post(okhttp3.RequestBody.create(mediaType, loginbody)).addHeader("Content-Type", "application/json").build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure:" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.protocol() + " " + response.code() + " " + response.message());
                try {
                    String jsonString = response.body().string();
                    Log.d(TAG, "onResponse:" + jsonString);
                    JavaBean loginbean = gson.fromJson(jsonString, JavaBean.class);
                    int code = loginbean.getCode();
                    if (code == 200) {
                        Message message = Message.obtain();//更新UI就要向消息机制发送消息
                        message.what = LOGINSUCCESS;//用来标志是哪个消息
                        message.obj = loginbean.getMsg();//消息主体
                        handler.sendMessage(message);
                        editor = prf.edit();

                        String token = loginbean.getData().getToken();
                        Log.d(TAG, "token->:" + token);
                        editor.putString("token", token);
                        editor.apply();
                        Initmessage(loginbean);

                        Intent intent = new Intent(LoginMainActivity.this, MainActivity.class);
                        //启动
                        startActivity(intent);

                    } else {
                        Message message = Message.obtain();
                        message.what = LOGINNOTFOUND;
                        message.obj = loginbean.getMsg();
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what = LOGINEXCEPT;
                    message.obj = "服务器异常...请稍后再试";
                    handler.sendMessage(message);
                }
            }
        });

    }

    //注册按钮的点击事件
    public void register(View v) {
        Intent intent = new Intent(LoginMainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void forgot(View v) {
        Intent intent = new Intent(LoginMainActivity.this, FindpassActivity.class);
        startActivity(intent);

    }

    public void Nopass() {
        System.out.println("进行了免密登录");
        prf = PreferenceManager.getDefaultSharedPreferences(this);
        String the_token = prf.getString("token", "");
        Log.d(TAG, "token->" + the_token);
        Gson gson = new Gson();
        MediaType mediaType = MediaType.parse(Constanst.Gson_Media);
        Request request = new Request.Builder().url(Constanst.Login_nopass_url)
                .post(okhttp3.RequestBody.create(mediaType, "")).addHeader("Content-Type", "application/json").addHeader("token", the_token).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure:" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.message());
                try {
                    String json_str = response.body().string();
                    Log.d(TAG, json_str);
                    JavaBean bean = gson.fromJson(json_str, JavaBean.class);
                    int code = bean.getCode();
                    if (code == 200) {
                        Initmessage(bean);
                        Intent intent = new Intent(LoginMainActivity.this, MainActivity.class);
                        //启动
                        startActivity(intent);
                    } else {
                        Log.d(TAG, "onResponse: +wroing");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what = LOGINEXCEPT;
                    message.obj = "服务器异常...请稍后再试";
                    handler.sendMessage(message);
                }
            }
        });
    }

    public void InitPermission() {
        Log.d("roamer", "clickCamera");

        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
            //提示用户开户权限   拍照和读写sd卡权限
            String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(this, perms, RESULT_CODE_STARTCAMERA);
        }
        if(PackageManager.PERMISSION_GRANTED!=ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
            String[] perm={Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this,perm,RESULT_CODE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RESULT_CODE_STARTCAMERA:
                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted) {
                } else {
                    //用户授权拒绝之后，友情提示一下就可以了
                    Toast.makeText(this, "请开启相册权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case RESULT_CODE_LOCATION:
                boolean locationAccepted=grantResults[0]== PackageManager.PERMISSION_GRANTED;
                if(locationAccepted){

                }else{
                    Toast.makeText(this, "请开启定位权限", Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void Initmessage(JavaBean bean) {
        JavaBean.DataBean.UserBean userBean = bean.getData().getUser();
        UU.userId = userBean.getUserId();
        UU.username = userBean.getUsername();
        UU.userIntroduction = userBean.getUserIntroduction();
        UU.userPortraitPath = userBean.getUserPortraitPath();
        UU.userEmailAddress = userBean.getUserEmailAddress();
    }
}
