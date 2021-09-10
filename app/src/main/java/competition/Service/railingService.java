package competition.Service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import competition.MainActivity_ViewBinding;
import competition.R;
import competition.fragment.Me.MeFragment;
import competition.fragment.Setting.Javabean.CollarMsg;
import competition.fragment.Setting.Set_show;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class railingService extends Service {
    private static final int NOTIF_ID = 1;
    private static final String CHANNEL_ID = "com.competition.notification.channel";
    private static String json;

    private IntentFilter intentFilter;
    private LocalBroadcastManager manager;
    private railingReceiver receiver;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("railingService", "onCreate: railingservice");
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("railingService", "被关掉了呜呜呜呜");
    }

    public void InitBroad() {
        manager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("competition.Service.railing_Localbroadcast");
        receiver = new railingReceiver();
        manager.registerReceiver(receiver, intentFilter);
    }


    /**
     * 通过通知启动服务
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @TargetApi(Build.VERSION_CODES.N)
    public void setForegroundService() {
        //设定的通知渠道名称
        String channelName = getString(R.string.app_name);
        //设置通知的重要程度
        int importance = NotificationManager.IMPORTANCE_HIGH;
        //构建通知渠道
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
        //在创建的通知渠道上发送通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher) //设置通知图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle("电子围栏")//设置通知标题
                .setContentText("电子围栏预警已开启")//设置通知内容
                .setOngoing(true);//设置处于运行状态
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        startForeground(NOTIF_ID, builder.build());
    }


    public  class railingReceiver extends BroadcastReceiver {
        private String ip;
        private String petname;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("railingrecevier", "i need");
            json = intent.getStringExtra("json");
            ip=intent.getStringExtra("ip");
            petname=intent.getStringExtra("name");
            updateNotification(petname+"越界了，快去找到它",context);
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
                    .setContentTitle("电子围栏")
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
