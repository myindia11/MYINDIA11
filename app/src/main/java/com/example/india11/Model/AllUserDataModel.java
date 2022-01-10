package com.example.india11.Model;

public class AllUserDataModel {
    String userApic, userAfid, userAname, userAemail;

    public AllUserDataModel() {
    }

    public String getUserApic() {
        return userApic;
    }

    public void setUserApic(String userApic) {
        this.userApic = userApic;
    }

    public String getUserAfid() {
        return userAfid;
    }

    public void setUserAfid(String userAfid) {
        this.userAfid = userAfid;
    }

    public String getUserAname() {
        return userAname;
    }

    public void setUserAname(String userAname) {
        this.userAname = userAname;
    }

    public String getUserAemail() {
        return userAemail;
    }

    public void setUserAemail(String userAemail) {
        this.userAemail = userAemail;
    }

    public AllUserDataModel(String userApic, String userAfid, String userAname, String userAemail) {
        this.userApic = userApic;
        this.userAfid = userAfid;
        this.userAname = userAname;
        this.userAemail = userAemail;
    }
}
