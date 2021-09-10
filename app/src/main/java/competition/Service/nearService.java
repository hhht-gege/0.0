package competition.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Timer;
import java.util.TimerTask;

import competition.Utils.Constanst;

public class nearService extends Service {
    private String ip;
    private LocalBroadcastManager manager;
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        ip= Constanst.firstip;
        Log.d("nearService","onCreate executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InitBroad();
                System.out.println("地图发送！");
            }
        },1000,60000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("nearService","OnDestroy executed");
        timer.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void InitBroad(){
        manager = LocalBroadcastManager.getInstance(this);
        Intent intent=new Intent("competition.Service.map_Localbroadcast" + ip);
        System.out.println("competition.Service.map_Localbroadcast"+ip);
        manager.sendBroadcast(intent);
    }
}
