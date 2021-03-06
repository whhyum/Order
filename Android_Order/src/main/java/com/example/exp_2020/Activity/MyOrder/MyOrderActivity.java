package com.example.exp_2020.Activity.MyOrder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.exp_2020.Activity.LoginActivity;
import com.example.exp_2020.R;
import com.example.exp_2020.dao.OrderDao;
import com.example.exp_2020.model.Order;
import com.example.exp_2020.util.DbUtil;
import com.example.exp_2020.util.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends AppCompatActivity {
    private DbUtil dbutil=new DbUtil();
    private OrderDao orderdao=new OrderDao();
    private Button btn;
    private EditText et;
    private List<Order> list= new ArrayList<Order>();
    private RecyclerView lv;
    private RecyclerView.Adapter<OrderAdapter.ViewHolder> oAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        btn=findViewById(R.id.search);
        lv=findViewById(R.id.myorder_listView);

        lv.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MyOrderActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv.setLayoutManager(mLayoutManager);

        list=getData(new Order());
        oAdapter = new OrderAdapter(list,listener);
        lv.setAdapter(oAdapter);
        lv.addItemDecoration(new DividerItemDecoration(MyOrderActivity.this,DividerItemDecoration.VERTICAL));
        oAdapter.notifyDataSetChanged();

        btn=findViewById(R.id.search);
        et=findViewById(R.id.orderIDtext);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderId = et.getText().toString();
                Order o = new Order();
                o.setOrderId(orderId);
                list=getData(o);
                oAdapter.notifyDataSetChanged();
            }
        });

    }
/*????????????*/
    private List<Order> getData(Order order) {
        list.clear();
        new Thread(){
            public void run() {
                Connection con=null;
                String username= LoginActivity.currentUser.getUserName();
                if(StringUtil.isEmpty(order.getOrderId())){
                    order.setUserName(username);
                }
                try {
                    con=dbutil.getcon();
                    Log.d("??????","??????");
                    ResultSet rs=orderdao.orderLista(con,order);
                    Log.d("??????","??????");
                    while(rs.next()) {
                        Order o=new Order();
                        o.setOrderId(rs.getString("orderId"));
                        o.setUserName(rs.getString("userName"));
                        o.setAddress(rs.getString("address"));
                        o.setPhone(rs.getString("phone"));
                        o.setFoodTotalPrice(rs.getFloat("orderTotalMoney"));
                        o.setOrderStatus(rs.getInt("orderStatus"));
                        Log.i("????????????",""+rs.getInt("orderStatus"));
                        list.add(o);
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }finally {
                    try {
                        dbutil.closeCon(con);
                    } catch (Exception e) {
                        // TODO ??????????????? catch ???
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
            AlertDialog.Builder bb=new AlertDialog.Builder(MyOrderActivity.this);
            bb.setTitle("????????????");
            String phone=o.getPhone();
            if(StringUtil.isEmpty(o.getPhone())){
                phone="???????????????";
            }
            bb.setMessage("????????????:"+o.getUserName()+"\n"+"?????????"+o.getAddress()+"\n"+"???????????????"+phone);
            bb.setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    switch (o.getOrderStatus()){
                        case 0:
                            o.setOrderStatus(5);
                            new Thread() {
                                public void run() {
                                    Looper.prepare();
                                    try {
                                        Connection con = dbutil.getcon();
                                        int modifyNum = orderdao.orderStatusModify(con, o);
                                        if (modifyNum == 1) {

                                            Toast.makeText(MyOrderActivity.this, "?????????", Toast.LENGTH_LONG).show();
                                        } else {

                                            Toast.makeText(MyOrderActivity.this, "????????????", Toast.LENGTH_LONG).show();
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Looper.loop();
                                }

                            }.start();
                            break;
                        default:
                            Toast.makeText(MyOrderActivity.this, "??????????????????", Toast.LENGTH_LONG).show();
                            break;
                    }
                    oAdapter.notifyDataSetChanged();
                }
            });
            bb.setNegativeButton("??????",null);
            bb.show();
        }
    };
}
