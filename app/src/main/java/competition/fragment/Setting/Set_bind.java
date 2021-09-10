package competition.fragment.Setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import competition.R;
import competition.Utils.API;
import competition.Utils.Constanst;
import competition.Utils.RetrofitManager;
import competition.fragment.Anim.AnimFragment;
import competition.fragment.Setting.Javabean.bindcollar_javabean;
import competition.fragment.Setting.Javabean.bindcollar_request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Set_bind extends AppCompatActivity {

    private static final String TAG = "Set_bind";
    private Button bindadd;
    private MaterialTextView bindedit;
    private String bind_str;
    private String ip;
    private OptionsPickerView picker;
    private List<Integer> petIds;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_set_bind);
        ip = getIntent().getStringExtra("ip");
        petIds = new ArrayList<>();
        petIds = AnimFragment.petIds;
        bind();
        InitPicker();
    }

    public void bind() {
        bindadd = (Button) findViewById(R.id.set_bindadd);
        bindedit = (MaterialTextView) findViewById(R.id.set_bindedit);
        bindedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (petIds.size() > 0)
                    picker.show(v);
                else
                    Toast.makeText(Set_bind.this, "您暂时还没有牲畜", Toast.LENGTH_SHORT).show();
            }
        });
        bindadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bind_str = bindedit.getText().toString().trim();
                bindcollar();
            }
        });
    }

    public void bindcollar() {
        Gson gson = new Gson();
        API api = RetrofitManager.getRetrofit().create(API.class);
        int petid = Integer.parseInt(bind_str);
        bindcollar_request request = new bindcollar_request(ip, "", petid);
        Call<ResponseBody> task = api.bind_collar(Constanst.token, request);
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:" + response.code());
                try {
                    String json = response.body().string();
                    bindcollar_javabean body = gson.fromJson(json, bindcollar_javabean.class);
                    Log.d(TAG, json);
                    int code = body.getCode();
                    if (code == 200) {
                        Toast.makeText(Set_bind.this, body.getMsg(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(Set_bind.this, body.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Set_bind.this, "服务器异常。。。。", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure:" + t.getMessage());
            }
        });
    }

    public void InitPicker() {
        picker = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                int tx = petIds.get(options1);
                System.out.println("tx->>>" + tx);
                bindedit.setText(String.valueOf(tx));
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("PetId")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setContentTextSize(18)//滚轮文字大小
                .setLabels(null, null, null)//设置选择的三级单位
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .build();
        picker.setPicker(petIds);

        Dialog mDialog = picker.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            picker.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }
}
