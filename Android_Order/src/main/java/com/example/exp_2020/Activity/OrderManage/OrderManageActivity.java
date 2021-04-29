package com.example.exp_2020.Activity.OrderManage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.exp_2020.Activity.MyOrder.OrderAdapter;
import com.example.exp_2020.R;
import com.example.exp_2020.dao.OrderDao;
import com.example.exp_2020.model.Order;
import com.example.exp_2020.util.DbUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class OrderManageActivity extends AppCompatActivity {
    private DbUtil dbutil=new DbUtil();
    private OrderDao orderdao=new OrderDao();
    private Button btn;
    private EditText et;
    private List<Order> list= new ArrayList<Order>();
    private RecyclerView lv;
    private OrderAdapter oAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manage);

        btn=findViewById(R.id.search);
        et=findViewById(R.id.orderID_text);
        lv=findViewById(R.id.myorder_listView);

        lv.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(OrderManageActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv.setLayoutManager(mLayoutManager);

        list=getData();
        oAdapter = new OrderAdapter(list,listener);
        lv.setAdapter(oAdapter);
        lv.addItemDecoration(new DividerItemDecoration(OrderManageActivity.this,DividerItemDecoration.VERTICAL));

        oAdapter.notifyDataSetChanged();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderId = et.getText().toString();
                Order order = new Order();
                order.setOrderId(orderId);
                list.clear();
                list=getData(order);
                oAdapter = new OrderAdapter(list,listener);
                lv.setAdapter(oAdapter);
                oAdapter.notifyDataSetChanged();

            }
        });
    }

    private List<Order> getData(Order or) {
        new Thread(){
            public void run() {
                Connection con=null;
                Order order = new Order();
                order.setOrderId(or.getOrderId());
                try {
                    con=dbutil.getcon();
                    Log.d("连接","成功");
                    ResultSet rs=orderdao.orderList(con,order);
                    Log.d("获取","成功");
                    while(rs.next()) {
                        Order o=new Order();
                        o.setOrderId(rs.getString("orderId"));
                        o.setUserName(rs.getString("userName"));
                        o.setAddress(rs.getString("address"));
                        o.setPhone(rs.getString("phone"));
                        o.setFoodTotalPrice(rs.getFloat("orderTotalMoney"));
                        o.setOrderStatus(rs.getInt("orderStatus"));
                        Log.i("订单界面",""+rs.getString("orderId"));
                        list.add(o);

                    }

                }catch(Exception e) {
                    e.printStackTrace();
                }finally {
                    try {
                        dbutil.closeCon(con);
                    } catch (Exception e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        oAdapter.notifyDataSetChanged();
        return list;
    }

//    /*刷新界面*/
//
    @Override
    protected void onRestart() {
        super.onRestart();
        list.clear();
        list=getData();
        oAdapter = new OrderAdapter(list,listener);
        lv.setAdapter(oAdapter);
        oAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        list.clear();
        list=getData();
        oAdapter = new OrderAdapter(list,listener);
        lv.setAdapter(oAdapter);
        oAdapter.notifyDataSetChanged();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        list.clear();
//        list=getData();
//        oAdapter = new OrderAdapter(list,listener);
//        lv.setAdapter(oAdapter);
//        oAdapter.notifyDataSetChanged();
//    }

    /*获取资源*/
    private List<Order> getData() {
        new Thread(){
            public void run() {
                Connection con=null;
                Order order = new Order();
                try {
                    con=dbutil.getcon();
                    Log.d("连接","成功");
                    ResultSet rs=orderdao.orderList(con,order);
                    Log.d("获取","成功");
                    while(rs.next()) {
                        Order o=new Order();
                        o.setOrderId(rs.getString("orderId"));
                        o.setUserName(rs.getString("userName"));
                        o.setAddress(rs.getString("address"));
                        o.setPhone(rs.getString("phone"));
                        o.setFoodTotalPrice(rs.getFloat("orderTotalMoney"));
                        o.setOrderStatus(rs.getInt("orderStatus"));
                        Log.i("订单界面",""+rs.getString("orderId"));
                        list.add(o);

                    }
                    oAdapter.notifyDataSetChanged();
                }catch(Exception e) {
                    e.printStackTrace();
                }finally {
                    try {
                        dbutil.closeCon(con);
                    } catch (Exception e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        return list;
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Order o=(Order)v.getTag();
            Intent intent=new Intent(OrderManageActivity.this,OrderUpActivity.class);
            Bundle b=new Bundle();
            b.putString("orderId",o.getOrderId());
            intent.putExtras(b);
            startActivity(intent);
        }
    };
}
