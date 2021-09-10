package competition.Service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.concurrent.TimeUnit;

import competition.R;
import competition.fragment.Me.MeFragment;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class tempService extends Service {
    private String json;
    private static final int NOTIF_ID=2;
    private static final String CHANNEL_ID = "com.competition.notification.channel.temp";

    private IntentFilter intentFilter;
    private LocalBroadcastManager manager;
    private tempReceiver receiver;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        setForegroundService();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setForegroundService();
        InitBroad();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void InitBroad() {
        manager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("competition.Service.temp_Localbroadcast");
        receiver = new tempReceiver();
        manager.registerReceiver(receiver, intentFilter);
    }

    /**
     *通过通知启动服务
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @TargetApi(Build.VERSION_CODES.N)
    public void  setForegroundService()
    {
        //设定的通知渠道名称
        String channelName = getString(R.string.app_name);
        //设置通知的重要程度
        int importance = NotificationManager.IMPORTANCE_HIGH;
        //构建通知渠道
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
        //在创建的通知渠道上发送通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher) //设置通知图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setContentTitle("体温检测 ")//设置通知标题
                .setContentText("体温开始检测咯！")//设置通知内容
                .setOngoing(true);//设置处于运行状态
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        startForeground(NOTIF_ID,builder.build());
    }


    public class tempReceiver extends BroadcastReceiver {
        private String ip;
        private String petname;
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("temprecevier", "hello");
            json = intent.getStringExtra("json");
            ip=intent.getStringExtra("ip");
            petname=intent.getStringExtra("name");
            updateNotification(petname+"的体温太高啦",context);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private Notification getMyActivityNotification(String text,Context context) {
            //设定的通知渠道名称
            String channelName = String.valueOf(R.string.app_name);
            //设置通知的重要程度
            int importance = NotificationManager.IMPORTANCE_HIGH;
            //构建通知渠道-
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
//            channel.enableVibration(true);
//            channel.setVibrationPattern(new long[]{1000, 2000});
            return new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("体温检控")
                    .setContentText(text)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .getNotification();
        }

        /**
         * This is the method that can be called to update the Notification
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        private void updateNotification(String text,Context context) {

            Notification notification = getMyActivityNotification(text,context);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIF_ID, notification);
        }
    }

}
