package com.example.exp_2020.dao;

import com.example.exp_2020.model.food;
import com.example.exp_2020.util.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FoodDao {
    public int add(Connection con,food food,String path) throws Exception{  //菜品添加
        String sql="insert into menu(dishes,price,taste,remark,path) values(?,?,?,?,?)";
        PreparedStatement pst=con.prepareStatement(sql);
        pst.setString(1,food.getDishes());
        pst.setDouble(2,food.getPrice());
        pst.setString(3,food.getTaste());
        pst.setString(4,food.getRemark());
        pst.setString(5,food.getPath());
        return pst.executeUpdate();
    }
    public ResultSet list(Connection con, food food) throws Exception{  //获取菜品集合
        StringBuffer sb=new StringBuffer("select * from menu");
        if(StringUtil.isNotEmpty(food.getDishes())) {
            sb.append(" and dishes like '%"+food.getDishes()+"%'");
        }
        PreparedStatement pst=con.prepareStatement(sb.toString().replaceFirst("and", "where"));
        return pst.executeQuery();
    }
    public int foodDelete(Connection con, int id) throws Exception{
        String sql="delete from menu where id=?";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setInt(1, id);
        return pstmt.executeUpdate();
    }

    public int foodModify(Connection con, food food) throws Exception{
        String sql="update menu set dishes=?,taste=?,price=?,path=? where id=?";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, food.getDishes());
        pstmt.setString(2, food.getTaste());
        pstmt.setFloat(3, food.getPrice());
        pstmt.setString(4, food.getPath());
        pstmt.setInt(5, food.getId());
        return pstmt.executeUpdate();
    }

}
