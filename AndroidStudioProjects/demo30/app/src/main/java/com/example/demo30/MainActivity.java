package com.example.demo30;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.web_view);

        // 启用JavaScript支持
        webView.getSettings().setJavaScriptEnabled(true);

        // 设置WebViewClient
        webView.setWebViewClient(new WebViewClient());

        // 加载网址
        webView.loadUrl("https://cn.bing.com/?mkt=zh-CN");
    }
}