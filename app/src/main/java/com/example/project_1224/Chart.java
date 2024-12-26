package com.example.project_1224;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;

public class Chart extends AppCompatActivity {

    private PieChart pieChart; // 用於顯示圓餅圖的組件
    private SQLiteDatabase dbrw; // 資料庫操作物件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);

        pieChart = findViewById(R.id.pieChart); // 綁定 XML 中的 PieChart 組件
        dbrw = new MyDBHelper(this).getReadableDatabase(); // 初始化資料庫

        initializePieChart(); // 初始化 PieChart 設定

        if (isTableEmpty()) { // 檢查資料表是否為空
            initializeEmptyChart(); // 初始化空的圓餅圖
        } else {
            loadChartData(); // 加載數據並顯示圖表
        }
    }

    private void initializePieChart() {
        pieChart.setUsePercentValues(true); // 顯示百分比
        pieChart.getDescription().setEnabled(false); // 禁用描述
        pieChart.setHoleRadius(40f); // 設定圓心洞的半徑
        pieChart.setTransparentCircleRadius(45f); // 設置透明圈半徑
        pieChart.setRotationEnabled(true); // 啟用旋轉
        pieChart.setEntryLabelTextSize(12f); // 設置條目標籤字體大小
    }

    private void initializeEmptyChart() {
        pieChart.setNoDataText("暫無數據"); // 當圖表無數據時顯示的文字
        pieChart.setNoDataTextColor(Color.BLACK); // 設置文字顏色
        pieChart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD); // 設置文字樣式為粗體
        pieChart.invalidate(); // 刷新圖表
    }

    private boolean isTableEmpty() {
        Cursor cursor = dbrw.rawQuery("SELECT COUNT(*) FROM myTable", null); // 查詢資料表記錄數
        cursor.moveToFirst();
        int rowCount = cursor.getInt(0); // 獲取記錄數量
        cursor.close();
        return rowCount == 0; // 判斷資料表是否為空
    }

    private void loadChartData() {
        HashMap<String, Integer> categoryData = new HashMap<>(); // 用於儲存分類數據
        Cursor cursor = dbrw.rawQuery("SELECT book, SUM(price) as total FROM myTable GROUP BY book", null); // 聚合查詢

        while (cursor.moveToNext()) {
            String category = cursor.getString(cursor.getColumnIndexOrThrow("book")); // 獲取分類名稱
            int total = cursor.getInt(cursor.getColumnIndexOrThrow("total")); // 獲取分類總額
            categoryData.put(category, total); // 將分類數據存入 HashMap
        }
        cursor.close();

        ArrayList<PieEntry> entries = new ArrayList<>(); // 圓餅圖數據條目列表
        for (String key : categoryData.keySet()) {
            entries.add(new PieEntry(categoryData.get(key), key)); // 將分類數據轉為 PieEntry
        }

        if (entries.isEmpty()) { // 如果數據條目為空
            Toast.makeText(this, "沒有資料可顯示", Toast.LENGTH_SHORT).show(); // 顯示提示信息
            return;
        }

        PieDataSet dataSet = new PieDataSet(entries, "支出分類"); // 創建數據集
        dataSet.setValueTextSize(14f); // 設置數據文字大小
        dataSet.setColors(new int[]{ // 設置數據顏色
                R.color.pie_color1, R.color.pie_color2, R.color.pie_color3,
                R.color.pie_color4, R.color.pie_color5, R.color.pie_color6
        }, this);

        PieData data = new PieData(dataSet); // 創建 PieData
        pieChart.setData(data); // 將數據設置到圖表中
        pieChart.invalidate(); // 刷新圖表
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbrw != null && dbrw.isOpen()) {
            dbrw.close(); // 關閉資料庫
        }
    }
}
