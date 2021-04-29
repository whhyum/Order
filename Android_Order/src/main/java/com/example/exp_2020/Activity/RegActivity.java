package com.example.exp_2020.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.exp_2020.R;
import com.example.exp_2020.dao.CustomerDao;
import com.example.exp_2020.dao.FoodDao;
import com.example.exp_2020.dao.MasterDao;
import com.example.exp_2020.model.Customer;
import com.example.exp_2020.model.Master;
import com.example.exp_2020.util.DbUtil;
import com.example.exp_2020.util.StringUtil;

import java.sql.Connection;

public class RegActivity extends AppCompatActivity {
    private RadioButton custom;
    private RadioButton worker;
    private Button re;
    private EditText nametext;
    private EditText passnntext;
    private EditText passwordtext;
    private DbUtil dbutil=new DbUtil();
    private FoodDao fooddao=new FoodDao();
    private MasterDao mdao=new MasterDao();
    private CustomerDao cdao=new CustomerDao();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        nametext=findViewById(R.id.nametext);
        passwordtext=findViewById(R.id.passwordtext);
        passnntext=findViewById(R.id.passntext);
        custom=findViewById(R.id.custom);
        worker=findViewById(R.id.worker);
        re=findViewById(R.id.register);

        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {

                    @Override
                    public void run() {
                        super.run();

                        String userName = nametext.getText().toString();
                        String password = passwordtext.getText().toString();
                        String passwordConfirm = passnntext.getText().toString();

                        if (StringUtil.isEmpty(userName)) {
                            Looper.prepare();
                            Toast.makeText(RegActivity.this, "用户名不能为空", Toast.LENGTH_LONG).show();
                            Looper.loop();
                            return;
                        }
                        if (StringUtil.isEmpty(password)) {
                            Looper.prepare();
                            Toast.makeText(RegActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                            Looper.loop();
                            return;
                        }
                        if (!password.equals(passwordConfirm)) {
                            Looper.prepare();
                            Toast.makeText(RegActivity.this, "两次输入的密码不一致", Toast.LENGTH_LONG).show();
                            Looper.loop();
                            return;
                        }

                        if (custom.isChecked()) {
                            Customer c = new Customer(userName, password);
                            Connection con = null;
                            try {
                                con = dbutil.getcon();
                                if (!cdao.isCustomerExist(con, c)) {
                                    int addNum = cdao.CustomerAdd(con, c);
                                    if (addNum == 1) {
                                        Looper.prepare();
                                        Toast.makeText(RegActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                                        finish();
                                        Looper.loop();

                                    } else {
                                        Looper.prepare();
                                        Toast.makeText(RegActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                                        Looper.loop();
                                    }
                                } else {
                                    Looper.prepare();
                                    Toast.makeText(RegActivity.this, "用户名存在，请重新输入！", Toast.LENGTH_LONG).show();
                                    Looper.loop();
                                }
                            } catch (Exception e) {
                                Looper.prepare();
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                Toast.makeText(RegActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                                Looper.loop();
                            } finally {
                                try {
                                    dbutil.closeCon(con);
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        } else if (worker.isChecked()) {
                            Master m = new Master(userName, password);
                            Connection con = null;
                            try {
                                con = dbutil.getcon();
                                if (!mdao.isMasterExist(con, m)) {
                                    int addNum = mdao.masterAdd(con, m);
                                    if (addNum == 1) {
                                        Looper.prepare();
                                        Toast.makeText(RegActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                                        Looper.loop();
                                        finish();

                                    } else {
                                        Looper.prepare();
                                        Toast.makeText(RegActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                                        Looper.loop();
                                    }
                                } else {
                                    Looper.prepare();
                                    Toast.makeText(RegActivity.this, "用户名存在，请重新输入！", Toast.LENGTH_LONG).show();
                                    Looper.loop();
                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                Toast.makeText(RegActivity.this, "注册失败", Toast.LENGTH_LONG).show();

                            } finally {
                                try {
                                    dbutil.closeCon(con);
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Looper.prepare();
                            Toast.makeText(RegActivity.this, "请选择一个身份进行注册", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
                    }
                }.start();


            }
        });


    }
}
