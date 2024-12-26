//MyDBHelper.java
package com.example.project_1224;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "expense.db"; // 資料庫名稱
    private static final int DB_VERSION = 1; // 資料庫版本

    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 創建資料表 myTable
        db.execSQL("CREATE TABLE IF NOT EXISTS myTable (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "book TEXT, " +
                "price INTEGER, " +
                "year INTEGER, " +
                "month INTEGER, " +
                "day INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果資料表已存在，則刪除並重新創建
        db.execSQL("DROP TABLE IF EXISTS myTable");
        onCreate(db);
    }
}
