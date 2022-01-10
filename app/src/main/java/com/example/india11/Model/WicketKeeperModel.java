package com.example.india11.Model;

public class WicketKeeperModel {
    String teamName, playerName, pid, playerStatus,playerRole;
    Double creditScore, obtainedPoints;
    Boolean isSelected;

    public WicketKeeperModel() {
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

    public WicketKeeperModel(String teamName, String playerName, String pid, String playerStatus, String playerRole, Double creditScore, Double obtainedPoints, Boolean isSelected) {
        this.teamName = teamName;
        this.playerName = playerName;
        this.pid = pid;
        this.playerStatus = playerStatus;
        this.playerRole = playerRole;
        this.creditScore = creditScore;
        this.obtainedPoints = obtainedPoints;
        this.isSelected = isSelected;
    }
}
