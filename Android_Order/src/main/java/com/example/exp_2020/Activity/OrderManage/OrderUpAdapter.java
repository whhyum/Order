package com.example.exp_2020.Activity.OrderManage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exp_2020.R;
import com.example.exp_2020.model.Order;
import com.example.exp_2020.model.food;

import java.util.List;

public class OrderUpAdapter  extends RecyclerView.Adapter<OrderUpAdapter.ViewHolder> {
    List<Order> data ;//保存数据的成员对象
    private Context context;//上下文
    private View.OnClickListener listener;
    @NonNull
    @Override
    public OrderUpAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.order_up_item, parent, false);
        OrderUpAdapter.ViewHolder vh = new OrderUpAdapter.ViewHolder(view);
        view.setOnClickListener(listener);
        return vh;//生成ViewHolder对象
    }


    public OrderUpAdapter(List<Order> data, View.OnClickListener listener) {//构造函数，保存数据
        this.data = data;
        this.listener=listener;
    }



    @Override
    public void onBindViewHolder(@NonNull OrderUpAdapter.ViewHolder holder, int position) {
        Order o = data.get(position);
        holder.id.setText(o.getFoodId()+"");//略，其中根据图片文件名，加载到 ImageView 中
        holder.foodname.setText(o.getFoodName()+"");
        holder.price.setText(o.getFoodPrice()+"");
        holder.num.setText(o.getFoodNum()+"");
        holder.itemView.setTag(position);
        holder.itemView.setTag(o);//附加数据
        /*获取图片资源*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {//保存行布局上的每个组件的对象引用
        TextView id;
        TextView foodname;
        TextView price;
        TextView num;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.id_o);
            num=itemView.findViewById(R.id.num_o);
            foodname=itemView.findViewById(R.id.foodname_o);
            price=itemView.findViewById(R.id.price_o);

        }
    }
}
