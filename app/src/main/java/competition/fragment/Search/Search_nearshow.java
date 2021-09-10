package competition.fragment.Search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import competition.R;
import competition.Utils.API;
import competition.Utils.Constanst;
import competition.Utils.RetrofitManager;
import competition.Utils.WebSocket.CircleImageView;
import competition.fragment.Anim.Animshow;
import competition.fragment.Anim.JavaBean.Anim_javabean;
import competition.fragment.Setting.Javabean.Pet_ip_javabean;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class Search_nearshow extends AppCompatActivity {
    private CircleImageView img;
    private MaterialTextView name;
    private MaterialTextView kind;
    private MaterialTextView intro;
    private AppCompatImageView img1;
    private AppCompatImageView img2;
    private AppCompatImageView img3;
    private AppCompatImageView img4;
    private AppCompatImageView img5;
    private Pet_ip_javabean.DataBean.PetBean bean;
    private int petId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_searshow);
        InitView();
        try {
            InitData();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "数据加载异常", Toast.LENGTH_SHORT).show();
        }
    }

    public void InitView() {
        img = (CircleImageView) findViewById(R.id.search_near_img);
        name = (MaterialTextView) findViewById(R.id.search_near_name);
        kind = (MaterialTextView) findViewById(R.id.search_near_kind);
        intro = (MaterialTextView) findViewById(R.id.near_intro);
        img1 = (AppCompatImageView) findViewById(R.id.nearshow_imag1);
        img2 = (AppCompatImageView) findViewById(R.id.nearshow_imag2);
        img3 = (AppCompatImageView) findViewById(R.id.nearshow_imag3);
        img4 = (AppCompatImageView) findViewById(R.id.nearshow_imag4);
        img5 = (AppCompatImageView) findViewById(R.id.nearshow_imag5);
    }

    public void InitData() {
        bean=Constanst.petBean;
        Glide.with(this).load(bean.getPetPortraitPath()).centerCrop().into(img);
        name.setText("昵称:"+bean.getPetName());
        kind.setText("种类:"+bean.getPetBreed());
        intro.setText(bean.getPetIntroduction());
        for (int i = 0; i < bean.getPhotos().size(); i++) {
            if (i == 0)
                Glide.with(this).load(bean.getPhotos().get(i).getPetPhotoUrl()).centerCrop().into(img1);
            if (i == 1)
                Glide.with(this).load(bean.getPhotos().get(i).getPetPhotoUrl()).centerCrop().into(img2);
            if (i == 2)
                Glide.with(this).load(bean.getPhotos().get(i).getPetPhotoUrl()).centerCrop().into(img3);
            if (i == 3)
                Glide.with(this).load(bean.getPhotos().get(i).getPetPhotoUrl()).centerCrop().into(img4);
            if (i == 4)
                Glide.with(this).load(bean.getPhotos().get(i).getPetPhotoUrl()).centerCrop().into(img5);

        }
    }
}
