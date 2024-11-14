package com.example.demo35;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService", "onCreate executed");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "your_channel_id";
            String channelName = "Your Channel Name";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // 创建一个意图，用于启动 MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

        // 构建通知
        Notification notification = new NotificationCompat.Builder(this, "your_channel_id")
                .setContentTitle("This is content title")   // 通知标题
                .setContentText("This is content text")     // 通知内容
                .setWhen(System.currentTimeMillis())        // 通知时间
                .setSmallIcon(R.mipmap.ic_launcher)         // 小图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))                 // 大图标
                .setContentIntent(pi)                      // 设置点击行为
                .build();

        // 启动前台服务
        startForeground(1, notification);
    }
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Log.d("MyService", "onCreate executed");
//    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyService", "onStartCommand executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("MyService", "onDestroy executed");
        super.onDestroy();
    }

    private DownloadBinder mBinder = new DownloadBinder();

    class DownloadBinder extends Binder {
        public void startDownload() {
            Log.d("MyService", "startDownload executed");
        }

        public int getProgress() {
            Log.d("MyService", "getProgress executed");
            return 0; // 模拟返回进度
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder; // 返回Binder实例
    }
}