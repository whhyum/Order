package com.example.exp_2020.model;

public class Master {
    private int id;
    private String userName;
    private String password;
    private String email;

    public  Master() {  //直接new无参会报错
        super();
    }

    public Master(String userName, String password) {
        super();
        this.userName = userName;
        this.password = password;
    }
    public Master(String userName, String password,String email) {
        super();
        this.userName = userName;
        this.password = password;
        this.email=email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

