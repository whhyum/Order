package com.example.exp_2020.Activity.OrderManage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exp_2020.Activity.MyOrder.OrderAdapter;
import com.example.exp_2020.R;
import com.example.exp_2020.dao.FoodDao;
import com.example.exp_2020.dao.OrderDao;
import com.example.exp_2020.model.Order;
import com.example.exp_2020.util.DbUtil;
import com.example.exp_2020.util.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderUpActivity extends AppCompatActivity {
    private RecyclerView rv;
    private RecyclerView.Adapter<OrderUpAdapter.ViewHolder> ooAdapter;
    private RecyclerView.Adapter<OrderAdapter.ViewHolder> oAdapter;
    private List<Order> list= new ArrayList<Order>();
    private DbUtil dbutil=new DbUtil();
    private OrderDao orderdao=new OrderDao();
    private TextView orderIdtext;
    private Button update_btn;
    private Button cancel_btn;
    private Button delete_btn;
    private TextView username;
    private TextView address;
    private TextView phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_up);

        rv=findViewById(R.id.om_lv);
        orderIdtext=findViewById(R.id.orderid_o);
        update_btn=findViewById(R.id.update_btn);
        cancel_btn=findViewById(R.id.cancel_btn);
        delete_btn=findViewById(R.id.delete_btn);

        rv.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(OrderUpActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(mLayoutManager);

        list=getData(new Order());
        ooAdapter = new OrderUpAdapter(list,listener);
        rv.setAdapter(ooAdapter);
        ooAdapter.notifyDataSetChanged();

        /*监听事件——更新状态*/
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] item={"未处理","烹饪中","配送中","已送达","待付款","已完成"};
                final int[] i = {-1};
                AlertDialog.Builder ab=new AlertDialog.Builder(OrderUpActivity.this);
                ab.setTitle("更新订单状态");
                ab.setSingleChoiceItems(item, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                i[0] =0;
                                break;
                            case 1:
                                i[0]=1;
                                break;
                            case 2:
                                i[0]=2;
                                break;
                            case 3:
                                i[0]=3;
                                break;
                            case 4:
                                i[0]=4;
                                break;
                            case 5:
                                i[0]=6;
                                break;
                        }
                    }
                });
                ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int Status = 0;
                        switch (i[0]){
                            case 0:
                                Status=0;
                                break;
                            case 1:
                                Status=1;
                                break;
                            case 2:
                                Status=2;
                                break;
                            case 3:
                                Status=3;
                                break;
                            case 4:
                                Status=4;
                                break;
                            case 6:
                                Status=6;
                                break;
                        }
                        Intent i=getIntent();
                        Bundle b=i.getExtras();
                        String orderid=b.getString("orderId");
                        Order order=new Order();
                        order.setOrderId(orderid);
                        int finalStatus = Status;
                        new Thread(){
                            public void run() {
                                Looper.prepare();
                                Connection con=null;
                                try {
                                    con=dbutil.getcon();
                                    Log.d("连接","成功");
                                    order.setOrderStatus(finalStatus);
                                    ResultSet rs=orderdao.orderList(con,order);
                                    Log.d("获取","成功");
                                    int modifyNum = orderdao.orderStatusModify(con, order);

                                    if (modifyNum == 1) {

                                        Toast.makeText(OrderUpActivity.this,"订单状态更新成功",Toast.LENGTH_LONG).show();
//                                        Intent i=new Intent(OrderUpActivity.this,OrderManageActivity.class);
//                                        startActivity(i);
                                        finish();

                                    } else {
                                        Looper.prepare();
                                        Toast.makeText(OrderUpActivity.this,"订单状态更新失败",Toast.LENGTH_LONG).show();
                                        Looper.loop();

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
                                Looper.loop();
                            }
                        }.start();

                    }

                });
                ab.setNegativeButton("取消",null);
                ab.show();
            }
        });
        /*监听事件——取消订单*/
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b=new AlertDialog.Builder(OrderUpActivity.this);
                b.setTitle("取消订单");
                b.setMessage("是否取消订单？");
                b.setPositiveButton("确认取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i=getIntent();
                        Bundle b=i.getExtras();
                        String orderid=b.getString("orderId");
                        Order order=new Order();
                        order.setOrderId(orderid);
                        new Thread(){
                            public void run() {
                                Looper.prepare();
                                Connection con=null;
                                try {
                                    con=dbutil.getcon();
                                    Log.d("连接","成功");
                                    order.setOrderStatus(5);
                                    ResultSet rs=orderdao.orderList(con,order);
                                    Log.d("获取","成功");
                                    int modifyNum = orderdao.orderStatusModify(con, order);
                                    if (modifyNum == 1) {
                                        Toast.makeText(OrderUpActivity.this,"取消订单成功",Toast.LENGTH_LONG).show();
//                                        Intent i=new Intent(OrderUpActivity.this,OrderManageActivity.class);
//                                        startActivity(i);
                                        finish();
                                    } else {
                                        Looper.prepare();
                                        Toast.makeText(OrderUpActivity.this,"取消订单失败",Toast.LENGTH_LONG).show();
                                        Looper.loop();
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
                                Looper.loop();
                            }
                        }.start();
                    }
                });
                b.setNegativeButton("返回",null);
                b.show();
            }
        });
        /*监听事件——删除订单*/
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b=new AlertDialog.Builder(OrderUpActivity.this);
                b.setTitle("删除订单");
                b.setMessage("是否删除订单？");
                b.setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i=getIntent();
                        Bundle b=i.getExtras();
                        String orderid=b.getString("orderId");
                        Order order=new Order();
                        order.setOrderId(orderid);
                        new Thread(){
                            public void run() {
                                Looper.prepare();
                                Connection con=null;
                                try {
                                    con=dbutil.getcon();
                                    int modifyNum = orderdao.deleteOrder(con, order);
                                    if (modifyNum == 1) {
                                        Toast.makeText(OrderUpActivity.this,"删除订单成功",Toast.LENGTH_LONG).show();
//                                        Intent i=new Intent(OrderUpActivity.this,OrderManageActivity.class);
//                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(OrderUpActivity.this,"删除订单失败",Toast.LENGTH_LONG).show();
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
                                Looper.loop();
                            }
                        }.start();
                    }
                });
                b.setNegativeButton("返回",null);
                b.show();
            }
        });

    }
    /*获取数据*/
    private List<Order> getData(Order o) {
        Intent i=getIntent();
        Bundle b=i.getExtras();
        String orderid=b.getString("orderId");
        orderIdtext.setText(orderid);
        username=findViewById(R.id.username_send);
        phone=findViewById(R.id.phone_send);
        address=findViewById(R.id.address_send);
        new Thread(){
            public void run() {
                Connection con=null;
                try {
                    con=dbutil.getcon();
                    Log.d("连接","成功");
                    Order order=new Order();
                    order.setOrderId(orderid);
                    ResultSet rs=orderdao.orderList(con,order);
                    Log.d("获取","成功");
                    while (rs.next()) {
                        username.setText(rs.getString("userName"));
                        if (StringUtil.isEmpty(rs.getString("phone"))) {
                            phone.setText("");
                        } else
                            phone.setText(rs.getString("phone"));
                        address.setText(rs.getString("address"));
                        Log.w("", "" + rs.getString("phone"));
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    try {
                        dbutil.closeCon(con);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                try {
                    con=dbutil.getcon();
                    Order or=new Order();
                    or.setOrderId(orderid);
                    ResultSet r = orderdao.orderFoodList(con, or);
                    while (r.next()) {
                        Order oo=new Order();
                        oo.setFoodName(r.getString("FoodName"));
                        oo.setFoodId(r.getInt("FoodId"));
                        oo.setFoodPrice(r.getFloat("FoodPrice"));
                        oo.setFoodNum(r.getInt("FoodNum"));
                        list.add(oo);
                        Log.w("订单清单",""+r.getString("FoodName")+list.size());
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    try {
                        dbutil.closeCon(con);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        return list;
    }
    /*监听事件*/
    View.OnClickListener listener=new View.OnClickListener() { //need按钮
        @Override
        public void onClick(View v) {

        }
        };
}
