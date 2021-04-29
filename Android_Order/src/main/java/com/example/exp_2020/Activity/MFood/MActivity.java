package com.example.exp_2020.Activity.MFood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exp_2020.Activity.FoodAddActivity;
import com.example.exp_2020.R;
import com.example.exp_2020.dao.FoodDao;
import com.example.exp_2020.dao.OrderDao;
import com.example.exp_2020.model.food;
import com.example.exp_2020.util.DbUtil;
import com.example.exp_2020.util.StringUtil;

import java.io.File;
import java.sql.Connection;

public class MActivity extends AppCompatActivity {
    private DbUtil dbutil=new DbUtil();
    private FoodDao fooddao=new FoodDao();
    private OrderDao orderdao=new OrderDao();
    private TextView id_et;
    private EditText food_text;
    private EditText price_text;
    private EditText taste_text;
    private ImageView imageView;
    private Button select_btn;
    private Button update_btn;
    private Button delete_btn;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);

        id_et=findViewById(R.id.id_t);
        food_text=findViewById(R.id.foodname_t);
        price_text=findViewById(R.id.foodprice_t);
        taste_text=findViewById(R.id.taste_t);
        imageView=findViewById(R.id.image_i);
        select_btn=findViewById(R.id.select_btn);
        update_btn=findViewById(R.id.update);
        delete_btn=findViewById(R.id.delete);

        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        int id= (int) b.get("ID");
        String foodname=b.getString("foodname");
        Float price=b.getFloat("price");
        path=b.getString("path");
        String taste=b.getString("taste");

        /*获取图片资源*/
        String fname=path.trim(); //获取图片名
        String temp[]=fname.split("\\\\");
        String img=temp[temp.length-1];
        Log.i("图片",""+img);

        File sdPath = Environment.getExternalStorageDirectory();
        File dir = new File(sdPath, "DCIM");
        File file = new File(dir, img);
        Bitmap bit = BitmapFactory.decodeFile(file.getPath());
        if (bit != null)
            imageView.setImageBitmap(bit);
        else
            imageView.setImageResource(R.drawable.dai);

        id_et.setText(id+"");
        food_text.setText(foodname+"");
        price_text.setText(price+"");
        taste_text.setText(taste+"");
        /*选择图片*/
        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });

        /*更改*/
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_et=findViewById(R.id.id_t);
                food_text=findViewById(R.id.foodname_t);
                price_text=findViewById(R.id.foodprice_t);
                taste_text=findViewById(R.id.taste_t);
                String dishes = food_text.getText().toString();
                String taste = taste_text.getText().toString();
                String price = price_text.getText().toString();
                Log.w("更改图片",""+path);
                if (StringUtil.isEmpty(dishes)) {
                    Toast.makeText(MActivity.this,"菜品名称不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                if (StringUtil.isEmpty(price)) {
                    Toast.makeText(MActivity.this,"菜品价格不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                food food = new food(id, dishes, taste, Float.parseFloat(price), path);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Looper.prepare();
                        Connection con = null;
                        try {
                            con = dbutil.getcon();
                            int modifyNum = fooddao.foodModify(con, food);
                            if (modifyNum == 1) {
                                Toast.makeText(MActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                                finish();
                            } else
                                Toast.makeText(MActivity.this,"修改失败",Toast.LENGTH_LONG).show();
                            Looper.loop();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            Toast.makeText(MActivity.this,"修改失败",Toast.LENGTH_LONG).show();
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
        });

        /*删除*/
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Connection con = null;
                        try {
                            con = dbutil.getcon();
                            int deleteNum = fooddao.foodDelete(con, id);
                            Looper.prepare();
                            if (deleteNum == 1) {
                                Toast.makeText(MActivity.this,"删除菜品成功",Toast.LENGTH_LONG).show();

                                finish();
                            } else
                                Toast.makeText(MActivity.this,"删除菜品失败",Toast.LENGTH_LONG).show();
                            Looper.loop();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MActivity.this,"删除菜品失败",Toast.LENGTH_LONG).show();
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
                path = getRealFilePath(MActivity.this, uri);
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

//        String fname = data.trim(); //获取图片名
        String temp[] = data.replaceAll("\\\\","/").split("/");
        if(temp.length>1){
            data=temp[temp.length - 1];
        }
        data="D:"+"\\\\"+data;
        return data;
    }
}
