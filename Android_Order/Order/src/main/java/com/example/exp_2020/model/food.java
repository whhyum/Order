package com.example.exp_2020.model;

import android.view.View;

import java.io.Serializable;

public class food implements Serializable {
    private int id;
    private String dishes;
    private float price;
    private String taste;
    private String remark;
    private String path;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    private int num;

    public food(){
        super();
        this.id=id;
        this.dishes = dishes;
        this.price = price;
        this.taste = taste;
        this.remark = remark;
        this.path = path;
    }
    public food(int id, String dishes, String taste, float price, String path) {
        // TODO 自动生成的构造函数存根
        super();
        this.id=id;
        this.dishes = dishes;
        this.price = price;
        this.taste = taste;
        this.path = path;
    }

    public food(View v) {
        super();
        this.id=id;
        this.dishes = dishes;
        this.price = price;
        this.remark = remark;
        this.path = path;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDishes() {
        return dishes;
    }
    public void setDishes(String dishes) {
        this.dishes = dishes;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public String getTaste() {
        return taste;
    }
    public void setTaste(String taste) {
        this.taste = taste;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
}
