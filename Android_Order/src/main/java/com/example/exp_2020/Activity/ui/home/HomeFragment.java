package com.example.exp_2020.Activity.ui.home;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exp_2020.Activity.CartActivity;
import com.example.exp_2020.Activity.MainActivity;
import com.example.exp_2020.Activity.MyAdapter;
import com.example.exp_2020.Activity.MyUserHelper;
import com.example.exp_2020.R;
import com.example.exp_2020.dao.FoodDao;
import com.example.exp_2020.model.food;
import com.example.exp_2020.util.DbUtil;
import com.example.exp_2020.util.StringUtil;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class HomeFragment extends Fragment {
    private EditText et;
    private Button search;
    private Button add_cart;
    private RecyclerView menu_lv;
    private HomeViewModel homeViewModel;
    private ImageView image;
    private DbUtil dbutil=new DbUtil();
    private FoodDao fooddao=new FoodDao();
    private List<food> data= new ArrayList<food>();
    private List<food> list= new ArrayList<food>();
    private RecyclerView.Adapter<MyAdapter.ViewHolder> mAdapter;
    MyUserHelper user_db;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        et=root.findViewById(R.id.food_nametext);
        image=root.findViewById(R.id.image_v);
        search=root.findViewById(R.id.search);
        add_cart=root.findViewById(R.id.add_cart);
        menu_lv=root.findViewById(R.id.menu_lv);

        menu_lv.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(root.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        menu_lv.setLayoutManager(mLayoutManager);

        data = getData(new food());

        mAdapter = new MyAdapter(data, listener);
        menu_lv.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
        Log.d("flash1","ok");
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        String dish=et.getText().toString();
                        food food=new food();
                        if(StringUtil.isEmpty(dish)){
                            Toast.makeText(root.getContext(),"请输入菜品名",Toast.LENGTH_LONG).show();
                        }else{
                            food.setDishes(dish);
                        }
                        data=getData(food);
                mAdapter.notifyDataSetChanged();
                Log.d("flash","ok");
            }
        }); //ok

        mAdapter.notifyDataSetChanged();
        Log.w("aa","aaaaaaaaaaaaaaaaaaaaaa");
        /*点击*/


        return root;

    }  //oncreate

    @Override
    public void onResume() {
        super.onResume();
        data.clear();
        data = getData(new food());
        mAdapter = new MyAdapter(data, listener);
        menu_lv.setAdapter(mAdapter);
    }


    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            food f = (food) v.getTag();
            int id=f.getId();
            Log.w("id",""+f.getDishes());
//            int num =0;
            user_db= new MyUserHelper(getContext());
            SQLiteDatabase db = user_db.getWritableDatabase();
            ContentValues cc=new ContentValues();
            Cursor cur =db.rawQuery("select * from Cart where _id="+f.getId(),null);
            cur.moveToFirst();
            while (cur.moveToNext()){
                if(id==cur.getInt(cur.getColumnIndex("_id"))){
                    Log.w("aa","重复"+cur.getColumnIndex("_id"));
                    Log.w("aa","重复"+f.getDishes()+cc.get("num"));
                    Toast.makeText(getContext(), "购物车中已存在", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if(cur.moveToFirst()!=true){
                cc.put("_id",f.getId());
                cc.put("foodname",f.getDishes());
                cc.put("taste",f.getTaste());
                cc.put("price",f.getPrice());
                cc.put("path",f.getPath());
                cc.put("num",1);
                Log.w("aa","不重复"+f.getDishes()+cc.get("num"));
                db.insert("Cart",null,cc);
                Toast.makeText(getContext(), "加入购物车", Toast.LENGTH_SHORT).show();
            }else{
                int n= cur.getInt(cur.getColumnIndex("num"));
                n++;
                db.execSQL("update Cart set num ="+n+" where _id= "+id);
                Log.w("aa","重复"+cur.getColumnIndex("_id"));
                Log.w("aa","重复"+f.getDishes()+cc.get("num"));
                Toast.makeText(getContext(), f.getDishes()+":数量+1", Toast.LENGTH_SHORT).show();
            }
            cur.close();
            Log.w("aa","aaaa"+f.getDishes()+cc.get("num"));


        }
    };
    View.OnLongClickListener l=new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            return false;
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
