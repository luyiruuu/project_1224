package com.example.project_1224;

<<<<<<< HEAD
import android.content.DialogInterface;
=======
import android.content.Intent;
>>>>>>> 003f14765f4c2260b159c95fbae3ca2d76f59c17
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private RadioGroup radioGroup;
    private RadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5, radioButton6;
    private Button btn_insert, btn_add_location;
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
            new LatLng(25.03369, 121.52998),  // 永康商圈
            new LatLng(25.0453, 121.5158),  //劉山東牛肉麵
            new LatLng(25.0523, 121.5311), // Beast Barbecue Co. 美式燒肉
            new LatLng(25.1162, 121.5331),// 鼎泰豐
            new LatLng(25.0486, 121.5555),//小紅莓自助火鍋城
            new LatLng(25.0588, 121.5567)//小北平麵食館

    };
    private final String[] predefinedLocationNames = {"小北平麵食館","台北101", "小紅莓自助火鍋城","台北車站", "臺北信義區", "鼎泰豐","全家便利商店", "7-11", "Beast Barbecue Co. 美式燒肉","中山商圈", "永康商圈","劉山東牛肉麵"};
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

<<<<<<< HEAD
        // 列表點擊刪除
        listView.setOnItemClickListener((parent, view, position, id) -> handleDeleteAction(position));
=======
        // 刪除功能
        btn_delete.setOnClickListener(view -> handleDeleteAction());
>>>>>>> 003f14765f4c2260b159c95fbae3ca2d76f59c17
    }

    private void initializeUI() {
        ed_price = findViewById(R.id.ed_price);

        radioGroup = findViewById(R.id.radioGroup); // 初始化 RadioGroup

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
            radioGroup.clearCheck(); // 清除選取的 RadioButton
        } catch (Exception e) {
            Toast.makeText(bill.this, "新增資料失敗: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

<<<<<<< HEAD
    private void handleDeleteAction(int position) {
        String item = items.get(position);
        String[] parts = item.split(" - |元 \\(|/|\\)");
        String type = parts[0];
        String price = parts[1];
        String year = parts[2];
        String month = parts[3];
        String day = parts[4];

        new AlertDialog.Builder(this)
                .setTitle("確認刪除")
                .setMessage("確定要刪除選取的項目嗎？")
                .setPositiveButton("是", (dialog, which) -> {
                    try {
                        int rowsAffected = dbrw.delete("myTable",
                                "book = ? AND price = ? AND year = ? AND month = ? AND day = ?",
                                new String[]{type, price, year, month, day});

                        if (rowsAffected > 0) {
                            items.remove(position);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(bill.this, "已成功刪除", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(bill.this, "刪除失敗，找不到資料", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(bill.this, "刪除失敗: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("否", null)
                .show();
=======
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
>>>>>>> 003f14765f4c2260b159c95fbae3ca2d76f59c17
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
        builder.setItems(predefinedLocationNames, (dialog, which) -> {
            LatLng selectedLatLng = predefinedLocations[which];
            selectedLocations.add(selectedLatLng);
            Toast.makeText(bill.this, "選擇地點：" + predefinedLocationNames[which], Toast.LENGTH_SHORT).show();
        });
        builder.create().show();
    }
}