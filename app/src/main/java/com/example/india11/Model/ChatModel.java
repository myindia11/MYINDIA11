package com.example.india11.Model;

import java.util.Map;

public class ChatModel {
    private Map<String, Object> chatTimeStamp;
    String message;
    String from;

    public ChatModel(Map<String, Object> chatTimeStamp, String message, String from) {
        this.chatTimeStamp = chatTimeStamp;
        this.message = message;
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Map<String, Object> getChatTimeStamp() {
        return chatTimeStamp;
    }

    public void setChatTimeStamp(Map<String, Object> chatTimeStamp) {
        this.chatTimeStamp = chatTimeStamp;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


      public ChatModel() {
    }
}
