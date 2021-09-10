package competition.fragment.Anim;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
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
import competition.fragment.Anim.JavaBean.Change_anim_javabean;
import competition.fragment.Me.Change_me;
import competition.fragment.Me.JavaBean.profile_javabean;
import competition.fragment.Me.JavaBean.profile_request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Change_anim extends AppCompatActivity {
    private  int petId;
    private AppCompatEditText animname;
    private AppCompatEditText animbreed;
    private AppCompatEditText animintroduction;
    private String name_str;
    private String breed_str;
    private String intro_str;
    private RadioGroup radioGroup;
    private boolean overt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_anim_message_change);
        InitView();

    }

    public void InitView() {
        animname = (AppCompatEditText) findViewById(R.id.anim_name);
        animname.setText(getIntent().getStringExtra("name"));
        animbreed = (AppCompatEditText) findViewById(R.id.anim_breed);
        animbreed.setText(getIntent().getStringExtra("breed"));
        animintroduction = (AppCompatEditText) findViewById(R.id.anim_intro);
        animintroduction.setText(getIntent().getStringExtra("intro"));
        petId=getIntent().getIntExtra("petId",0);
        radioGroup=(RadioGroup) findViewById(R.id.anim_radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.anim_isovert:
                        overt=true;
                        break;

                    case R.id.anim_notovert:
                        overt=false;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void InitData() {
        name_str = animname.getText().toString().trim();
        breed_str = animbreed.getText().toString().trim();
        intro_str = animintroduction.getText().toString().trim();
    }

    public void change(View view) {
        InitData();
        Gson gson = new Gson();
        String token = Constanst.token;
        AnimAdd_request request = new AnimAdd_request(name_str, breed_str, intro_str,overt);
        API api = RetrofitManager.getRetrofit().create(API.class);
        Call<ResponseBody> task = api.anim_change(token,request,petId);
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("Change_me", "onResponse:" + response.code());
                try {
                    String json = response.body().string();
                    profile_javabean body = gson.fromJson(json, profile_javabean.class);
                    int code = body.getCode();
                    if (code == 200) {
                        Animshow.nick.setText(name_str);
                        Animshow.kind.setText(breed_str);
                        Animshow.introduction.setText(intro_str);
                        if(overt)
                            Animshow.isovert.setChecked(true);
                        else
                            Animshow.isovert.setChecked(false);
                        Animshow.dialog.dismiss();
                        finish();
                        Toast.makeText(Change_anim.this, body.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        String message = body.getMsg();
                        Toast.makeText(Change_anim.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Change_anim.this, "服务器异常...请稍后再试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Change_me", "onFailure:" + t.getMessage());
            }
        });
    }
}
