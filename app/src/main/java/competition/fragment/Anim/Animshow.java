package competition.fragment.Anim;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.PolylineOptions;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;
import com.suke.widget.SwitchButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import competition.R;
import competition.Service.AnimService;
import competition.Service.MyService;
import competition.Socket.Socketjavabean;
import competition.Utils.API;
import competition.Utils.Constanst;
import competition.Utils.GetPath;
import competition.Utils.RetrofitManager;
import competition.fragment.Anim.JavaBean.Anim_history_javabean;
import competition.fragment.Anim.JavaBean.Anim_ip_javabean;
import competition.fragment.Anim.JavaBean.Anim_javabean;
import competition.fragment.Anim.JavaBean.Imag_javabean;
import competition.fragment.Setting.Javabean.CollarMsg;
import competition.fragment.Setting.Set_show;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Animshow extends AppCompatActivity {
    private static final String TAG = "AnimShow";
    private int petId;
    private String ip;
    private ImageView nick_pic;
    public static TextView nick;
    public static TextView kind;
    private TextView tempe;
    private TextView HR;
    private TextView step;
    private TextView link;
    private MaterialTextView Tpetid;
    public static MaterialTextView introduction;
    public static SwitchButton isovert;

    private Anim_javabean.DataBean.PetBean animal;
    private List<Anim_javabean.DataBean.PetBean.RailingsBean> ra;

    private ImagePicker imagePicker;
    private List<Anim_javabean.DataBean.PetBean.PhotosBean> imglist;
    public static ImageView imag1;
    public static ImageView imag2;
    public static ImageView imag3;
    public static ImageView imag4;
    public static ImageView imag5;

    private IntentFilter intentFilter;
    private LocalBroadcastManager manager;
    private AnimshowReceiver receiver;
    private Intent intentService;

    private AlertDialog.Builder builder;
    public static AlertDialog dialog;
    private ConstraintLayout layout;
    private MaterialTextView change_img;
    private MaterialTextView change_message;
    private MaterialTextView change_photo;
    private MaterialTextView no_act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_show);
        InitView();
        InitData1();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (intentService != null)
            stopService(intentService);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Animphotos_upload.pass == 1) {
            int start=imglist.size();
            System.out.println("go");
            for (int i = start; i < start+Animphotos_upload.list.size(); i++) {
                if (i == 0)
                    Glide.with(this).load(Animphotos_upload.list.get(i-start)).centerCrop().into(Animshow.imag1);
                if (i == 1)
                    Glide.with(this).load(Animphotos_upload.list.get(i-start)).centerCrop().into(Animshow.imag2);
                if (i == 2)
                    Glide.with(this).load(Animphotos_upload.list.get(i-start)).centerCrop().into(Animshow.imag3);
                if (i == 3)
                    Glide.with(this).load(Animphotos_upload.list.get(i-start)).centerCrop().into(Animshow.imag4);
                if (i == 4)
                    Glide.with(this).load(Animphotos_upload.list.get(i-start)).centerCrop().into(Animshow.imag5);
            }
        }
    }

    public void InitView() {
        ip = getIntent().getStringExtra("ip");
        nick_pic = (ImageView) findViewById(R.id.show_image);
        nick = (TextView) findViewById(R.id.show_nick);
        kind = (TextView) findViewById(R.id.show_kind);
        tempe = (TextView) findViewById(R.id.show_temp);
        HR = (TextView) findViewById(R.id.show_HR);
        step = (TextView) findViewById(R.id.show_step);
        link = (TextView) findViewById(R.id.show_link);
        introduction = (MaterialTextView) findViewById(R.id.show_edit);
        isovert = (SwitchButton) findViewById(R.id.show_bt1);
        Tpetid = (MaterialTextView) findViewById(R.id.show_petid);
        imag1 = (ImageView) findViewById(R.id.show_imag1);
        imag2 = (ImageView) findViewById(R.id.show_imag2);
        imag3 = (ImageView) findViewById(R.id.show_imag3);
        imag4 = (ImageView) findViewById(R.id.show_imag4);
        imag5 = (ImageView) findViewById(R.id.show_imag5);
        imglist = new ArrayList<>();
        isovert.setChecked(false);//设置为真，即默认为真
        isovert.isChecked();  //被选中
        isovert.toggle();  //开关状态
        isovert.toggle(true);//开关有动画
        isovert.setShadowEffect(false);//禁用阴影效果
        isovert.setEnabled(true);//false为禁用按钮
        isovert.setEnableEffect(true);//false为禁用开关动画

        imagePicker = new ImagePicker();
        // 设置标题
        imagePicker.setTitle("设置头像");
        // 设置是否裁剪图片
        imagePicker.setCropImage(true);

    }

    public void InitData1() {
        animal = Constanst.anim.getData().getPet();
        ra = new ArrayList<>(Constanst.anim.getData().getPet().getRailings());
        petId = animal.getPetId();
        getIp();
        nick.setText(animal.getPetName());
        kind.setText(animal.getPetBreed());
        Tpetid.setText("Petid:" + String.valueOf(petId));
        introduction.setText(animal.getPetIntroduction());
        if (animal.isOvert()) {
            isovert.setChecked(true);
        }
        Glide.with(this).load(animal.getPetPortraitPath()).centerCrop().into(nick_pic);
        InitImg();
    }

    public void InitData2(CollarMsg collar) {
        tempe.setText("体温：" + String.format("%.2f", collar.getTemp()));
        HR.setText("心率：" + String.format("%.1f", collar.getHeartrate()));
        step.setText("步数：" + collar.getStepnumber());
    }

    public void InitImg() {
        imglist = Constanst.anim.getData().getPet().getPhotos();
        for (int i = 0; i < imglist.size(); i++) {
            if (i == 0)
                Glide.with(this).load(imglist.get(i).getPetPhotoUrl()).centerCrop().into(imag1);
            if (i == 1)
                Glide.with(this).load(imglist.get(i).getPetPhotoUrl()).centerCrop().into(imag2);
            if (i == 2)
                Glide.with(this).load(imglist.get(i).getPetPhotoUrl()).centerCrop().into(imag3);
            if (i == 3)
                Glide.with(this).load(imglist.get(i).getPetPhotoUrl()).centerCrop().into(imag4);
            if (i == 4)
                Glide.with(this).load(imglist.get(i).getPetPhotoUrl()).centerCrop().into(imag5);
        }
    }


    public void getIp() {
        Gson gson = new Gson();
        API api = RetrofitManager.getRetrofit().create(API.class);
        Call<ResponseBody> task = api.get_pet_collar(Constanst.token, petId);
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:" + response.code());
                try {
                    String json = response.body().string();
                    Anim_ip_javabean body = gson.fromJson(json, Anim_ip_javabean.class);
                    int code = body.getCode();
                    if (code == 200) {
                        ip = body.getData().getCollarIp();
                        InitService();
                        link.setText("已连接项圈:" + ip);
                    } else {
                        ip = "0.0.0.0";
                        link.setText("未连接项圈");
                        Toast.makeText(Animshow.this, body.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Animshow.this, "服务器异常.....", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    public void show_back(View v) {
        finish();
    }

    public void temp_show(View view) {
        Intent intent = new Intent(this, Anim_temp_show.class);
        intent.putExtra("petId", petId);
        intent.putExtra("ip", ip);
        startActivity(intent);
    }
    public void heart_show(View view){
        Intent intent = new Intent(this, Anim_heart_show.class);
        intent.putExtra("petId", petId);
        intent.putExtra("ip", ip);
        startActivity(intent);
    }
    public void humidity_show(View view){
        Intent intent = new Intent(this, Anim_humidity_show.class);
        intent.putExtra("petId", petId);
        intent.putExtra("ip", ip);
        startActivity(intent);
    }

    public void railing(View view) {
        Intent intent = new Intent(this, Anim_railing.class);
        intent.putExtra("petId", petId);
        intent.putExtra("railing", (Serializable) ra);
        startActivity(intent);
    }

    public void Trajectory(View view) {
        Intent intent = new Intent(this, Anim_trajectory.class);
        intent.putExtra("ip", ip);
        startActivity(intent);
    }

    public void animshow_menu(View view) {
        builder = new AlertDialog.Builder(this);//创建对话框
        layout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.animshow_menu, null);//获取自定义布局
        builder.setView(layout);//设置对话框的布局
        dialog = builder.create();//生成最终的对话框
        dialog.show();//显示对话框

        change_img = layout.findViewById(R.id.anim_show_Chead);
        change_message = layout.findViewById(R.id.anim_show_Cmessage);
        no_act = layout.findViewById(R.id.anim_show_dismiss);
        change_photo = layout.findViewById(R.id.anim_show_Cphoto);

        change_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitImageView();
            }
        });

        change_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Animshow.this, Change_anim.class);
                intent.putExtra("petId", petId);
                intent.putExtra("name", animal.getPetName());
                intent.putExtra("breed", animal.getPetBreed());
                intent.putExtra("intro", animal.getPetIntroduction());
                startActivity(intent);
            }
        });

        change_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Animshow.this, Animphotos_upload.class);
                intent.putExtra("petId", petId);
                startActivity(intent);
            }
        });

        no_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void InitService() {
        InitReceiver();
        intentService = new Intent(this, AnimService.class);
        Log.d(TAG, "InitService: "+ip);
        intentService.putExtra("ip", ip);
        startService(intentService);
    }

    public void InitReceiver() {
        manager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("competition.Service.Set_show_Localbroadcast" + ip);
        receiver = new AnimshowReceiver();
        manager.registerReceiver(receiver, intentFilter);
    }

    public void InitImageView() {
        // 启动图片选择器
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override
            public void onPickImage(Uri imageUri) {
            }

            // 裁剪图片回调
            @Override
            public void onCropImage(Uri imageUri) {
                String path = getPath(imageUri);
                Gson gson = new Gson();
                UploadImag load = new UploadImag();
                Call<ResponseBody> task = load.uploadImag(path, petId, Constanst.token);
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
                               Message message=Message.obtain();
                               message.obj=path;
                               handler2.sendMessage(message);
                               dialog.dismiss();
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
        imagePicker.onActivityResult(Animshow.this, requestCode, resultCode, data);

    }

    public String getPath(Uri uri) {
        String path1 = uri.getPath();
        if (path1.charAt(0) == 'f') {
            return uri.getPath();
        } else {
            String filePath = GetPath.getPath(Animshow.this, uri);
            Log.d("str", "file" + filePath);
            if (filePath != null)
                return filePath;
            else
                return " ";
        }
    }

    class AnimshowReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new Gson();
            String json = intent.getStringExtra("json");
            Log.d("animshow", json);
            CollarMsg body = gson.fromJson(json, CollarMsg.class);
            try {
                InitData2(body);
            } catch (Exception e) {
                Toast.makeText(context, "项圈连接异常", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {//消息机制，用来在子线程中更新UI
            Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler2 = new Handler() {
        public void handleMessage(Message msg) {//消息机制，用来在子线程中更新UI
            Glide.with(Animshow.this).load(msg.obj).centerCrop().into(nick_pic);
        }
    };
}
