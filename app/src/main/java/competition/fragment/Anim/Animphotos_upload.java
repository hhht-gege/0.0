package competition.fragment.Anim;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import competition.R;
import competition.Utils.API;
import competition.Utils.Constanst;
import competition.Utils.GetPath;
import competition.Utils.RetrofitManager;
import competition.fragment.Anim.Adapter.Anim_GvAdapter;
import competition.fragment.Anim.JavaBean.Anim_imgjavabean;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static competition.fragment.Search.UploadImags.ReturnPart;

public class Animphotos_upload extends AppCompatActivity {
    private String TAG = "Animphotos_upload";
    private int petId;
    private ImagePicker imagePicker;
    private GridView gridView;
    private Anim_GvAdapter adapter;
    public static List<String> list;
    private String path;
    private MaterialTextView cancle;
    private MaterialTextView upl;

    public static int pass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animphotos_upload);
        pass=0;
        InitView();
    }

    public void InitView() {
        petId = getIntent().getIntExtra("petId", 0);
        imagePicker = new ImagePicker();
        // 设置标题
        imagePicker.setTitle("上传相册");
        // 设置是否裁剪图片
        imagePicker.setCropImage(true);
        gridView = (GridView) findViewById(R.id.grid_animphotos);

        list = new ArrayList<>();
        adapter = new Anim_GvAdapter(this, list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View
                    view, int position, long id) {
                //判断是否是最后一个。
                if (position == parent.getChildCount() - 1) {
                    if (position == 5) {//不能点击了
                    } else {
                        opnePhoto();
                    }
                } else {//可以加点预览功能。

                }
            }
        });
        cancle = (MaterialTextView) findViewById(R.id.animphotos_cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        upl = (MaterialTextView) findViewById(R.id.animphotos_upload);
        upl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() > 0)
                    upload();
                else
                    Toast.makeText(Animphotos_upload.this, "请去相册选择图片", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void opnePhoto() {
        // 启动图片选择器
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override
            public void onPickImage(Uri imageUri) {
            }

            // 裁剪图片回调
            @Override
            public void onCropImage(Uri imageUri) {
                if (list.size() >= 5) {
                    Toast.makeText(Animphotos_upload.this, "最多选择五张图片", Toast.LENGTH_LONG).show();
                } else {
                    path = getPath(imageUri);
                    list.add(path);
                }
                adapter.notifyDataSetChanged();
            }

            // 自定义裁剪配置
            @Override
            public void cropConfig(CropImage.ActivityBuilder
                                           builder) {
                builder
                        // 是否启动多点触摸
                        .setMultiTouchEnabled(false)
                        // 设置网格显示模式
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        // 圆形/矩形
                        .setCropShape(CropImageView.CropShape
                                .RECTANGLE)
                        // 调整裁剪后的图片最终大小
                        .setRequestedSize(960, 960)
                        // 宽高比
                        .setAspectRatio(1, 1);
            }

            // 用户拒绝授权回调
            @Override
            public void onPermissionDenied(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int
            resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(Animphotos_upload.this, requestCode, resultCode, data);

    }

    public String getPath(Uri uri) {
        String path1 = uri.getPath();
        if (path1.charAt(0) == 'f') {
            return uri.getPath();
        } else {
            String filePath = GetPath.getPath(Animphotos_upload.this, uri);
            Log.d("str", "file" + filePath);
            if (filePath != null)
                return filePath;
            else
                return " ";
        }
    }

    public void upload() {
        Gson gson = new Gson();
        API api = RetrofitManager.getRetrofit().create(API.class);
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (String s : list
        ) {
            parts.add(ReturnPart(s, "photos"));
        }
        Call<ResponseBody> task = api.upload_anim_imag(parts, petId, Constanst.token);
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: " + response.code());
                try {
                    String json = response.body().string();
                    Anim_imgjavabean body = gson.fromJson(json, Anim_imgjavabean.class);
                    Log.d(TAG, json);
                    int code = body.getCode();
                    if (code == 200) {
                        Log.d(TAG, "牲畜相册上传成功！！");
                        pass=1;
                        Animshow.dialog.dismiss();
                        finish();
                    } else {
                        Message message = Message.obtain();//更新UI就要向消息机制发送消息
                        message.obj = body.getMsg();//消息主体
                        handler.sendMessage(message);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.obj = "服务器异常...请稍后再试";
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {//消息机制，用来在子线程中更新UI
            Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
        }
    };

}
