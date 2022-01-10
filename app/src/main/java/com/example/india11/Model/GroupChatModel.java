package com.example.india11.Model;

public class GroupChatModel {
    String sender, time, message;

    public GroupChatModel() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GroupChatModel(String sender, String time, String message) {
        this.sender = sender;
        this.time = time;
        this.message = message;
    }
}
