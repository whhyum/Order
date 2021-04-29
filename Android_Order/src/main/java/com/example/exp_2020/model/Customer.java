package com.example.exp_2020.model;

public class Customer {
    private int id;
    private String userName;
    private String password;

    public  Customer() {  //直接new无参会报错
        super();
    }

    public Customer(String userName, String password) {
        super();
        this.userName = userName;
        this.password = password;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
