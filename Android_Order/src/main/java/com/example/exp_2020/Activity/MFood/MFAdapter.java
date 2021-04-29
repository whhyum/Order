package com.example.exp_2020.Activity.MFood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exp_2020.R;
import com.example.exp_2020.model.food;
import com.example.exp_2020.util.ImageUtil;

import java.io.File;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MFAdapter extends RecyclerView.Adapter<MFAdapter.ViewHolder> {
    private View.OnClickListener listener;
    List<food> data ;//保存数据的成员对象


    public MFAdapter(List<food> data, View.OnClickListener listener) {//构造函数，保存数据

        this.data = data;
        this.listener=listener;
    }


    @NonNull
    @Override
    public MFAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//在构建行布局对象时调用
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.mfood_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(listener);
        return vh;//生成ViewHolder对象
    }
@Override
public long getItemId(int position){
        return position;
}

    @Override
    public void onBindViewHolder(@NonNull MFAdapter.ViewHolder holder, int position) {//当行布局的组件要进行数据加载时调用
        food f = data.get(position);
        holder.id.setText(f.getId()+"");//略，其中根据图片文件名，加载到 ImageView 中
        holder.foodname.setText(f.getDishes()+"");
        holder.price.setText(f.getPrice()+"");
        holder.taste.setText(f.getTaste()+"");

        /*获取图片资源*/
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
        holder.itemView.setTag(f);//附加数据

    }

    @Override
    public int getItemCount() {//数据的多少
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {//保存行布局上的每个组件的对象引用
        TextView id;
        TextView foodname;
        ImageView imageView;
        TextView price;
        TextView taste;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.id);
            imageView = itemView.findViewById(R.id.image_v);
            foodname=itemView.findViewById(R.id.foodname);
            price=itemView.findViewById(R.id.price);
            taste=itemView.findViewById(R.id.taste);
        }
    }

    public enum ViewName {
        ITEM,
        PRACTISE
    }

    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener  {
        void onItemClick(View v, ViewName viewName, int position);
        void onItemLongClick(View v);
    }

    private OnItemClickListener mOnItemClickListener;//声明自定义的接口

    //定义方法并传给外面的使用者
    public void setOnItemClickListener(OnItemClickListener  listener) {
        this.mOnItemClickListener  = listener;
    }


    public void onClick(View v) {
        int position = (int) v.getTag();      //getTag()获取数据
        if (mOnItemClickListener != null) {
            switch (v.getId()){
                case R.id.add_cart:
                    mOnItemClickListener.onItemClick(v, ViewName.PRACTISE, position);
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, ViewName.ITEM, position);
                    break;
            }
        }
    }
}

