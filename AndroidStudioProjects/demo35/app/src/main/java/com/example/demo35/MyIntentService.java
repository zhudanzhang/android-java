package com.example.demo35;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");  // 调用父类构造函数，指定线程名称
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // 在子线程中处理耗时逻辑
        Log.d("MyIntentService", "Thread id is " + Thread.currentThread().getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyIntentService", "onDestroy executed");  // 服务任务完成后自动停止
    }
}