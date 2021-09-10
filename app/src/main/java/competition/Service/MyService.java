package competition.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import competition.Socket.MSocket;
import competition.fragment.Setting.Set_show;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


public class MyService extends Service {
    private String ip;
    private LocalBroadcastManager manager;
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService", "onCreate executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ip = intent.getStringExtra("ip");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InitBroad();
                System.out.println("发送！");
            }
        }, 1000, 5000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyService", "OnDestroy executed");
        timer.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void InitBroad() {
        manager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent("competition.Service.recent_Localbroadcast" + ip);
        intent.putExtra("recent", "recent");
        manager.sendBroadcast(intent);
    }
}