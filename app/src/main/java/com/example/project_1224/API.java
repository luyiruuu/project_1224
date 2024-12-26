package com.example.project_1224;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class API extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api);

        findViewById(R.id.btn_search).setOnClickListener(v -> {
            // TWSE API URL
            String URL = "https://openapi.twse.com.tw/v1/exchangeReport/STOCK_DAY_ALL";

            Request request = new Request.Builder().url(URL).build();
            OkHttpClient okHttpClient = new OkHttpClient();  // 使用安全的 OkHttpClient

            // 發送 API 請求
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.code() == 200) {
                        if (response.body() == null) return;

                        // 解析回應資料
                        String responseBody = response.body().string();
                        Type listType = new TypeToken<List<StockData>>() {}.getType();
                        List<StockData> stockList = new Gson().fromJson(responseBody, listType);

                        // 顯示前 5 筆資料
                        final String[] items = new String[Math.min(stockList.size(), 20)];
                        for (int i = 0; i < items.length; i++) {
                            items[i] = "股票代號：" + stockList.get(i).Code + "\n" +
                                    "股票名稱：" + stockList.get(i).Name + "\n" +
                                    "收盤價：" + stockList.get(i).ClosingPrice;
                        }

                        // 更新 UI
                        runOnUiThread(() -> {
                            new AlertDialog.Builder(API.this)
                                    .setTitle("臺灣證券交易所股票資料")
                                    .setItems(items, null)
                                    .show();
                        });
                    } else {
                        Log.e("伺服器錯誤", response.code() + " " + response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e("查詢失敗", e.getMessage());
                }
            });
        });
    }

    // 資料模型
    static class StockData {
        String Code;              // 股票代號
        String Name;              // 股票名稱
        String ClosingPrice;      // 收盤價
    }
}
