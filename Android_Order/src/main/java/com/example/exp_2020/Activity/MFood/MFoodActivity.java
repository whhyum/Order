package com.example.exp_2020.Activity.MFood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.exp_2020.Activity.MyAdapter;
import com.example.exp_2020.Activity.MyUserHelper;
import com.example.exp_2020.R;
import com.example.exp_2020.dao.FoodDao;
import com.example.exp_2020.model.food;
import com.example.exp_2020.util.DbUtil;
import com.example.exp_2020.util.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MFoodActivity extends AppCompatActivity {

    private EditText et;
    private Button search;
    private RecyclerView menu_lv;
    private ImageView image;
    private DbUtil dbutil=new DbUtil();
    private FoodDao fooddao=new FoodDao();
    private List<food> data=new ArrayList<>();
    private RecyclerView.Adapter<MFAdapter.ViewHolder> mAdapter;
    MyUserHelper user_db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_food2);

        et=findViewById(R.id.food_nametext);
        image=findViewById(R.id.image_v);
        search=findViewById(R.id.search);
        menu_lv=findViewById(R.id.menu_lv);

        menu_lv.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MFoodActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        menu_lv.setLayoutManager(mLayoutManager);
        menu_lv.setItemViewCacheSize(100);

        data = getData(new food());

        mAdapter = new MFAdapter(data, listener);
        menu_lv.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
        Log.d("flash1","ok");
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dish=et.getText().toString();
                food food=new food();
                if(StringUtil.isEmpty(dish)){
                    Toast.makeText(MFoodActivity.this,"请输入菜品名",Toast.LENGTH_LONG).show();
                }else{
                    food.setDishes(dish);
                }
                data=getData(food);
                mAdapter.notifyDataSetChanged();
                Log.d("flash","ok");
            }
        }); //ok

        Log.w("aa","aaaaaaaaaaaaaaaaaaaaaa");
        /*点击*/

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        data.clear();
        data = getData(new food());
        mAdapter = new MFAdapter(data, listener);
        menu_lv.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        data.clear();
//        data = getData(new food());
//        mAdapter = new MFAdapter(data, listener);
//        menu_lv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            food f = (food) v.getTag();
            Log.w("id",""+f.getDishes());
            Intent i=new Intent(MFoodActivity.this,MActivity.class);
            Bundle b=new Bundle();
            b.putInt("ID",f.getId());
            b.putString("foodname",f.getDishes());
            b.putFloat("price",f.getPrice());
            b.putString("path",f.getPath());
            b.putString("taste",f.getTaste());
            i.putExtras(b);
            startActivity(i);
        }
    };
    private List<food> getData(food food) {
            data.clear();
        new Thread(){
            public void run() {
                Connection con=null;
                try {
                    con=dbutil.getcon();
                    Log.d("连接","成功");
                    ResultSet rs=fooddao.list(con,food);
                    Log.d("获取","成功");
                    while(rs.next()) {
                        food f=new food();
                        f.setDishes(rs.getString("dishes"));
                        f.setId(rs.getInt("id"));
                        f.setPrice(rs.getFloat("price"));
                        f.setTaste(rs.getString("taste"));
                        f.setPath(rs.getString("path"));
                        data.add(f);
                    }
                    mAdapter.notifyDataSetChanged();
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

        return data;
    }
}
