package com.example.exp_2020.dao;

import com.example.exp_2020.model.Order;
import com.example.exp_2020.util.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrderDao {
    public int order_foodAdd(Connection con , Order order) throws Exception{
        String sql="insert into order_food values(?,?,?,?,?,?)";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, order.getOrderId());
        pstmt.setFloat(2, order.getFoodTotalPrice());
        pstmt.setInt(3, order.getFoodId());
        pstmt.setFloat(4, order.getFoodPrice());
        pstmt.setInt(5, order.getFoodNum());
        pstmt.setString(6, order.getFoodName());
        return pstmt.executeUpdate();
    }

    public int order_infoAdd(Connection con ,Order order) throws Exception{
        String sql="insert into order_info values(?,?,?,?,?,?,?)";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setString(1, order.getId());
        pstmt.setString(2, order.getOrderId());
        pstmt.setInt(3, order.getOrderStatus());
        pstmt.setFloat(4, order.getOrderTotalMoney());
        pstmt.setString(5, order.getUserName());
        pstmt.setString(6, order.getAddress());
        pstmt.setString(7, order.getPhone());
        return pstmt.executeUpdate();
    }
    public ResultSet orderList(Connection con, Order order) throws Exception{
        StringBuffer sb=new StringBuffer("select * from order_info");
        if(StringUtil.isNotEmpty(order.getOrderId())){
            sb.append(" and orderId like '%"+order.getOrderId()+"%' Order By Id DESC");
        }
        if(StringUtil.isNotEmpty(order.getUserName())){
            sb.append(" and userName like '%"+order.getUserName()+"%' Order By Id DESC");
        }
        PreparedStatement pstmt=con.prepareStatement(sb.toString().replaceFirst("and", "where"));
        return pstmt.executeQuery();
    }
    public ResultSet orderLista(Connection con, Order order) throws Exception{
        StringBuffer sb=new StringBuffer("select * from order_info");
        if(StringUtil.isNotEmpty(order.getOrderId())){
            sb.append(" and orderId ="+order.getOrderId()+" Order By Id DESC");
        }
        if(StringUtil.isNotEmpty(order.getUserName())){
            sb.append(" and userName like '%"+order.getUserName()+"%' Order By Id DESC");
        }
        PreparedStatement pstmt=con.prepareStatement(sb.toString().replaceFirst("and", "where"));
        return pstmt.executeQuery();
    }
    public ResultSet orderShell(Connection con,Order order) throws Exception{
        String sb=new String("select Top 7 Id,ROUND(sum(orderTotalMoney),2) as shell "
                + "from order_info where orderStatus!=5 Group By Id Order By Id DESC"); //成功交易的订单
        PreparedStatement pstmt=con.prepareStatement(sb);
        return pstmt.executeQuery();
    }
    public ResultSet orderShellFood(Connection con,Order order) throws Exception{
        StringBuffer sb=new StringBuffer("select foodname,sum(FoodNum) as num "
                + "from order_food"); //成功交易的订单
        if(StringUtil.isNotEmpty(order.getId())){
            sb.append(" and orderId like '%"+order.getId()+"%' Group By foodname");
        }
        PreparedStatement pstmt=con.prepareStatement(sb.toString().replaceFirst("and", "where"));
        return pstmt.executeQuery();
    }

    public ResultSet orderFoodList(Connection con,Order order) throws Exception{
        StringBuffer sb=new StringBuffer("select * from order_food");
        if(StringUtil.isNotEmpty(order.getOrderId())){
            sb.append(" and orderId like '%"+order.getOrderId()+"%' Order By orderId desc");
        }
        PreparedStatement pstmt=con.prepareStatement(sb.toString().replaceFirst("and", "where"));
        return pstmt.executeQuery();
    }

    public int orderStatusModify(Connection con,Order order) throws Exception{
        String sql="update order_info set orderStatus=? where orderId=?";
        PreparedStatement pstmt=con.prepareStatement(sql);
        pstmt.setInt(1, order.getOrderStatus());
        pstmt.setString(2, order.getOrderId());
        return pstmt.executeUpdate();
    }
    //同时删除order_goods和irder_info两张表里的有关orderId的数据 删除顺序要保证主外键关联顺序，不可以互换顺序
    public int deleteOrder(Connection con,Order order) throws Exception{
        String sql="delete from order_food where orderId=?";
        PreparedStatement pstmt1=con.prepareStatement(sql);
        pstmt1.setString(1, order.getOrderId());
        pstmt1.executeUpdate();
        sql="delete from order_info where orderId=?";
        PreparedStatement pstmt2=con.prepareStatement(sql);
        pstmt2.setString(1, order.getOrderId());
        return pstmt2.executeUpdate();
    }

}
