package com.example.demo15;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private IntentFilter intentFilter;
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取 LocalBroadcastManager 实例
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        // 设置按钮点击事件，发送本地广播
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.demo15.LOCAL_BROADCAST");
                localBroadcastManager.sendBroadcast(intent);  // 发送本地广播
            }
        });

        // 注册本地广播接收器
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.demo15.LOCAL_BROADCAST");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter); // 动态注册
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消注册接收器
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    // 定义本地广播接收器
    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "received local broadcast", Toast.LENGTH_SHORT).show();
        }
    }
}