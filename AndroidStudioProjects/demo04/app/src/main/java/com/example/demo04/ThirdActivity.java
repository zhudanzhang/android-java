package com.example.demo04;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ThirdActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.third_layout);

            Button button3 = findViewById(R.id.button_3);  // 修正 button id
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCollector.finishAll();  // 销毁所有活动
                    android.os.Process.killProcess(android.os.Process.myPid());  // 杀掉当前进程
                }
            });
    }
}