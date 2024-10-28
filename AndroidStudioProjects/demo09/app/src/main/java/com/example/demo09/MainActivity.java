package com.example.demo09;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        // 初始加载 RightFragment
        replaceFragment(new RightFragment());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                // 替换为 AnotherRightFragment
                replaceFragment(new AnotherRightFragment());

                // 1. 在活动中调用碎片的方法
                // 尝试获取 RightFragment 实例并调用方法
//                Fragment rightFragment = getSupportFragmentManager().findFragmentById(R.id.right_layout);
//                if (rightFragment instanceof RightFragment) {
//                    ((RightFragment) rightFragment).showMessage("Hello from MainActivity!");
//                } else {
//                    Toast.makeText(this, "RightFragment is not available", Toast.LENGTH_SHORT).show();
//                }
                break;
            default:
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.right_layout, fragment);
        // 将事务添加到返回栈
        transaction.addToBackStack(null);
        // 提交事务
        transaction.commit();
    }
// 2. 调用 MainActivity 的方法
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}