package com.example.india11.Model;

public class TandCModel {
    String heading, subheading;

    public TandCModel() {
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSubheading() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }

    public TandCModel(String heading, String subheading) {
        this.heading = heading;
        this.subheading = subheading;
    }
}
