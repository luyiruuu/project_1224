package com.example.project_1224;

import android.content.Intent;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class bill extends AppCompatActivity {

    private EditText ed_price;
    private RadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5, radioButton6;
    private Button btn_insert, btn_delete, btn_add_location;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();

    private Spinner yearSpinner, monthSpinner, daySpinner;

    private final LatLng[] predefinedLocations = {
            new LatLng(25.033611, 121.565000), // 台北101
            new LatLng(25.047924, 121.517081), // 台北車站
            new LatLng(25.032728, 121.564137),  // 臺北信義區
            new LatLng(25.02348, 121.52864),  // 全家便利商店
            new LatLng(25.04360, 121.53562),  // 7-11
            new LatLng(25.05591, 121.51970), // 中山商圈
            new LatLng(25.03369, 121.52998)  // 永康商圈
    };
    private final String[] predefinedLocationNames = {"台北101", "台北車站", "臺北信義區","全家便利商店","7-11","中山商圈","永康商圈"};
    public static final List<LatLng> selectedLocations = new ArrayList<>();

    private SQLiteDatabase dbrw;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbrw != null && dbrw.isOpen()) {
            dbrw.close(); // 關閉資料庫
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill);

        // 初始化資料庫
        dbrw = new MyDBHelper(this).getWritableDatabase();

        // 初始化 UI 元件
        initializeUI();

        // 初始化 Spinners
        initializeSpinners();

        // 加載已儲存的資料到清單
        loadSavedItems();

        // 新增地點按鈕功能
        btn_add_location.setOnClickListener(v -> promptUserToSelectLocation());

        // 插入功能
        btn_insert.setOnClickListener(view -> handleInsertAction());

        // 刪除功能
        btn_delete.setOnClickListener(view -> handleDeleteAction());
    }

    private void initializeUI() {
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
        btn_add_location = findViewById(R.id.btn_add_location);

        listView = findViewById(R.id.listView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
    }

    private void initializeSpinners() {
        ArrayAdapter<String> yearAdapter = createSpinnerAdapter(generateYearList());
        yearSpinner.setAdapter(yearAdapter);

        ArrayAdapter<String> monthAdapter = createSpinnerAdapter(generateNumberList(1, 12));
        monthSpinner.setAdapter(monthAdapter);

        ArrayAdapter<String> dayAdapter = createSpinnerAdapter(generateNumberList(1, 31));
        daySpinner.setAdapter(dayAdapter);
    }

    private ArrayAdapter<String> createSpinnerAdapter(List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private List<String> generateYearList() {
        List<String> years = new ArrayList<>();
        for (int i = 2023; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            years.add(String.valueOf(i));
        }
        return years;
    }

    private List<String> generateNumberList(int start, int end) {
        List<String> numbers = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            numbers.add(String.valueOf(i));
        }
        return numbers;
    }

    private void loadSavedItems() {
        Cursor cursor = dbrw.rawQuery("SELECT book, price, year, month, day FROM myTable", null);
        while (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndexOrThrow("book"));
            int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
            int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));

            String item = type + " - " + price + "元 (" + year + "/" + month + "/" + day + ")";
            items.add(item); // 將資料添加到清單
        }
        cursor.close();
        adapter.notifyDataSetChanged(); // 通知 ListView 更新
    }

    private void handleInsertAction() {
        String type = getSelectedType();
        String price = ed_price.getText().toString();

        if (type == null || price.isEmpty()) {
            Toast.makeText(bill.this, "欄位請勿留空", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());
            int month = Integer.parseInt(monthSpinner.getSelectedItem().toString());
            int day = Integer.parseInt(daySpinner.getSelectedItem().toString());

            // 新增資料到資料庫
            dbrw.execSQL("INSERT INTO myTable(book, price, year, month, day) VALUES(?, ?, ?, ?, ?)",
                    new Object[]{type, price, year, month, day});

            // 新增資料到列表
            String item = type + " - " + price + "元 (" + year + "/" + month + "/" + day + ")";
            items.add(item);
            adapter.notifyDataSetChanged();

            Toast.makeText(bill.this, "成功新增：類別 " + type + " 價格 " + price, Toast.LENGTH_SHORT).show();
            ed_price.setText(""); // 清空輸入框
        } catch (Exception e) {
            Toast.makeText(bill.this, "新增資料失敗: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleDeleteAction() {
        if (items.isEmpty()) {
            Toast.makeText(this, "目前沒有資料可刪除", Toast.LENGTH_SHORT).show();
            return;
        }

        // 刪除第一筆資料作為範例
        try {
            Cursor cursor = dbrw.rawQuery("SELECT id FROM myTable LIMIT 1", null);
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                dbrw.execSQL("DELETE FROM myTable WHERE id = ?", new Object[]{id});

                items.remove(0); // 同時從 ListView 的資料中移除
                adapter.notifyDataSetChanged();

                Toast.makeText(this, "成功刪除第一筆資料", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "刪除資料失敗: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getSelectedType() {
        if (radioButton1.isChecked()) return "早餐";
        if (radioButton2.isChecked()) return "午餐";
        if (radioButton3.isChecked()) return "晚餐";
        if (radioButton4.isChecked()) return "交通";
        if (radioButton5.isChecked()) return "娛樂";
        if (radioButton6.isChecked()) return "其他";
        return null;
    }

    private void promptUserToSelectLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("選擇地點");

        final Spinner locationSpinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                predefinedLocationNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        builder.setView(locationSpinner);

        builder.setPositiveButton("新增標記", (dialog, which) -> {
            int selectedPosition = locationSpinner.getSelectedItemPosition();
            LatLng selectedLocation = predefinedLocations[selectedPosition];
            selectedLocations.add(selectedLocation);
            Toast.makeText(this, "地點已新增: " + predefinedLocationNames[selectedPosition], Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}

