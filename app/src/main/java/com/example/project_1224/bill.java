package com.example.project_1224;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class bill extends AppCompatActivity {

    private EditText ed_price;
    private RadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5, radioButton6;
    private Button  btn_insert, btn_delete;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();
    private SQLiteDatabase dbrw;

    private Spinner yearSpinner, monthSpinner, daySpinner;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbrw.close(); // 關閉資料庫
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill);

        // 初始化 UI 元件
        ed_price = findViewById(R.id.ed_price);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        radioButton5 = findViewById(R.id.radioButton5);
        radioButton6 = findViewById(R.id.radioButton6);

        yearSpinner = findViewById(R.id.spinner_year);
        monthSpinner = findViewById(R.id.spinner_month);
        daySpinner = findViewById(R.id.spinner_day);


        btn_insert = findViewById(R.id.btn_insert);
        btn_delete = findViewById(R.id.btn_delete);
        listView = findViewById(R.id.listView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        dbrw = new MyDBHelper(this).getWritableDatabase();

        // 初始化 Spinner 的數據
        initializeSpinners();

        // 插入功能
        btn_insert.setOnClickListener(view -> {
            String type = getSelectedType();
            String price = ed_price.getText().toString();

            if (type == null || price.isEmpty()) {
                Toast.makeText(bill.this, "欄位請勿留空", Toast.LENGTH_SHORT).show();
                return;
            }

            // 獲取 Spinner 中的選擇
            int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());
            int month = Integer.parseInt(monthSpinner.getSelectedItem().toString());
            int day = Integer.parseInt(daySpinner.getSelectedItem().toString());

            try {
                dbrw.execSQL("INSERT INTO myTable(book, price, year, month, day) VALUES(?, ?, ?, ?, ?)",
                        new Object[]{type, price, year, month, day});
                Toast.makeText(bill.this, "成功新增：類別 " + type + " 價格 " + price, Toast.LENGTH_SHORT).show();
                ed_price.setText(""); // 清空價格輸入
                updateListView(); // 更新 ListView
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(bill.this, "新增失敗：" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        // 刪除功能
        btn_delete.setOnClickListener(view -> {
            String type = getSelectedType();
            String year = yearSpinner.getSelectedItem().toString();
            String month = monthSpinner.getSelectedItem().toString();
            String day = daySpinner.getSelectedItem().toString();

            if (type == null) {
                Toast.makeText(bill.this, "請選擇要刪除的類別", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int rows = dbrw.delete(
                        "myTable",
                        "book = ? AND year = ? AND month = ? AND day = ?",
                        new String[]{type, year, month, day}
                );

                if (rows > 0) {
                    Toast.makeText(bill.this, "刪除成功：類別 " + type, Toast.LENGTH_SHORT).show();
                    updateListView(); // 更新 ListView
                } else {
                    Toast.makeText(bill.this, "刪除失敗：未找到符合條件的記錄", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(bill.this, "刪除失敗：" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 初始化 Spinner 的數據
    private void initializeSpinners() {
        // 設置年份範圍
        ArrayList<String> years = new ArrayList<>();
        for (int i = 2023; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // 設置月份範圍
        ArrayList<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            months.add(String.valueOf(i));
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        // 設置日期範圍
        ArrayList<String> days = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            days.add(String.valueOf(i));
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);
    }

    // 更新 ListView 資料
    private void updateListView() {
        items.clear(); // 清空舊數據

        Cursor cursor = dbrw.rawQuery("SELECT * FROM myTable", null);
        while (cursor.moveToNext()) {
            String book = cursor.getString(cursor.getColumnIndexOrThrow("book"));
            String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
            int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));

            items.add(book + " - " + price + "元 (" + year + "/" + month + "/" + day + ")");
        }
        cursor.close();

        adapter.notifyDataSetChanged(); // 更新 Adapter
    }

    // 取得選中的類別
    private String getSelectedType() {
        if (radioButton1.isChecked()) return "早餐";
        if (radioButton2.isChecked()) return "午餐";
        if (radioButton3.isChecked()) return "晚餐";
        if (radioButton4.isChecked()) return "交通";
        if (radioButton5.isChecked()) return "娛樂";
        if (radioButton6.isChecked()) return "其他";
        return null;
    }
}
