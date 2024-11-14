package com.example.demo34;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);

        // 执行 AsyncTask
        new UpdateTextTask().execute();
    }

    // 创建 AsyncTask 类
    private class UpdateTextTask extends AsyncTask<Void, Void, String> {

        // 该方法在后台线程执行
        @Override
        protected String doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);  // 模拟耗时操作
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "更新UI";  // 返回结果
        }

        // 该方法在主线程执行，用于更新 UI
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            textView.setText(result);  // 更新 UI
        }
    }
}