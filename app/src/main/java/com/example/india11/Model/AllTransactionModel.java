package com.example.india11.Model;

public class AllTransactionModel {
    String tid, userName, userMobile, userEmail, amount, time;

    public AllTransactionModel() {
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public AllTransactionModel(String tid, String userName, String userMobile, String userEmail, String amount, String time) {
        this.tid = tid;
        this.userName = userName;
        this.userMobile = userMobile;
        this.userEmail = userEmail;
        this.amount = amount;
        this.time = time;
    }
}
