package com.example.demo28;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button play = findViewById(R.id.play);
        Button pause = findViewById(R.id.pause);
        Button stop = findViewById(R.id.stop);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            initMediaPlayer(); // 初始化MediaPlayer
        }
    }

    private void initMediaPlayer() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "music.mp3");
            if (file.exists()) { // 检查文件是否存在
                mediaPlayer.setDataSource(file.getPath()); // 指定音频文件的路径
                mediaPlayer.prepare(); // 让MediaPlayer进入到准备状态
            } else {
                Toast.makeText(this, "音频文件不存在", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMediaPlayer();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                try {
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start(); // 开始播放
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "播放失败，请重试", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.pause:
                try {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause(); // 暂停播放
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "暂停失败，请重试", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.stop:
                try {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop(); // 停止播放
                        mediaPlayer.prepare(); // 准备重新播放
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "停止失败，请重试", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release(); // 释放MediaPlayer资源
        }
    }
}