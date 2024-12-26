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
import java.util.ArrayList;
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

        // 查詢台股按鈕
        findViewById(R.id.btn_search).setOnClickListener(v -> {
            fetchTWSEData(); // 查詢台股資料
        });

        // 查詢美股按鈕
        findViewById(R.id.btn_us).setOnClickListener(v -> {
            fetchUSData(); // 查詢美股資料
        });
    }

    /**
     * 查詢台股資料
     */
    private void fetchTWSEData() {
        // 台股 API URL
        String TWSE_URL = "https://openapi.twse.com.tw/v1/exchangeReport/STOCK_DAY_ALL";

        Request request = new Request.Builder().url(TWSE_URL).build();
        OkHttpClient okHttpClient = new OkHttpClient();

        // 發送 API 請求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200 && response.body() != null) {
                    // 解析回應資料
                    String responseBody = response.body().string();
                    Type listType = new TypeToken<List<TWSEStockData>>() {}.getType();
                    List<TWSEStockData> stockList = new Gson().fromJson(responseBody, listType);

                    // 顯示前 20 筆資料
                    List<String> stockInfo = new ArrayList<>();
                    for (int i = 0; i < Math.min(stockList.size(), 20); i++) {
                        stockInfo.add("代號：" + stockList.get(i).Code + "\n" +
                                "名稱：" + stockList.get(i).Name + "\n" +
                                "收盤價：" + stockList.get(i).ClosingPrice);
                    }

                    // 更新 UI 顯示資料
                    runOnUiThread(() -> {
                        new AlertDialog.Builder(API.this)
                                .setTitle("台股資料")
                                .setItems(stockInfo.toArray(new String[0]), null)
                                .show();
                    });
                } else {
                    Log.e("台股錯誤", response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("台股查詢失敗", e.getMessage());
            }
        });
    }

    /**
     * 查詢美股資料
     */
    private void fetchUSData() {
        // 查詢 20 支股票
        String US_URL = "https://financialmodelingprep.com/api/v3/quote/AAPL,MSFT,GOOGL,AMZN,FB,TSLA,NVDA,INTC,AMD,ADBE,ORCL,CSCO,PYPL,SQ,CRM,BABA,TWTR,UBER,LYFT,ZM?apikey=Z0z9dSLYR8NvNPnzg6I1tHlIvXz4CCzq" ;

        Request request = new Request.Builder().url(US_URL).build();
        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200 && response.body() != null) {
                    // 解析資料
                    String responseBody = response.body().string();
                    Log.d("API_RESPONSE", responseBody); // 檢查資料是否完整
                    Type listType = new TypeToken<List<USStockData>>() {}.getType();
                    List<USStockData> stockList = new Gson().fromJson(responseBody, listType);

                    // 顯示前 20 筆資料
                    List<String> stockInfo = new ArrayList<>();
                    for (int i = 0; i < Math.min(stockList.size(), 20); i++) {
                        stockInfo.add("代號：" + stockList.get(i).symbol + "\n" +
                                "名稱：" + stockList.get(i).name + "\n" +
                                "收盤價：" + stockList.get(i).price);
                    }

                    // 更新 UI
                    runOnUiThread(() -> {
                        new AlertDialog.Builder(API.this)
                                .setTitle("美股資料")
                                .setItems(stockInfo.toArray(new String[0]), null)
                                .show();
                    });
                } else {
                    Log.e("美股錯誤", response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("美股查詢失敗", e.getMessage());
            }
        });
    }


    // 台股資料模型
    static class TWSEStockData {
        String Code;              // 股票代號
        String Name;              // 股票名稱
        String ClosingPrice;      // 收盤價
    }

    // 美股資料模型
    static class USStockData {
        String symbol;  // 股票代號
        String name;    // 股票名稱
        double price;   // 收盤價
    }
}
