package com.example.project_1224;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class select_function extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_function); // 使用對應的佈局檔案

        // 找到 bill 按鈕
        Button billButton = findViewById(R.id.btn_bill);
        Button ViewLocationButton = findViewById(R.id.btn_ViewLocation);
        Button chartButton = findViewById(R.id.btn_chart);
        Button StockButton = findViewById(R.id.btn_Stock);

        // 設置按鈕點擊事件
        billButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳轉到 bill Activity
                Intent intent = new Intent(select_function.this, bill.class);
                startActivity(intent);
            }
        });

        ViewLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳轉到 ViewLocation Activity
                Intent intent = new Intent(select_function.this, ViewLocation.class);
                startActivity(intent);
            }
        });

        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳轉到 chart Activity
                Intent intent = new Intent(select_function.this, Chart.class);
                startActivity(intent);
            }
        });

        StockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳轉到 bill Activity
                Intent intent = new Intent(select_function.this, API.class);
                startActivity(intent);
            }
        });
    }
}

