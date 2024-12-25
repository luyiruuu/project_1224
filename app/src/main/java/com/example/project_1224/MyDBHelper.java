//MyDBHelper.java
package com.example.project_1224;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myDB.db"; // 資料庫名稱
    private static final int DATABASE_VERSION = 2; // 資料庫版本（升級時需更新版本號）

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建立 myTable 資料表，包含 book、price、year、month、day 欄位
        db.execSQL("CREATE TABLE myTable (" +
                "book TEXT, " +
                "price INTEGER, " +
                "year INTEGER, " +
                "month INTEGER, " +
                "day INTEGER)");

        // 建立 deleted_categories 資料表，包含 book 欄位
        db.execSQL("CREATE TABLE deleted_categories (" +
                "book TEXT PRIMARY KEY)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 若版本升級，刪除舊表並重新建立
        db.execSQL("DROP TABLE IF EXISTS myTable");
        db.execSQL("DROP TABLE IF EXISTS deleted_categories");
        onCreate(db);
    }
}
