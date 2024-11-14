package com.example.demo13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(v -> {
            // 发送给 demo14
            Intent intentToDemo14 = new Intent("com.example.demo13.MY_BROADCAST");
            intentToDemo14.setPackage("com.example.demo14"); // 设置目标应用的包名
            sendOrderedBroadcast(intentToDemo14, null); // 发送给 demo14

            Intent intent = new Intent(this, MyBroadcastReceiver.class);
            intent.setAction("com.example.demo13.MY_BROADCAST");
            sendOrderedBroadcast(intent, null); // 发送给自己
        });


    }
}