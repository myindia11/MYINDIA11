package com.example.india11.Model;

public class PlayersListModel {
    String teamName, playerName, pid, playerStatus,playerPic;
    Double creditScore, obtainedPoints;
    Integer selectedBy;

    public PlayersListModel() {
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

    public Integer getSelectedBy() {
        return selectedBy;
    }

    public void setSelectedBy(Integer selectedBy) {
        this.selectedBy = selectedBy;
    }

    public PlayersListModel(String teamName, String playerName, String pid, String playerStatus, String playerPic, Double creditScore, Double obtainedPoints, Integer selectedBy) {
        this.teamName = teamName;
        this.playerName = playerName;
        this.pid = pid;
        this.playerStatus = playerStatus;
        this.playerPic = playerPic;
        this.creditScore = creditScore;
        this.obtainedPoints = obtainedPoints;
        this.selectedBy = selectedBy;
    }
}
