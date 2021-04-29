package com.example.exp_2020.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exp_2020.Activity.ui.home.HomeFragment;
import com.example.exp_2020.R;
import com.example.exp_2020.dao.OrderDao;
import com.example.exp_2020.model.Order;
import com.example.exp_2020.util.DbUtil;
import com.example.exp_2020.util.StringUtil;

import java.sql.Connection;

public class SendActivity extends AppCompatActivity {
    private Button send;
    private EditText nametext;
    private EditText phonetext;
    private EditText addresstext;
    private OrderDao orderdao=new OrderDao();
    private DbUtil dbutil=new DbUtil();
    private CAdapter cAdapter;
    private MyUserHelper user_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        send=findViewById(R.id.send);
        nametext=findViewById(R.id.nametext);
        phonetext=findViewById(R.id.phonetext);
        addresstext=findViewById(R.id.addresstext);

        nametext.setText(LoginActivity.currentUser.getUserName()+"");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =nametext.getText().toString();
                String phone =phonetext.getText().toString();
                String address =addresstext.getText().toString();
                if(StringUtil.isEmpty(name)){
                    Toast.makeText(SendActivity.this,"请输入您的名字",Toast.LENGTH_SHORT).show();
                }
                if(StringUtil.isEmpty(phone)){
                    Toast.makeText(SendActivity.this,"请输入您的手机号码",Toast.LENGTH_SHORT).show();
                }
                if(StringUtil.isEmpty(address)){
                    Toast.makeText(SendActivity.this,"请输入您的地址",Toast.LENGTH_SHORT).show();
                }
                order(name,address,phone);
            }
        });

    }

    private void order(String name, String address, String phone) {

        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        String totalprice=b.getString("totalprice");

        String Id = new java.text.SimpleDateFormat("yyyyMMdd")
                .format(new java.util.Date());//将当前时间作为订单号码
        String orderId = new java.text.SimpleDateFormat("yyyyMMddHHmmss")
                .format(new java.util.Date());//将当前时间作为订单号码
        if(StringUtil.isEmpty(totalprice)) {
            Log.i("","jjjjjjjjjj"+totalprice);
            Toast.makeText(SendActivity.this,"请选择商品后再结算",Toast.LENGTH_SHORT).show();
            return;
        }
        float orderTotalMoney = Float.parseFloat(totalprice);
        new Thread(){
            public void run() {
                user_db= new MyUserHelper(SendActivity.this);
                SQLiteDatabase db = user_db.getReadableDatabase();
                Log.i("提交订单", "sssssssssssssss");
                Cursor cur = db.rawQuery("Select * from Cart", null);//构造适配器
                cur.moveToFirst();
                Connection con = null;
                try {
                    con = dbutil.getcon();
                    Order order = new Order(Id,orderId, 0, orderTotalMoney,name,address,phone);
                    int addNum=orderdao.order_infoAdd(con, order);
                    if(addNum==1) {  //插入数据到order_food表
                        do{
                            Order oo = new Order();
                            oo.setOrderId(orderId);
                            oo.setFoodId(cur.getInt(cur.getColumnIndex("_id")));
                            oo.setFoodName(cur.getString(cur.getColumnIndex("foodname")));
                            oo.setFoodPrice(cur.getFloat(cur.getColumnIndex("price")));
                            oo.setFoodNum(cur.getInt(cur.getColumnIndex("num")));
                            oo.setFoodTotalPrice(orderTotalMoney);
                            orderdao.order_foodAdd(con, oo);
                            addNum++;
//                    int id = cur.getInt(cur.getColumnIndex("_id"));
//                    String foodname=cur.getString(cur.getColumnIndex("foodname"));
//                    String taste=cur.getString(cur.getColumnIndex("taste"));
//                    float price = cur.getFloat(cur.getColumnIndex("price"));
//                    int num=cur.getInt(cur.getColumnIndex("num"));
//                    String path=cur.getString(cur.getColumnIndex("taste"));
                        }
                        while (cur.moveToNext());
                    }
                    if (addNum >=1) {
                        Looper.prepare();
                        AlertDialog.Builder builder=new AlertDialog.Builder(SendActivity.this);
                        builder.setTitle("订单提交成功");
                        builder.setMessage("请记住您的订单号："+orderId);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.execSQL("DELETE FROM Cart");
                                Intent i=new Intent(SendActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                        });
                        builder.show();

                        Looper.loop();
//                        Toast.makeText(SendActivity.this,"订单提交成功，请记住您的订单号："+orderId,Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w("行数",""+addNum+"车中");
                        Toast.makeText(SendActivity.this,"订单提交失败",Toast.LENGTH_SHORT).show();
                    }

                    cur.close();
                    cAdapter.notifyDataSetChanged();
                    user_db.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Looper.prepare();
                    e.printStackTrace();
                    Toast.makeText(SendActivity.this,"订单提交失败",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } finally {
                    try {
                        dbutil.closeCon(con);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }}.start();
    }


}
