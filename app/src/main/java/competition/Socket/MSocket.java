package competition.Socket;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Timer;

import competition.R;
import competition.Service.railingService;
import competition.Utils.API;
import competition.Utils.Constanst;
import competition.Utils.RetrofitManager;
import competition.fragment.Setting.Javabean.Pet_ip_javabean;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import retrofit2.Call;

public class MSocket extends WebSocketListener {
    private Context context;
    private LocalBroadcastManager manager;
    private Socketjavabean body;
    private WebSocket webSocket;
    private String ip;
    private String petname;

    private static int rflag; //1,2

    private IntentFilter recentFilter;
    private recentReceiver recentReceiver;

    private IntentFilter mapFilter;
    private mapReceiver mapReceiver;

    private IntentFilter animFilter;
    private animReceiver animReceiver;

    public MSocket(Context context, String ip) {
        this.context = context;
        manager = LocalBroadcastManager.getInstance(context);
        this.ip = ip;
        Log.d("oncreate",ip);
        InitRbroad();
        InitMbroad();
        InitAbroad();
    }

    public void InitRbroad() {
        recentFilter = new IntentFilter();
        recentFilter.addAction("competition.Service.recent_Localbroadcast" + ip);
        recentReceiver = new recentReceiver();
        manager.registerReceiver(recentReceiver, recentFilter);
        rflag = 0;
    }

    public void InitMbroad() {
        mapFilter = new IntentFilter();
        mapFilter.addAction("competition.Service.map_Localbroadcast" + ip);
        mapReceiver = new mapReceiver();
        manager.registerReceiver(mapReceiver, mapFilter);
        rflag = 0;
    }

    public void InitAbroad() {
        animFilter = new IntentFilter();
        animFilter.addAction("competition.Service.animshow_Localbroadcast"+ip);
        animReceiver = new animReceiver();
        manager.registerReceiver(animReceiver, animFilter);
        rflag = 0;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.d("Socket", "open");
        this.webSocket = webSocket;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.d("onMessage: ", text+ip);
        if (rflag == 1) {
          //  Log.d("ronMessage", text + ip);
            Intent intent = new Intent("competition.Service.Set_show_Localbroadcast" + ip);
            intent.putExtra("json", text);
            manager.sendBroadcast(intent);
            rflag = 0;
        } else if (rflag == 2) {
           // Log.d("monMessage", text + ip);
            Intent intent = new Intent("competition.Service.near_Localbroadcast" +ip);
            intent.putExtra("json", text);
            manager.sendBroadcast(intent);
            rflag = 0;
        } else {
            Gson gson = new Gson();
            body = gson.fromJson(text, Socketjavabean.class);
            try {
                if(petname==null)
                    getpet(ip);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(body.getIp());
            if (body.getFromRailing() == null) {
                Intent intent = new Intent("competition.Service.temp_Localbroadcast");
                intent.putExtra("json", text);
                intent.putExtra("ip", ip);
                intent.putExtra("name",petname);
                manager.sendBroadcast(intent);
            } else {
                Intent intent = new Intent("competition.Service.railing_Localbroadcast");
                intent.putExtra("json", text);
                intent.putExtra("ip", ip);
                intent.putExtra("name",petname);
                Log.d("railing","牲畜越界");
                manager.sendBroadcast(intent);
            }
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        Log.d("onMessage byteString: ", bytes.toString());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.d("onClosing: ", code + "/" + reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Log.d("onClosed: ", code + "/" + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.d("onFailure: ", t.getMessage());
    }

    public  class recentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("recent", "onReceive: " +ip);
            webSocket.send("recent");
            rflag = 1;
        }
    }

    public class mapReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("maps", "onReceive: "+ip);
            webSocket.send("map");
            Log.d("maps", "发送了map哦");
            rflag=2;
        }
    }

    public  class animReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("recent", "onReceive: " +ip);
            webSocket.send("recent");
            rflag = 1;
        }
    }

    public void getpet(String ip) throws InterruptedException {
          Log.d("12345678", "getpet: " + ip);
        Gson gson = new Gson();
        API api = RetrofitManager.getRetrofit().create(API.class);
        Call<ResponseBody> task = api.getPetByIp(ip);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    retrofit2.Response<ResponseBody> response = task.execute();
                    String json = response.body().string();
                     Log.d("1234568", "onResponse: " + json);
                    Pet_ip_javabean bean = gson.fromJson(json, Pet_ip_javabean.class);
                    int code = bean.getCode();
                    if (code == 200) {
                        petname = bean.getData().getPet().getPetName();
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
