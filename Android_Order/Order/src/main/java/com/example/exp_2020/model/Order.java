package com.example.exp_2020.model;

public class Order {
        private String id;
        private String orderId;
        private float foodTotalPrice;
        private int foodId;
        private float foodPrice;
        private int foodNum;
        private String foodName;
        //订单状态 0 未处理（可以取消）1 烹饪中 2 配送中 3 已送达 4待付款 5已取消 6已完成
        private int orderStatus;
        private int orderNum;
        private float orderTotalMoney;
        private String userName;
        private String address;
        private String phone;

        public Order() {
            super();
            // TODO Auto-generated constructor stub
        }
        public Order(String id, String orderId, int orderStatus,
                     float orderTotalMoney, String userName, String addres) {
            super();
            this.id=id;
            this.orderId = orderId;
            this.orderStatus = orderStatus;
            this.orderTotalMoney = orderTotalMoney;
            this.userName = userName;
            this.address=address;
        }
        public Order(String id, String orderId, int orderStatus,
                     float orderTotalMoney, String userName, String address, String phone) {
            super();
            this.id=id;
            this.orderId = orderId;
            this.orderStatus = orderStatus;
            this.orderTotalMoney = orderTotalMoney;
            this.userName = userName;
            this.address=address;
            this.phone=phone;
        }
        public Order(String orderId, float foodTotalPrice, int foodId,
                     float foodPrice, int foodNum, String foodName) {
            super();
            this.orderId = orderId;
            this.foodTotalPrice = foodTotalPrice;
            this.foodId = foodId;
            this.foodPrice = foodPrice;
            this.foodNum = foodNum;
            this.foodName = foodName;

        }

        public Order(String id) {
            // TODO 自动生成的构造函数存根
            super();
            this.id = id;
        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getOrderId() {
            return orderId;
        }
        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
        public float getFoodTotalPrice() {
            return foodTotalPrice;
        }
        public void setFoodTotalPrice(float foodTotalPrice) {
            this.foodTotalPrice = foodTotalPrice;
        }
        public int getFoodId() {
            return foodId;
        }
        public void setFoodId(int foodId) {
            this.foodId = foodId;
        }
        public float getFoodPrice() {
            return foodPrice;
        }
        public void setFoodPrice(float foodPrice) {
            this.foodPrice = foodPrice;
        }
        public int getFoodNum() {
            return foodNum;
        }
        public void setFoodNum(int foodNum) {
            this.foodNum = foodNum;
        }
        public String getFoodName() {
            return foodName;
        }
        public void setFoodName(String foodName) {
            this.foodName = foodName;
        }
        public int getOrderStatus() {
            return orderStatus;
        }
        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }
        public int getOrderNum() {
            return orderNum;
        }
        public void setOrderNum(int orderNum) {
            this.orderNum = orderNum;
        }
        public float getOrderTotalMoney() {
            return orderTotalMoney;
        }
        public void setOrderTotalMoney(float orderTotalMoney) {
            this.orderTotalMoney = orderTotalMoney;
        }
        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }
        public String getAddress() {
            return address;
        }
        public void setAddress(String address) {
            this.address = address;
        }
        public String getPhone() {
            return phone;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }


}
