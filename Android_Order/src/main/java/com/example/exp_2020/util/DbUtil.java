package com.example.exp_2020.util;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtil {
    private String driver = "net.sourceforge.jtds.jdbc.Driver";
    private String url ="jdbc:jtds:sqlserver://192.168.1.104:1434/db_ordering";
    private String user = "sa";
    private String password = "abc123";
    public Connection getcon() throws Exception{  //获取数据库连接
        Class.forName(driver);
        Log.d("加载","成功");
        Connection con = DriverManager.getConnection(url,user,password);
        return con;
    }
    public void closeCon(Connection con) throws Exception{  //获取数据库连接
        if(con!=null) {
            con.close();
        }
    }
    public static void main(String[] args) {
        DbUtil dbutil=new DbUtil();
        new Thread(new Runnable(){
            public void run(){
        try {
            dbutil.getcon();
            System.out.println("连接成功！");
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
            }
        }).start();


    }

}

