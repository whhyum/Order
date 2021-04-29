package com.example.exp_2020.dao;

import android.database.Cursor;

import com.example.exp_2020.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerDao {
    public Customer login(Connection con,Customer cuser) throws Exception{
        Customer resultUser=null;
        String sql="select * from customer where userName=? and password=?";
        PreparedStatement pst=con.prepareStatement(sql);
        pst.setString(1,cuser.getUserName());
        pst.setString(2,cuser.getPassword());
        ResultSet rs=pst.executeQuery();//结果集
        if(rs.next()) {
            resultUser=new Customer();
            resultUser.setId(rs.getInt("id"));
            resultUser.setUserName(rs.getString("userName"));
            resultUser.setUserName(rs.getString("password"));

        }
        return resultUser;
    }
    public boolean isCustomerExist(Connection con,Customer c) throws Exception{
        String sql="select * from customer where userName=?";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, c.getUserName());
        ResultSet rs =pstmt.executeQuery();
        return rs.next();
    }
    public int CustomerAdd(Connection con,Customer c) throws Exception{
        String sql="insert into customer values(null,?,?,?)";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, c.getUserName());
        pstmt.setString(2, c.getPassword());
        pstmt.setString(3, null);
        return pstmt.executeUpdate();
    }
}
