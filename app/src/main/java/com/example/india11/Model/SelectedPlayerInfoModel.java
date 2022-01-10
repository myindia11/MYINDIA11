package com.example.india11.Model;

public class SelectedPlayerInfoModel {
    String Captain,ViceCaptain,team,updateStatus;

    public SelectedPlayerInfoModel() {
    }

    public String getCaptain() {
        return Captain;
    }

    public void setCaptain(String captain) {
        Captain = captain;
    }

    public String getViceCaptain() {
        return ViceCaptain;
    }

    public void setViceCaptain(String viceCaptain) {
        ViceCaptain = viceCaptain;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public SelectedPlayerInfoModel(String captain, String viceCaptain, String team, String updateStatus) {
        Captain = captain;
        ViceCaptain = viceCaptain;
        this.team = team;
        this.updateStatus = updateStatus;
    }
}
