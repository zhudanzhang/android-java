package com.example.demo01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);

        // 按钮点击事件，返回数据到 FirstActivity
        Button returnResultButton = findViewById(R.id.button_2);
        returnResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建 Intent，存放返回的数据
                Intent resultIntent = new Intent();
                resultIntent.putExtra("key", "Returned Value");

                // 设置结果并返回
                setResult(Activity.RESULT_OK, resultIntent);
                finish(); // 结束活动，返回上一个活动
            }
        });
    }
}