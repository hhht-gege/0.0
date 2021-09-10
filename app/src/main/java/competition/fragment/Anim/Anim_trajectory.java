package competition.fragment.Anim;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import competition.R;
import competition.Utils.API;
import competition.Utils.Constanst;
import competition.Utils.RetrofitManager;
import competition.fragment.Anim.JavaBean.Anim_history_javabean;
import competition.fragment.Anim.View.Douglas;
import competition.fragment.Anim.View.LatLngPoint;
import competition.fragment.Anim.View.SportTrailView;
import competition.fragment.Anim.View.ViewUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Anim_trajectory extends AppCompatActivity {
    private static final String TAG = "Anim_trajectory";
    private MapView mapView;
    private AMap map;
    private Set<LatLng> hashSet = new LinkedHashSet<>();
    private ArrayList<LatLng> latLngs;
    private Polyline polyline;
    private TimePickerView pvTime;
    private String ip;
    private AppCompatImageButton btn;
    private SportTrailView sportTrailView;

    private int tmp = 0;

    private int into = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_trajectory);
        sportTrailView = (SportTrailView) findViewById(R.id.sportview);
        mapView = (MapView) findViewById(R.id.map_trajectory);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();
        latLngs = new ArrayList<>();
        ip = getIntent().getStringExtra("ip");
        Date date = new Date(System.currentTimeMillis());
        String day = getTime(date);
        Inittrajectory(day);
        InitTimePicker();
        btn = (AppCompatImageButton) findViewById(R.id.anim_trajectory_set);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击了");
                pvTime.show(v);
            }
        });
        map.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                //展示动画的时候移动了地图，取消展示动画
                if (tmp != 0)
                    sportTrailView.setVisibility(View.GONE);
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                //如果轨迹动画隐藏了，展示完整轨迹
                if (latLngs.size() > 0 && tmp != 0 &&sportTrailView.getVisibility()==View.GONE)
                    drawpoline(latLngs);

                if (latLngs.size() > 0)
                    tmp++;

                //等待地图加载完后再展示
                if (sportTrailView.getVisibility() == View.VISIBLE) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //移动完毕后开始展示轨迹动画
                if (into == 1) {
                    display(latLngs);
                    into = 0;
                }
            }
        });
    }


    public void Inittrajectory(String day) {
        latLngs.clear();
        map.clear();
        Gson gson = new Gson();
        API api = RetrofitManager.getRetrofit().create(API.class);
        System.out.println(ip);
        Call<ResponseBody> task = api.collar_history(Constanst.token, ip, day);
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:" + response.code());
                try {
                    String json = response.body().string();
                    Log.d(TAG, json);
                    Anim_history_javabean body = gson.fromJson(json, Anim_history_javabean.class);
                    int code = body.getCode();
                    if (code == 200) {
                        bezero();
                        for (Anim_history_javabean.DataBean.CollarMsgsBean bean : body.getData().getCollarMsgs()
                        ) {
                            latLngs.add(new LatLng(bean.getLatitude(), bean.getLongitude()));
                        }
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (LatLng p : latLngs) {
                            builder.include(p);
                        }
                        LatLngBounds latLngBounds = builder.build();
                        if (latLngs.size() > 0) {
                            map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10));
                            into = 1;
                        } else {
                            Toast.makeText(Anim_trajectory.this, "没有" + day + "的轨迹", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Anim_trajectory.this, body.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Anim_trajectory.this, "服务器异常.....", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void bezero(){
        sportTrailView.setVisibility(View.VISIBLE);
        tmp=0;
    }

    public void drawpoline(ArrayList<LatLng> latLngs) {
        polyline = map.addPolyline(new PolylineOptions().
                addAll(latLngs).width(8).color(Color.parseColor("#9999FF")));
    }

    public void display(ArrayList<LatLng> latLngs) {
        Douglas douglas = new Douglas(latLngs, 10);
        ArrayList<LatLng> mPont = douglas.compress();
        ArrayList<Point> mPositions = new ArrayList<>();
        for (int i = 0; i < mPont.size(); i++) {
            Point p = map.getProjection().toScreenLocation(mPont.get(i));
            mPositions.add(p);
        }

        sportTrailView.drawSportLine(mPositions, R.drawable.ic_outside_run_record_start_point, R.drawable.ic_speed_view_point, new SportTrailView.OnTrailChangeListener() {
            @Override
            public void onFinish() {
                System.out.println("写完咯");
//                sportTrailView.setVisibility(View.GONE);
//                drawpoline(latLngs);
            }
        });
    }


    public void InitTimePicker() {
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String the_date = getTime(date);
                Inittrajectory(the_date);
                Log.i("pvTime", "onTimeSelect");

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{false, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .setItemVisibleCount(3) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
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

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

}
