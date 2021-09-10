package competition.fragment.Anim;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import competition.R;
import competition.Utils.API;
import competition.Utils.Constanst;
import competition.Utils.GetPath;
import competition.Utils.RealPathFromUriUtils;
import competition.Utils.RetrofitManager;
import competition.fragment.Anim.Adapter.Anim_GvAdapter;
import competition.fragment.Anim.JavaBean.AnimAdd_javabean;
import competition.fragment.Anim.JavaBean.AnimAdd_request;
import competition.fragment.Anim.JavaBean.Anim_imgjavabean;
import competition.fragment.Anim.JavaBean.Imag_javabean;
import competition.fragment.Search.Adapter.Search_GvAdapter;
import competition.fragment.Search.SearchAdd;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static competition.fragment.Search.UploadImags.ReturnPart;

public class AnimAdd extends AppCompatActivity {
    private static final String TAG = "AnimAdd";
    int flag = 0;
    ImagePicker imagePicker;

    private EditText nick;
    private String nick_str;
    private AppCompatSpinner kind;

    private String kind_str;
    private EditText introduction;
    private String intro_str;
    private ImageView image;
    private CheckBox isovert;
    private String anim_pic;
    private Button back;

    private int petId;
    private GridView gridView;
    private Anim_GvAdapter adapter;
    private List<String> list;
    private String path;

    private SharedPreferences prf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_anim_add);
        prf = PreferenceManager.getDefaultSharedPreferences(this);
        InitView();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {//消息机制，用来在子线程中更新UI
            Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
        }
    };

    public void InitView() {
        nick = (EditText) findViewById(R.id.anim_add_nick);
        kind = (AppCompatSpinner) findViewById(R.id.anim_add_kind);
        kind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] kinds = getResources().getStringArray(R.array.kinds);
                kind_str=kinds[position];
                System.out.println(kind_str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        introduction = (EditText) findViewById(R.id.anim_add_introduction);
        image = (ImageView) findViewById(R.id.anim_add_imag);
        back = (Button) findViewById(R.id.set_add_back);
        isovert = (CheckBox) findViewById(R.id.anim_add_isovert);
        imagePicker = new ImagePicker();
        // 设置标题
        imagePicker.setTitle("设置头像");
        // 设置是否裁剪图片
        imagePicker.setCropImage(true);
        gridView = (GridView) findViewById(R.id.grid_anim);

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
    }

    public void InitData() {
        nick_str = nick.getText().toString().trim();
        intro_str = introduction.getText().toString().trim();
    }


    //添加
    public void add_create(View view) throws InterruptedException {
        InitData();
        Gson gson = new Gson();
        String token = prf.getString("token", "");
        AnimAdd_request request = new AnimAdd_request(nick_str, kind_str, intro_str, isovert.isChecked());
        API api = RetrofitManager.getRetrofit().create(API.class);
        Call<ResponseBody> task = api.post_add(request, token);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = task.execute();
                    String json = response.body().string();
                    AnimAdd_javabean body = gson.fromJson(json, AnimAdd_javabean.class);
                    Log.d(TAG, json);
                    int code = body.getCode();
                    if (code == 200) {
                        flag = 1;
                        petId = body.getData().getPetId();
                        Log.d(TAG, "添加成功！！");
                    } else {
                        Message message = Message.obtain();//更新UI就要向消息机制发送消息
                        message.obj = body.getMsg();//消息主体
                        handler.sendMessage(message);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.obj = "服务器异常...请稍后再试";
                    handler.sendMessage(message);
                }
            }
        });
        thread.start();
        thread.join();

        if (flag != 1)
            return;

        if (anim_pic != null) {
            Upload_imag(anim_pic, petId, token);
        }

        if (list.size() != 0)
            Upload_animimg();
        else
            finish();

    }


    public void Upload_imag(String str, int petId, String token) throws InterruptedException {
        // Log.d(TAG, "petId->" + petId + "     str->" + str);
        Gson gson = new Gson();
        UploadImag load = new UploadImag();
        Call<ResponseBody> task = load.uploadImag(str, petId, token);
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = task.execute();
                    String json = response.body().string();
                    Log.d(TAG, json);
                    Imag_javabean body = gson.fromJson(json, Imag_javabean.class);
                    int code = body.getCode();
                    if (code == 200) {
                        anim_pic = body.getData().getPetPortraitPath();
                    } else {
                        Message message = Message.obtain();//更新UI就要向消息机制发送消息
                        message.obj = body.getMsg();//消息主体
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.obj = "服务器异常...请稍后再试";
                    handler.sendMessage(message);
                }
            }
        });
        thread2.start();
        thread2.join();
    }

    public void Upload_animimg() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "flag->" + flag);
        if (flag == 0) {
            Constanst.animal = null;
        }

    }

    public  void InitImageView() {
        // 启动图片选择器
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override
            public void onPickImage(Uri imageUri) {
            }

            // 裁剪图片回调
            @Override
            public void onCropImage(Uri imageUri) {
                anim_pic = getPath(imageUri);
                Glide.with(AnimAdd.this).load(anim_pic).into(image);
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
                    Toast.makeText(AnimAdd.this, "最多选择五张图片", Toast.LENGTH_LONG).show();
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
        imagePicker.onActivityResult(AnimAdd.this, requestCode, resultCode, data);

    }

    public String getPath(Uri uri) {
        String path1 = uri.getPath();
        if (path1.charAt(0) == 'f') {
            return uri.getPath();
        } else {
            String filePath = GetPath.getPath(AnimAdd.this, uri);
            Log.d("str", "file" + filePath);
            if (filePath != null)
                return filePath;
            else
                return " ";
        }
    }

    //上传图片
    public void add_image_text(View view) {
        InitImageView();
    }


}
