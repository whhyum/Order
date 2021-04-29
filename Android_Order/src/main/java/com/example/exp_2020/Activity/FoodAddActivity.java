package com.example.exp_2020.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exp_2020.Activity.MFood.MActivity;
import com.example.exp_2020.Activity.MFood.MFoodActivity;
import com.example.exp_2020.R;
import com.example.exp_2020.dao.FoodDao;
import com.example.exp_2020.dao.OrderDao;
import com.example.exp_2020.model.food;
import com.example.exp_2020.util.DbUtil;
import com.example.exp_2020.util.StringUtil;

import java.sql.Connection;

public class FoodAddActivity extends AppCompatActivity {
    //调取系统摄像头的请求码
    private static final int MY_ADD_CASE_CALL_PHONE = 6;
    //打开相册的请求码
    private static final int MY_ADD_CASE_CALL_PHONE2 = 7;
    private DbUtil dbutil = new DbUtil();
    private FoodDao fooddao = new FoodDao();
    private OrderDao orderdao = new OrderDao();
    private EditText remark_et;
    private EditText food_text;
    private EditText price_text;
    private EditText taste_text;
    private ImageView imageView;
    private Button select_btn;
    private Button insert_btn;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_add);

        remark_et = findViewById(R.id.remark);
        food_text = findViewById(R.id.foodname_t);
        price_text = findViewById(R.id.foodprice_t);
        taste_text = findViewById(R.id.taste_t);
        imageView = findViewById(R.id.image_i);
        select_btn = findViewById(R.id.select_btn);
        insert_btn = findViewById(R.id.insert_btn);

        /*添加菜品*/
        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = food_text.getText().toString();
                String price = price_text.getText().toString();
                String remark = remark_et.getText().toString();
                String taste = taste_text.getText().toString();
                if (path == null) {
                    path = "dai.png";
                }
                if (StringUtil.isEmpty(name)) {
                    Toast.makeText(FoodAddActivity.this, "菜品名不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (StringUtil.isEmpty(price)) {
                    Toast.makeText(FoodAddActivity.this, "菜品价格不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                food food = new food();
                food.setDishes(name);
                food.setPath(path);
                food.setPrice(Float.parseFloat(price));
                food.setRemark(remark);
                food.setTaste(taste);

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Connection con = null;
                        try {
                            con = dbutil.getcon();
                            int n = fooddao.add(con, food, path);
                            if (n == 1) {
                                Looper.prepare();
                                Toast.makeText(FoodAddActivity.this, "菜品添加成功", Toast.LENGTH_LONG).show();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        food_text.setText("");
                                        price_text.setText("");
                                        remark_et.setText("");
                                        taste_text.setText("");
                                        imageView.setImageURI(null);
                                    }
                                });
                                Looper.loop();
                            } else
                                Looper.prepare();
                            Toast.makeText(FoodAddActivity.this, "菜品添加失败", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(FoodAddActivity.this, "菜品添加失败", Toast.LENGTH_LONG).show();

                        } finally {
                            try {
                                dbutil.closeCon(con);
                            } catch (Exception e) {
                                // TODO 自动生成的 catch 块
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        });

        /*上传图片*/
        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                imageView.setImageURI(uri);
                path = getRealFilePath(FoodAddActivity.this, uri);
                Log.w("路径", "" + data.getDataString() + data + "转后" + path);
            }
        }
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }

        String temp[] = data.replaceAll("\\\\","/").split("/");
        if(temp.length>1){
            data=temp[temp.length - 1];
        }
        data="D:"+"\\\\"+data;
        return data;
    }
}



