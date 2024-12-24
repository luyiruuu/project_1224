package com.example.project_1224;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 這是您的啟動畫面佈局檔案

        // 使用 Handler 延遲跳轉
        new Handler().postDelayed(() -> {
            // 跳轉到新的 Activity，例如 bill
            Intent intent = new Intent(MainActivity.this,select_function.class);
            startActivity(intent);
            finish(); // 關閉 MainActivity，避免返回到啟動畫面
        }, 2000); // 延遲 2 秒（2000 毫秒）
    }
}
