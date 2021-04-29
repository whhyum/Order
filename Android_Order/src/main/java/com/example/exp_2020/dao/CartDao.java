package com.example.exp_2020.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.exp_2020.Activity.MyUserHelper;

public class CartDao {
    private final MyUserHelper m;

    public CartDao(Context context) {
        m=new MyUserHelper(context);
    }
    public void insert(){
//        SQLiteDatabase db=m.getWritableDatabase();
//        String sql="insert into Cart(_id,foodname,taste,price,num,img) values(?,?,?,?,?,?)";
//        db.execSQL(sql,new[]{1,});
    }
}
