package com.example.exp_2020.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.exp_2020.R;
import com.example.exp_2020.dao.CustomerDao;
import com.example.exp_2020.dao.MasterDao;
import com.example.exp_2020.model.Customer;
import com.example.exp_2020.model.Master;
import com.example.exp_2020.util.DbUtil;
import com.example.exp_2020.util.StringUtil;

import java.sql.Connection;

public class LoginActivity extends AppCompatActivity {
    private EditText nametext;
    private EditText passwordtext;
    private Button btn=null;
    private DbUtil dbutil=new DbUtil();
    private MasterDao masterdao=new MasterDao();
    private CustomerDao cusdao=new CustomerDao();
    public static Customer currentUser = new Customer();
    private RadioButton custom;
    private RadioButton worker;
    private Button re;

    Connection con=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nametext=findViewById(R.id.nametext);
        passwordtext=findViewById(R.id.passwordtext);

        custom=findViewById(R.id.custom);
        worker=findViewById(R.id.worker);
        re=findViewById(R.id.register);
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,RegActivity.class);
                startActivity(i);
            }
        });

        btn=findViewById(R.id.login);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String userName = nametext.getText().toString();
                String password = passwordtext.getText().toString();
                if (StringUtil.isEmpty(userName)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("提示");// 设置标题
                    // builder.setIcon(R.drawable.ic_launcher);//设置图标
                    builder.setMessage("用户名不能为空");// 为对话框设置内容
                    builder.setPositiveButton("我知道了", null);
                    builder.show();
                    return;
                }
                if (StringUtil.isEmpty(password)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("提示");// 设置标题
                    // builder.setIcon(R.drawable.ic_launcher);//设置图标
                    builder.setMessage("用户名不能为空");// 为对话框设置内容
                    builder.setPositiveButton("我知道了", null);
                    builder.show();
                    return;
                }
                new Thread(){
                    public void run(){
                        if(custom.isChecked()) {
                            try {
                                Customer cus = new Customer(userName, password);
                                con = dbutil.getcon();
                                Customer cc = cusdao.login(con, cus);
                                if (cc != null) {
                                    Looper.prepare();
                                    currentUser .setUserName(userName);
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this, Welcome.class);
                                    startActivity(intent);
                                    finish();
                                    Looper.loop();
                                } else {
                                    Log.d("", "失败");
                                    Looper.prepare();
                                    Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_LONG).show();
                                    Looper.loop();
                                }
                            } catch (Exception e) {
                                // TODO 自动生成的 catch 块
                                e.printStackTrace();
                            } finally {
                                try {
                                    dbutil.closeCon(con);
                                } catch (Exception e) {
                                    // TODO 自动生成的 catch 块
                                    e.printStackTrace();
                                }
                            }
                        }else if(worker.isChecked()){
                            try {
                                Master m=new Master(userName,password);
                                con=dbutil.getcon();
                                Master master=masterdao.login(con,m);
                                if (master!= null) {
                                    Looper.prepare();
                                    currentUser .setUserName("管理员");
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this, Welcome.class);
                                    startActivity(intent);
                                    finish();
                                    Looper.loop();
                                } else {
                                    Log.d("", "失败");
                                    Looper.prepare();
                                    Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_LONG).show();
                                    Looper.loop();
                                }
                            } catch (Exception e) {
                                // TODO 自动生成的 catch 块
                                e.printStackTrace();
                            } finally {
                                try {
                                    dbutil.closeCon(con);
                                } catch (Exception e) {
                                    // TODO 自动生成的 catch 块
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this, "请选择一个身份", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }

                    }
                }.start();
            }
        });


    }
}

