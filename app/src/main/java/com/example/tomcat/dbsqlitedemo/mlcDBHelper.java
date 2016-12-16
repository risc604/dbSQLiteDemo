package com.example.tomcat.dbsqlitedemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tomcat on 2016/12/5.
 */

public class mlcDBHelper extends SQLiteOpenHelper
{
    // 資料庫名稱
    public static final String DATABASE_NAME = "/sdcard/mt24hr.db";
    // 資料庫版本號，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;
    // 資料庫物件，固定的欄位變數
    private static SQLiteDatabase database;

    // 建構子，在一般的應用都不需要更改
    public mlcDBHelper(Context context, String name, CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    public static SQLiteDatabase getDatabase(Context context)
    {
        if ((database == null) || !database.isOpen())
        {
            database = new mlcDBHelper(context, DATABASE_NAME, null, VERSION).getWritableDatabase();
        }

        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // 建立應用程式需要的表格
        db.execSQL(ItemDAO.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // 刪除原有的表格
        db.execSQL("DROP TABLE IF EXISTS " + ItemDAO.TABLE_NAME);

        // 呼叫onCreate建立新版的表格
        onCreate(db);
    }

}
