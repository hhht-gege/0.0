package competition.fragment.Search;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import competition.R;
import competition.Service.MyService;
import competition.Service.nearService;
import competition.Socket.Socket_mapjavabean;
import competition.Socket.Socketjavabean;
import competition.Utils.API;
import competition.Utils.Constanst;
import competition.Utils.RetrofitManager;
import competition.Utils.WebSocket.CircleImageView;
import competition.fragment.Setting.Javabean.Pet_ip_javabean;
import competition.fragment.Setting.Set_show;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search_near extends AppCompatActivity implements AMap.InfoWindowAdapter {
    private static final String TAG = "Search_near";
    private MapView mapView;
    private AMap map;
    private String ip;
    private List<Pet_ip_javabean.DataBean.PetBean> anims;
    private IntentFilter intentFilter;
    private LocalBroadcastManager manager;
    private mapReceiver receiver;
    private Intent intentService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_near);
        anims = new ArrayList<>();
        mapView = (MapView) findViewById(R.id.map_near);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();
        Location();
        map.setInfoWindowAdapter(this);
        AMap.OnInfoWindowClickListener listener = new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(Search_near.this, Search_nearshow.class);
                Constanst.petBean=(Pet_ip_javabean.DataBean.PetBean)marker.getObject();
                startActivity(intent);
            }
        };
        map.setOnInfoWindowClickListener(listener);
        ip = Constanst.firstip;
        System.out.println("cons" + ip);
        if (ip!=null)
            InitService();
        else
            Toast.makeText(this, "用户没有绑定项圈，无法查看附近牲畜", Toast.LENGTH_SHORT).show();
    }

    public void InitService() {
        InitReceiver();
        intentService = new Intent(this, nearService.class);
        startService(intentService);
    }

    public void InitReceiver() {
        manager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("competition.Service.near_Localbroadcast" + ip);
        receiver = new mapReceiver();
        manager.registerReceiver(receiver, intentFilter);
    }

    public void InitDate(List<Socket_mapjavabean> bean) {
        System.out.println(bean.size());
        System.out.println(anims.size());
        for (int i = 0; i < anims.size(); i++) {
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(bean.get(i).getLatitude(), bean.get(i).getLongitude()))
                    .title(anims.get(i).getPetName())
                    .snippet(anims.get(i).getPetPortraitPath()));
            marker.setObject(anims.get(i));
            marker.showInfoWindow();
            getInfoWindow(marker);
        }
    }

    public void Location() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        map.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        map.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }


    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater().from(this).inflate(R.layout.item_overt, null);
        render(marker, infoWindow);
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    public void render(Marker marker, View view) {
        String title = marker.getTitle();
        CircleImageView titleUi = (CircleImageView) view.findViewById(R.id.overt_userimg);
//        如果不判断活动是否销毁，会有异常产生
        if (!isDestroy(this)) {
            Glide.with(this).load(marker.getSnippet()).into(titleUi);
        }
        String snippet = marker.getTitle();
        MaterialTextView snippetUi = (MaterialTextView) view.findViewById(R.id.overt_username);
        snippetUi.setText(snippet);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(intentService!=null)
        stopService(intentService);
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

    public class mapReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new Gson();
            String json = intent.getStringExtra("json");
            Log.d("map", json);
            anims.clear();
            List<Socket_mapjavabean> body = gson.fromJson(json, new TypeToken<List<Socket_mapjavabean>>() {
            }.getType());
            System.out.println("body的size"+body.size());
            for (Socket_mapjavabean dog : body
            ) {
                try {
                    getpet(dog.getIp());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                InitDate(body);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void getpet(String ip) throws InterruptedException {
            Log.d(TAG, "getpet: " + ip);
            Gson gson = new Gson();
            API api = RetrofitManager.getRetrofit().create(API.class);
            Call<ResponseBody> task = api.getPetByIp(ip);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response<ResponseBody> response = task.execute();
                        String json = response.body().string();
                        Log.d(TAG, "onResponse: " + json);
                        Pet_ip_javabean bean = gson.fromJson(json, Pet_ip_javabean.class);
                        int code = bean.getCode();
                        if (code == 200) {
                            anims.add(bean.getData().getPet());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            thread.join();
        }
    }

    /**
     * 判断Activity是否Destroy
     *
     * @param
     * @return
     */
    public static boolean isDestroy(Activity mActivity) {
        if (mActivity == null || mActivity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }

}
