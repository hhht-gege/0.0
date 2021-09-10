package competition.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Timer;
import java.util.TimerTask;

public class AnimService extends Service {

    private String ip;
    private LocalBroadcastManager manager;
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("AnimService", "onCreate executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            ip = intent.getStringExtra("ip");
        } catch (Exception e) {
            e.printStackTrace();
            ip = "127.0.0.1";
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InitBroad();
                System.out.println("anim 发送！");
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
        Intent intent = new Intent("competition.Service.animshow_Localbroadcast" + ip);
        Log.d("AnimService", "   " + ip);
        manager.sendBroadcast(intent);
    }
}
