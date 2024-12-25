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

        // 初始化 PieChart
        pieChart = findViewById(R.id.pieChart);
        dbrw = new MyDBHelper(this).getReadableDatabase();

        loadChartData(); // 加載圓餅圖數據
    }

    private void loadChartData() {
        // 檢查資料庫表格是否存在
        Cursor checkCursor = dbrw.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='myTable'", null);
        if (checkCursor.getCount() == 0) {
            Toast.makeText(this, "資料庫表格不存在！", Toast.LENGTH_SHORT).show();
            checkCursor.close();
            return;
        }
        checkCursor.close();

        // 獲取數據
        HashMap<String, Integer> categoryData = new HashMap<>();
        Cursor cursor = dbrw.rawQuery("SELECT book, SUM(price) as total FROM myTable GROUP BY book", null);

        while (cursor.moveToNext()) {
            String category = cursor.getString(cursor.getColumnIndexOrThrow("book"));
            int total = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
            categoryData.put(category, total);
        }
        cursor.close();

        // 準備 PieChart 數據
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (String key : categoryData.keySet()) {
            entries.add(new PieEntry(categoryData.get(key), key));
        }

        if (entries.isEmpty()) {
            Toast.makeText(this, "沒有資料可顯示", Toast.LENGTH_SHORT).show();
            return;
        }

        PieDataSet dataSet = new PieDataSet(entries, "支出分類");
        dataSet.setColors(new int[]{
                R.color.pie_color1, R.color.pie_color2, R.color.pie_color3,
                R.color.pie_color4, R.color.pie_color5, R.color.pie_color6}, this);

        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.setUsePercentValues(true); // 顯示百分比
        pieChart.invalidate(); // 刷新圖表
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbrw.close(); // 關閉資料庫
    }
}
