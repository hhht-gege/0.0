package competition.fragment.Anim;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Text;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import competition.R;
import competition.Service.railingService;
import competition.Service.tempService;
import competition.Utils.API;
import competition.Utils.Constanst;
import competition.Utils.RetrofitManager;
import competition.fragment.Anim.JavaBean.Anim_javabean;
import competition.fragment.Anim.JavaBean.railing_javabean;
import competition.fragment.Anim.JavaBean.railing_request;
import competition.fragment.Anim.View.DraggingButton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Anim_railing extends AppCompatActivity implements AMap.InfoWindowAdapter {
    private String TAG = "Anim_railing";
    private MapView mapView;
    private AMap map;
    private DraggingButton fab;
    private int petId;
    private List<Anim_javabean.DataBean.PetBean.RailingsBean> ra;
    private ArrayList<Marker> mlist;
    private ArrayList<Circle> clist;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private FrameLayout layout;
    private MaterialButton add;
    private AppCompatEditText latitude;
    private AppCompatEditText longitude;
    private AppCompatEditText radius;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment__anim_railingr);
        InitView();
        InitData();
        mapView = (MapView) findViewById(R.id.railing_map);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();
        map.setInfoWindowAdapter(this);
        if (ra != null&&ra.size()>0)
            InitRail();
        else
            Toast.makeText(this, "用户没有设置电子围栏", Toast.LENGTH_SHORT).show();

    }

    public void InitView() {
        fab = (DraggingButton) findViewById(R.id.railing_add);
        mlist=new ArrayList<>();
        clist=new ArrayList<>();
    }

    public void InitData() {
        petId = getIntent().getIntExtra("petId", -1);
        ra = (List<Anim_javabean.DataBean.PetBean.RailingsBean>) getIntent().getSerializableExtra("railing");
    }

    public void InitRail() {
        for (int i = 0; i < ra.size(); i++) {
            LatLng latLng = new LatLng(Double.parseDouble(ra.get(i).getRailingLatitude()), Double.parseDouble(ra.get(i).getRailingLongitude()));
            Circle circle = map.addCircle(new CircleOptions().
                    center(latLng).
                    radius(1000).
                    fillColor(Color.argb(50, 1, 1, 1)).
                    strokeColor(Color.argb(1, 1, 1, 1)).
                    strokeWidth(Float.parseFloat(ra.get(i).getRailingRadius())));
            Marker marker=map.addMarker(new MarkerOptions().position(latLng).title("围栏编号").snippet(ra.get(i).getRailingId()+""));
            marker.showInfoWindow();
            getInfoWindow(marker);
            mlist.add(marker);
            clist.add(circle);
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(Double.parseDouble(ra.get(ra.size() - 1).getRailingLatitude()), Double.parseDouble(ra.get(ra.size() - 1).getRailingLongitude())), 16, 0, 0));
        map.moveCamera(cameraUpdate);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    public void railing_add(View view) {
        builder = new AlertDialog.Builder(this);//创建对话框
        layout = (FrameLayout) getLayoutInflater().inflate(R.layout.fragment_anim_railing, null);//获取自定义布局
        builder.setView(layout);//设置对话框的布局
        dialog = builder.create();//生成最终的对话框
        dialog.show();//显示对话框

        add = (MaterialButton) layout.findViewById(R.id.railing_btn);
        latitude = (AppCompatEditText) layout.findViewById(R.id.railing_latitude);
        longitude = (AppCompatEditText) layout.findViewById(R.id.railing_longitude);
        radius = (AppCompatEditText) layout.findViewById(R.id.railing_radius);
        //设置监听
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                railing_create();
            }
        });
    }

    public void railing_create() {
        Gson gson = new Gson();
        double dlatitude, dlongitude, dradius;
        dlatitude = Double.parseDouble(latitude.getText().toString());
        dlongitude = Double.parseDouble(longitude.getText().toString());
        dradius = Double.parseDouble(radius.getText().toString())/3;
        railing_request request = new railing_request(petId, dlongitude, dlatitude, dradius);
        API api = RetrofitManager.getRetrofit().create(API.class);
        Call<ResponseBody> task = api.create_railing(Constanst.token, request);
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:" + response.code());
                try {
                    String json = response.body().string();
                    railing_javabean body = gson.fromJson(json, railing_javabean.class);
                    int code = body.getCode();
                    if (code == 200) {
                        LatLng latLng = new LatLng(dlatitude, dlongitude);
                        Circle circle = map.addCircle(new CircleOptions().
                                center(latLng).
                                radius(1000).
                                fillColor(Color.argb(50, 1, 1, 1)).
                                strokeColor(Color.argb(1, 1, 1, 1)).
                                strokeWidth((float) dradius));
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 16, 0, 0));
                        map.moveCamera(cameraUpdate);
                        Marker marker=map.addMarker(new MarkerOptions().position(latLng).title("围栏编号").snippet(body.getData().getRailingId()+""));
                        marker.showInfoWindow();
                        getInfoWindow(marker);
                        mlist.add(marker);
                        clist.add(circle);
                        dialog.dismiss();
                    } else {
                        String msg = body.getMsg();
                        Toast.makeText(Anim_railing.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Anim_railing.this, "服务器异常。。。", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure:" + t.getMessage());
            }
        });
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow =getLayoutInflater().from(this).inflate(R.layout.railingid,null);
        render(marker,infoWindow);
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
    public void render(Marker marker, View view) {
        String title = marker.getTitle();
        TextView titleUi = (TextView) view.findViewById(R.id.title);
        titleUi.setText(title);
        String snippet = marker.getSnippet();
        TextView snippetUi = (TextView) view.findViewById(R.id.snippet);
        snippetUi.setText(snippet);
    }
}
