package com.example.project_1224;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    private PieChart pieChart;
    private SQLiteDatabase dbrw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);

        pieChart = findViewById(R.id.pieChart);
        dbrw = new MyDBHelper(this).getReadableDatabase();

        initializePieChart(); // 初始化 PieChart 設定
        if (isTableEmpty()) {
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
        PieDataSet dataSet = new PieDataSet(new ArrayList<>(), "暫無數據");
        dataSet.setValueTextSize(14f);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // 刷新圖表
    }

    private boolean isTableEmpty() {
        Cursor cursor = dbrw.rawQuery("SELECT COUNT(*) FROM myTable", null);
        cursor.moveToFirst();
        int rowCount = cursor.getInt(0);
        cursor.close();
        return rowCount == 0;
    }

    private void loadChartData() {
        HashMap<String, Integer> categoryData = new HashMap<>();
        Cursor cursor = dbrw.rawQuery("SELECT book, SUM(price) as total FROM myTable GROUP BY book", null);

        while (cursor.moveToNext()) {
            String category = cursor.getString(cursor.getColumnIndexOrThrow("book"));
            int total = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
            categoryData.put(category, total);
        }
        cursor.close();

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (String key : categoryData.keySet()) {
            entries.add(new PieEntry(categoryData.get(key), key));
        }

        if (entries.isEmpty()) {
            Toast toast = Toast.makeText(this, "沒有資料可顯示", Toast.LENGTH_SHORT);
            toast.getView().setPadding(10, 10, 10, 10);
            toast.show();
            return;
        }

        PieDataSet dataSet = new PieDataSet(entries, "支出分類");
        dataSet.setValueTextSize(14f); // 設置數據字體大小
        dataSet.setColors(new int[]{
                R.color.pie_color1, R.color.pie_color2, R.color.pie_color3,
                R.color.pie_color4, R.color.pie_color5, R.color.pie_color6
        }, this);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // 刷新圖表
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbrw != null && dbrw.isOpen()) {
            dbrw.close();
        }
    }
}

