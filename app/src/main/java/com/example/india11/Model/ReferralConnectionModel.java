package com.example.india11.Model;

public class ReferralConnectionModel {
    String connectionName, connectionMobile, connectionPic, connectionUid;

    public ReferralConnectionModel() {
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getConnectionMobile() {
        return connectionMobile;
    }

    public void setConnectionMobile(String connectionMobile) {
        this.connectionMobile = connectionMobile;
    }

    public String getConnectionPic() {
        return connectionPic;
    }

    public void setConnectionPic(String connectionPic) {
        this.connectionPic = connectionPic;
    }

    public String getConnectionUid() {
        return connectionUid;
    }

    public void setConnectionUid(String connectionUid) {
        this.connectionUid = connectionUid;
    }

    public ReferralConnectionModel(String connectionName, String connectionMobile, String connectionPic, String connectionUid) {
        this.connectionName = connectionName;
        this.connectionMobile = connectionMobile;
        this.connectionPic = connectionPic;
        this.connectionUid = connectionUid;
    }
}
