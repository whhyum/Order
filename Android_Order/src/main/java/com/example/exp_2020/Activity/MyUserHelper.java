package com.example.exp_2020.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyUserHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "my.db";
    private static final int DATABASE_VERSION = 10;

    public MyUserHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        getWritableDatabase();//此步不能少
    }

    public void onCreate(SQLiteDatabase db) {
        String sql = "create table Cart(_id INTEGER,foodname varchar(50)," +
                "taste varchar(20),price float,num int,path varchar(20));";
        db.execSQL(sql);
//        ...然后添加数据，可采用把数据 SQL 放在文件，然后读入文件来装载数据

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        onCreate(db);
    }
//    public void add(SQLiteDatabase db){
//        ContentValues values = new ContentValues();
//        String sql="INSERT INTO Student VALUES (null, 111, '张三', '男', '信计', '2000‐11‐11 00:00:00.000');";
//        db.execSQL(sql);
//
//    }
}