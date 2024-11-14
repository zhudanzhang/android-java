package com.example.demo33;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendRequest = findViewById(R.id.send_request);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_request) {

//            HttpUtil.sendHttpRequest("http://www.baidu.com", new HttpCallbackListener() {
//                @Override
//                public void onFinish(String response) {
//                    // 处理成功响应
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    // 处理异常
//                }
//            });
            
            HttpUtil.sendOkHttpRequest("http://www.baidu.com", new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // 获取服务器返回的数据
                    String responseData = response.body().string();
                    Log.d("MainActivity", responseData);
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    // 处理失败情况
                }
            });
        }
    }
}