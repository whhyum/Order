package com.example.exp_2020.dao;


import com.example.exp_2020.model.Master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class MasterDao {
    //登录验证
    public Master login(Connection con, Master master) throws Exception{
        Master resultUser=null;
        String sql="select * from master where userName=? and password=?";
        PreparedStatement pst=con.prepareStatement(sql);
        pst.setString(1,master.getUserName());
        pst.setString(2,master.getPassword());
        ResultSet rs=pst.executeQuery();//结果集
        if(rs.next()) {
            resultUser=new Master();
            resultUser.setId(rs.getInt("id"));
            resultUser.setUserName(rs.getString("userName"));
            resultUser.setUserName(rs.getString("password"));

        }
        return resultUser;
    }
    public int masterAdd(Connection con,Master m) throws Exception{
        String sql="insert into master values(null,?,?,?)";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, m.getUserName());
        pstmt.setString(2, m.getPassword());
        pstmt.setString(3, m.getEmail());
        return pstmt.executeUpdate();
    }
    public boolean isMasterExist(Connection con,Master m) throws Exception{
        String sql="select * from master where userName=?";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, m.getUserName());
        ResultSet rs =pstmt.executeQuery();
        return rs.next();
    }

    public int MasterModify(Connection con,Master m) throws Exception{
        String sql="update master set userName=?,password=?,email=? where id=?";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, m.getUserName());
        pstmt.setString(2, m.getPassword());
        pstmt.setString(3, m.getEmail());
        pstmt.setInt(4, m.getId());;
        return pstmt.executeUpdate();
    }
}

