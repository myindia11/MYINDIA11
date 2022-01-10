package com.example.india11.Model;

public class NotificationModel {
    String heading, body, date;

    public NotificationModel() {
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public NotificationModel(String heading, String body, String date) {
        this.heading = heading;
        this.body = body;
        this.date = date;
    }
}
