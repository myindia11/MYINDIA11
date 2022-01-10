package com.example.india11.Model;

public class ComplaintModel {
    String complaintId, complaintDate, message, userName, userEmail, userMobile,userId,status;

    public ComplaintModel() {
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(String complaintDate) {
        this.complaintDate = complaintDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ComplaintModel(String complaintId, String complaintDate, String message, String userName, String userEmail, String userMobile, String userId, String status) {
        this.complaintId = complaintId;
        this.complaintDate = complaintDate;
        this.message = message;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userMobile = userMobile;
        this.userId = userId;
        this.status = status;
    }
}
