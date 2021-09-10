package competition.fragment.Me;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;




import competition.R;
import competition.Utils.Constanst;
import competition.Utils.UU;
import competition.Utils.WebSocket.CircleImageView;
import competition.fragment.Anim.JavaBean.Imag_javabean;
import competition.fragment.Me.JavaBean.userPortraitPath_javabean;
import competition.fragment.Search.SearchAdd;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MeFragment extends Fragment {
    private static String TAG = "MeFragment";
    private String token;
    ImagePicker imagePicker;

    private MaterialTextView usernick;
    private MaterialTextView introduction;
    private TextView add_image;
    private CircleImageView imageView;

    private String user_pic;
    private String introduction_str;
    private String usernick_str;

    private Button quit;
    private Button change;

    //调取系统摄像头的请求码
    private static final int MY_ADD_CASE_CALL_PHONE = 6;
    //打开相册的请求码
    private static final int MY_ADD_CASE_CALL_PHONE2 = 7;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LinearLayout layout;
    private TextView takePhotoTV;
    private TextView choosePhotoTV;
    private TextView cancelTV;

    public MeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        token = Constanst.token;
        usernick = (MaterialTextView) view.findViewById(R.id.username_me);
        introduction = (MaterialTextView) view.findViewById(R.id.introduce_me);
        imageView = (CircleImageView) view.findViewById(R.id.image_me);
        Glide.with(getContext()).load(UU.userPortraitPath).centerCrop().into(imageView);
        usernick.setText(UU.username);
        introduction.setText(UU.userIntroduction);
        imagePicker = new ImagePicker();
        imagePicker.setTitle("设置头像");
        imagePicker.setCropImage(true);
        add_image(view);
        back(view);
        change(view);

        return view;
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {//消息机制，用来在子线程中更新UI
            Toast.makeText(getContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
        }
    };

    public void Upload_imag(String str, String token) throws InterruptedException {

        Gson gson = new Gson();
        Uploadme load = new Uploadme();
        Call<ResponseBody> task = load.uploadImag(str, token);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = task.execute();
                    String json = response.body().string();
                    Log.d(TAG, json);
                    userPortraitPath_javabean body = gson.fromJson(json, userPortraitPath_javabean.class);
                    int code = body.getCode();
                    if (code == 200) {
                        Message message = Message.obtain();//更新UI就要向消息机制发送消息
                        message.obj = body.getMsg();//消息主体
                        handler.sendMessage(message);
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
    }

    public void back(View view) {
        quit = (Button) view.findViewById(R.id.me_back);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "back");
                getActivity().finish();
            }
        });
    }

    public void change(View view) {
        change = (Button) view.findViewById(R.id.me_change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Change_me.class);
                intent.putExtra("name",usernick.getText().toString());
                intent.putExtra("intro",introduction.getText().toString());
                startActivity(intent);
            }
        });
    }

    public void add_image(View view) {
        add_image = (TextView) view.findViewById(R.id.me_addimage);

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              opnePhoto();
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
                    user_pic = getPath(imageUri);
                   try {
                       Glide.with(getActivity()).load(user_pic).into(imageView);
                       Upload_imag(user_pic,token);
                   }catch (Exception e){
                       e.printStackTrace();
                   }
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
                        .setRequestedSize(960, 540)
                        // 宽高比
                        .setAspectRatio(16, 16);
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
    public void onActivityResult(int requestCode, int
            resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(MeFragment.this, requestCode, resultCode, data);

    }

    public String getPath(Uri uri) {
        String path = uri.toString();
        String[] str = path.split("//");
        for (String string : str) {
            Log.d("str", string);
        }
        return str[1];
    }

}
//    /*
//初始化控件方法
// */
//    public void viewInit() {
//        builder = new AlertDialog.Builder(getActivity());//创建对话框
//        layout =(LinearLayout) getLayoutInflater().inflate(R.layout.dialog_select_photo, null);//获取自定义布局
//        builder.setView(layout);//设置对话框的布局
//        dialog = builder.create();//生成最终的对话框
//        dialog.show();//显示对话框
//
//        takePhotoTV = layout.findViewById(R.id.photograph);
//        choosePhotoTV = layout.findViewById(R.id.photo);
//        cancelTV = layout.findViewById(R.id.cancel);
//        //设置监听
//        takePhotoTV.setOnClickListener(this);
//        choosePhotoTV.setOnClickListener(this);
//        cancelTV.setOnClickListener(this);
//    }
//
//
//
//
//    private void takePhoto() throws IOException {
//        Intent intent = new Intent();
//        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//        // 获取文件
//        File file = createFileIfNeed("UserIcon.png");
//        //拍照后原图回存入此路径下
//        Uri uri;
//        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
//            uri = Uri.fromFile(file);
//        }else {
//            /**
//             * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
//             * 并且这样可以解决MIUI系统上拍照返回size为0的情况
//             */
//            uri = FileProvider.getUriForFile(getContext(), "com.example.competition.fileProvider", file);
//        }
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//        startActivityForResult(intent, 1);
//    }
//
//    // 在sd卡中创建一保存图片（原图和缩略图共用的）文件夹
//    private File createFileIfNeed(String fileName) throws IOException {
//        String fileA = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/nick_pic";
//        File fileJA = new File(fileA);
//        if (!fileJA.exists()) {
//            fileJA.mkdirs();
//        }
//        File file = new File(fileA, fileName);
//        if (!file.exists()) {
//            file.createNewFile();
//        }
//        return file;
//    }
//
//    /**
//     * 打开相册
//     */
//    private void choosePhoto() {
//        //这是打开系统默认的相册(就是你系统怎么分类,就怎么显示,首先展示分类列表)
//        Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(picture, 2);
//    }
//
//    /**
//     * 申请权限回调方法
//     *
//     * @param requestCode
//     * @param permissions
//     * @param grantResults
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == MY_ADD_CASE_CALL_PHONE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                try {
//                    takePhoto();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Toast.makeText(getActivity(),"拒绝了你的请求",Toast.LENGTH_SHORT).show();
//                //"权限拒绝");
//                // TODO: 2018/12/4 这里可以给用户一个提示,请求权限被拒绝了
//            }
//        }
//        if (requestCode == MY_ADD_CASE_CALL_PHONE2) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                choosePhoto();
//            } else {
//                Toast.makeText(getActivity(),"拒绝了你的请求",Toast.LENGTH_SHORT).show();
//                //"权限拒绝");
//                // TODO: 2018/12/4 这里可以给用户一个提示,请求权限被拒绝了
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //照相
//        if (requestCode == 1 && resultCode != Activity.RESULT_CANCELED) {
//            String state = Environment.getExternalStorageState();
//            if (!state.equals(Environment.MEDIA_MOUNTED)) return;
//            // 把原图显示到界面上
//            File file=new File(readpic());
//            Glide.with(getActivity()).load(file).into(imageView);
//            user_pic=readpic();
//            try {
//                Upload_imag(user_pic,token);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK  //相册
//                && null != data) {
//            try {
//                Uri selectedImage = data.getData();//获取路径
//                Glide.with(getActivity()).load(selectedImage).asBitmap().into(imageView);
//                user_pic= RealPathFromUriUtils.getRealPathFromUri(getContext(),selectedImage);
//                try {
//                    Upload_imag(user_pic,token);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            } catch (Exception e) {
//                //"上传失败");
//            }
//        }
//
//    }
//
//    /**
//     * 从保存原图的地址读取图片
//     */
//    private String readpic() {
//        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/nick_pic/" + "UserIcon.png";
//        return filePath;
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.photograph:
//                //"点击了照相";
//                //  6.0之后动态申请权限 摄像头调取权限,SD卡写入权限
//                //判断是否拥有权限，true则动态申请
//                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
//                        && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(getActivity(),
//                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            MY_ADD_CASE_CALL_PHONE);
//                } else {
//                    try {
//                        //有权限,去打开摄像头
//                        takePhoto();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                dialog.dismiss();
//                break;
//            case R.id.photo:
//                //"点击了相册";
//                if (ContextCompat.checkSelfPermission(getActivity(),
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(getActivity(),
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            MY_ADD_CASE_CALL_PHONE2);
//                } else {
//                    //打开相册
//                    choosePhoto();
//                }
//                dialog.dismiss();
//                break;
//
//            case R.id.cancel:
//                dialog.dismiss();//关闭对话框
//                break;
//            default:break;
//        }
//    }
