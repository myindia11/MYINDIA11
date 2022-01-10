package com.example.india11.PreviewAdapters;

public class BatPreviewModel {
    String teamName, playerName, pid, playerStatus,playerRole,playerPic;
    Double creditScore, obtainedPoints;
    Boolean isSelected;
    String cap, vcap;

    public BatPreviewModel() {
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(String playerStatus) {
        this.playerStatus = playerStatus;
    }

    public String getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(String playerRole) {
        this.playerRole = playerRole;
    }

    public String getPlayerPic() {
        return playerPic;
    }

    public void setPlayerPic(String playerPic) {
        this.playerPic = playerPic;
    }

    public Double getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Double creditScore) {
        this.creditScore = creditScore;
    }

    public Double getObtainedPoints() {
        return obtainedPoints;
    }

    public void setObtainedPoints(Double obtainedPoints) {
        this.obtainedPoints = obtainedPoints;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getVcap() {
        return vcap;
    }

    public void setVcap(String vcap) {
        this.vcap = vcap;
    }

    public BatPreviewModel(String teamName, String playerName, String pid, String playerStatus, String playerRole, String playerPic, Double creditScore, Double obtainedPoints, Boolean isSelected, String cap, String vcap) {
        this.teamName = teamName;
        this.playerName = playerName;
        this.pid = pid;
        this.playerStatus = playerStatus;
        this.playerRole = playerRole;
        this.playerPic = playerPic;
        this.creditScore = creditScore;
        this.obtainedPoints = obtainedPoints;
        this.isSelected = isSelected;
        this.cap = cap;
        this.vcap = vcap;
    }
}
