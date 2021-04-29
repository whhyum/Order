package com.example.exp_2020.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exp_2020.R;
import com.example.exp_2020.dao.OrderDao;
import com.example.exp_2020.model.Order;
import com.example.exp_2020.model.food;
import com.example.exp_2020.util.DbUtil;
import com.example.exp_2020.util.StringUtil;

import java.io.Serializable;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements Serializable {

    private TextView id;
    private RecyclerView cart_lv;
    private List<Order> orderlist = new ArrayList<Order>();
    private OrderDao orderdao = new OrderDao();
    private DbUtil dbutil = new DbUtil();
    private CAdapter cAdapter;
    private List<food> list = new ArrayList<food>();
    private AlertDialog ad;
    private List<food> data = new ArrayList<food>();
    TextView num;
    int n;
    TextView total;
    float p;
    float totalp = 0;
    private Button OK;
    private String address;
//    String[] keys = {"_id", "foodname", "taste","price","num","img"};//keys和数组id一一对应
//    int[] ids = {R.id.id, R.id.foodname, R.id.taste, R.id.price,R.id.num,R.id.image_v};

    private MyUserHelper user_db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        OK = findViewById(R.id.OK);

        id = findViewById(R.id.id);

        num = findViewById(R.id.num);
        total = findViewById(R.id.totalPrice);
        cart_lv = findViewById(R.id.cart_lv);
        cart_lv.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(CartActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cart_lv.setLayoutManager(mLayoutManager);

        cAdapter = new CAdapter(this);
        cart_lv.setAdapter(cAdapter);
        cAdapter.setOnItemClickListener(MyItemClickListener);

        user_db = new MyUserHelper(CartActivity.this);
        //金额
        comPrice();


        cAdapter.refresh(getData());

        /*选择堂食或外卖*/
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] item = {"堂食点餐", "外卖送餐"};
                final int[] i = {0};
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("请选择需要的服务");
                builder.setSingleChoiceItems(item, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                i[0] = 1;
                                break;
                            case 1:
                                i[0] = 2;
                                break;
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (i[0]) {
                            case 1:
                                table();
                                break;
                            case 2:
                                Intent i = new Intent(CartActivity.this, SendActivity.class);
                                float orderTotalMoney = Float.parseFloat(total.getText().toString());
                                Log.w(null, "ttttttttttttt" + orderTotalMoney);
                                Bundle b = new Bundle();
                                b.putString("totalprice", String.valueOf(orderTotalMoney));
                                i.putExtras(b);
                                startActivity(i);
                                break;
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ad.dismiss();
                    }
                });
                ad = builder.create();
                ad.show();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cart_lv.setAdapter(cAdapter);
        cAdapter.refresh(getData());
        cAdapter.setOnItemClickListener(MyItemClickListener);
    }

    private CAdapter.OnItemClickListener MyItemClickListener = new CAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, CAdapter.ViewName viewName, int position) {
            //viewName可区分item及item内部控件
            num = findViewById(R.id.num);
            user_db = new MyUserHelper(CartActivity.this);
            SQLiteDatabase db = user_db.getWritableDatabase();
            int n = data.get(position).getNum();
            int id = data.get(position).getId();
            switch (v.getId()) {
                case R.id.add_num:
                    n++;
                    db.execSQL("update Cart set num =" + n + " where _id= " + id);
                    Toast.makeText(CartActivity.this, "增加", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.de_num:
                    if (n == 1) {
                        Toast.makeText(CartActivity.this, "已达到最小数量", Toast.LENGTH_SHORT).show();
                    } else {
                        n--;
                        db.execSQL("update Cart set num =" + n + " where _id= " + id);
                        Toast.makeText(CartActivity.this, "减少", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    AlertDialog.Builder bb = new AlertDialog.Builder(CartActivity.this);
                    bb.setTitle("确认从购物车删除？").setNegativeButton("取消", null);
                    bb.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int ID = data.get(position).getId();
                            SQLiteDatabase db = user_db.getWritableDatabase();
                            db.delete("Cart", "_id = ?", new String[]{String.valueOf(ID)});
                            data.remove(position);
                            cart_lv.setAdapter(cAdapter);
                            Intent i = new Intent(CartActivity.this, CartActivity.class);
                            finish();
                            startActivity(i);
                            cAdapter.refresh(getData());
                            Toast.makeText(CartActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    bb.show();
                    break;
            }
            cAdapter.refresh(getData());
            comPrice();
        }

        @Override
        public void onItemLongClick(View v, CAdapter.ViewName viewName, int position) {
            Log.w("长按", "");


        }

    };



    /*计算总金额*/
    public void comPrice() {
        totalp=0;
        user_db = new MyUserHelper(CartActivity.this);
        SQLiteDatabase db = user_db.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * from Cart", null);//构造适配器
        while (cur.moveToNext()) {
            int num = cur.getInt(cur.getColumnIndex("num"));
            float p = cur.getFloat(cur.getColumnIndex("price"));
            totalp += p * num;
        }
        DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(totalp);//format 返回的是字符串
        total.setText(p);
        cur.close();
        user_db.close();
    }

    /*获取加入购物车中的食物集*/
    private List<food> getData() {
        data.clear();
        user_db = new MyUserHelper(CartActivity.this);
        SQLiteDatabase db = user_db.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * from Cart", null);//构造适配器
        while (cur.moveToNext()) {
            food f = new food();
            f.setNum(cur.getInt(cur.getColumnIndex("num")));
            f.setDishes(cur.getString(cur.getColumnIndex("foodname")));
            f.setTaste(cur.getString(cur.getColumnIndex("taste")));
            f.setPrice(cur.getFloat(cur.getColumnIndex("price")));
            f.setPath(cur.getString(cur.getColumnIndex("path")));
            f.setId(cur.getInt(cur.getColumnIndex("_id")));
            data.add(f);
        }
        cur.close();
        user_db.close();
        return data;
    }


    /*堂食点餐输入桌台号*/
    private void table() {
        final EditText inputServer = new EditText(CartActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        builder.setTitle("输入桌台号").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton("提交订单", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String address = inputServer.getText().toString();
                if (StringUtil.isEmpty(address)) {
                    Toast.makeText(CartActivity.this, "请输入餐桌号", Toast.LENGTH_SHORT).show();
                }
                order(address, null);
            }
        });
        builder.show();
    }

    /*提交订单*/
    private void order(String address, String phone) {
        String Id = new SimpleDateFormat("yyyyMMdd")
                .format(new java.util.Date(System.currentTimeMillis()));//将当前时间作为订单号码
        String orderId = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new java.util.Date(System.currentTimeMillis()));//将当前时间作为订单号码
        Log.w("日期", "id" + Id);
        if (StringUtil.isEmpty(total.getText().toString())) {
            Toast.makeText(CartActivity.this, "请选择商品后再结算", Toast.LENGTH_SHORT).show();
            return;
        }
        float orderTotalMoney = Float.parseFloat(this.total.getText().toString());
        int rowNum = cAdapter.getItemCount();
        new Thread() {
            public void run() {
                Looper.prepare();
                user_db = new MyUserHelper(CartActivity.this);
                SQLiteDatabase db = user_db.getReadableDatabase();
                Cursor cur = db.rawQuery("Select * from Cart", null);//构造适配
                Connection con = null;
                try {
                    con = dbutil.getcon();
                    String userName = LoginActivity.currentUser.getUserName();
                    Order order = new Order(Id, orderId, 0, orderTotalMoney, userName, "桌号" + address, phone);
                    int addNum = orderdao.order_infoAdd(con, order);
                    int rowNum = cAdapter.getItemCount();
                    if (addNum == 1) {  //插入数据到order_food表
                            while (cur.moveToNext()) {
                            Order oo = new Order();
                            oo.setOrderId(orderId);
                            oo.setFoodId(cur.getInt(cur.getColumnIndex("_id")));
                            oo.setFoodName(cur.getString(cur.getColumnIndex("foodname")));
                            oo.setFoodPrice(cur.getFloat(cur.getColumnIndex("price")));
                            oo.setFoodNum(cur.getInt(cur.getColumnIndex("num")));
                            oo.setFoodTotalPrice(orderTotalMoney);
                            addNum ++;
                            orderdao.order_foodAdd(con, oo);
                        }
                    }
                    if (addNum >=1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("订单提交成功");
                        builder.setMessage("请记住您的订单号：" + orderId);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(CartActivity.this, MainActivity.class);
                                db.execSQL("DELETE FROM Cart");
                                startActivity(i);
                            }
                        });
                        builder.show();
                    } else {
                        Log.w("行数", "" + addNum + "车中" + rowNum);
                        Toast.makeText(CartActivity.this, "订单提交失败", Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                    cur.close();
                    cAdapter.refresh(getData());
                    user_db.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(CartActivity.this, "订单提交失败", Toast.LENGTH_SHORT).show();

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
    }


}
