package com.example.exp_2020.Activity;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exp_2020.R;
import com.example.exp_2020.model.food;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CAdapter extends RecyclerView.Adapter<CAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "MYCAT";
    MyUserHelper user_db;
    List<food> data = new ArrayList();//保存数据的成员对象
    private Context context;//上下文

    public CAdapter( Context context) {//构造函数，保存数据
        this.context = context;
    }


    public void refresh(List<food> data){
        if (data!=null&&data.size()>=0){
            this.data.clear();
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public CAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//在构建行布局对象时调用
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;//生成ViewHolder对象
    }


    @Override
    public void onBindViewHolder(@NonNull CAdapter.ViewHolder holder, int position) {//当行布局的组件要进行数据加载时调用
        food f = data.get(position);
        holder.id.setText(f.getId() + "");//略，其中根据图片文件名，加载到 ImageView 中
        holder.foodname.setText(f.getDishes() + "");
        holder.price.setText(f.getPrice() + "");
        holder.num.setText(f.getNum() + "");
        holder.taste.setText(f.getTaste() + "");
        /*图片 */
        String fname = f.getPath().trim(); //获取图片名
        String temp[] = fname.split("\\\\");
        String img = temp[temp.length - 1];
        Log.i("图片", "" + img);

        File sdPath = Environment.getExternalStorageDirectory();
        File dir = new File(sdPath, "DCIM");
        File file = new File(dir, img);
        Bitmap bit = BitmapFactory.decodeFile(file.getPath());
        if (bit != null)
            holder.imageView.setImageBitmap(bit);
        else
            holder.imageView.setImageResource(R.drawable.dai);

        holder.itemView.setTag(position);

        holder.add.setTag(position);
        holder.de.setTag(position);
    }

    @Override
    public int getItemCount() {//数据的多少
        return data.size();
    }

    //item里面有多个控件可以点击
    public enum ViewName {
        ITEM,
        PRACTISE
    }

    public void setData(List<food> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener {
        void onItemClick(View v, ViewName viewName, int position);

        void onItemLongClick(View v, ViewName practise, int position);
    }

    private OnItemClickListener mOnItemClickListener;//声明自定义的接口

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;

    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(); //getTag()获取数据
        if (mOnItemClickListener != null) {
            switch (v.getId()) {
                case R.id.cart_lv:
                    mOnItemClickListener.onItemClick(v, ViewName.PRACTISE, position);
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, ViewName.ITEM, position);
                    break;
            }
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {//保存行布局上的每个组件的对象引用
        TextView id;
        Button add;
        Button de;
        TextView foodname;
        ImageView imageView;
        TextView price;
        TextView taste;
        TextView num;
        TextView totalprice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            add = itemView.findViewById(R.id.add_num);
            de = itemView.findViewById(R.id.de_num);
            id = itemView.findViewById(R.id.id);
            num = itemView.findViewById(R.id.num);
            totalprice = itemView.findViewById(R.id.totalPrice);
            imageView = itemView.findViewById(R.id.image_v);
            foodname = itemView.findViewById(R.id.foodname);
            price = itemView.findViewById(R.id.price);
            taste = itemView.findViewById(R.id.taste);
            itemView.setOnClickListener(CAdapter.this);
            add.setOnClickListener(CAdapter.this);
            de.setOnClickListener(CAdapter.this);
        }
    }


}


