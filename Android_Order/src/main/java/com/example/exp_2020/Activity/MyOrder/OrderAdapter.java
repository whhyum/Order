package com.example.exp_2020.Activity.MyOrder;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exp_2020.R;
import com.example.exp_2020.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private View.OnClickListener listener;
    private List<Order> data =new ArrayList<>();
    private Context context;//上下文



    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
//        ViewHolder vh = new ViewHolder(view);
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.myorder_item, parent, false);
        OrderAdapter.ViewHolder vh = new OrderAdapter.ViewHolder(view);
        view.setOnClickListener(listener);
        return vh;
    }
    public OrderAdapter(List<Order> data, View.OnClickListener listener) {//构造函数，保存数据
        this.data = data;
        this.listener=listener;
    }

    public void setData(List<Order> data) {
        this.data = data;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        Order order=data.get(position);
        holder.orderId.setText(order.getOrderId()+"");
        String status;
        switch (order.getOrderStatus()){
            case 0:
                status="未处理";
                break;
            case 1:
                status="烹饪中";
                break;
            case 2:
                status="配送中";
                break;
            case 3:
                status="已送达";
                break;
            case 4:
                status="待付款";
                break;
            case 5:
                status="已取消";
                break;
            case 6:
                status="已完成";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + order.getOrderStatus());
        }
        holder.orderStatus.setText(status);
        
        holder.orderPrice.setText(order.getFoodTotalPrice()+"");
        Log.w("订单",""+order.getOrderId()+"");
        holder.userName.setText(order.getUserName()+"");
        holder.itemView.setTag(position);
        holder.itemView.setTag(order);//附加数据
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {//保存行布局上的每个组件的对象引用
        TextView orderId;
        TextView orderStatus;
        TextView orderPrice;
        TextView userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderidtext);
            orderStatus = itemView.findViewById(R.id.orderstatustext);
            orderPrice= itemView.findViewById(R.id.orderpricetext);
            userName= itemView.findViewById(R.id.userNametext);

        }

    }
}
