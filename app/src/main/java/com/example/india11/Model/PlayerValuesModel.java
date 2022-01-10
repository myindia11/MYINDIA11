package com.example.india11.Model;

public class PlayerValuesModel {
    String teamCode;
    Double leftCredit;
    Integer WK,BT,AR,BR,teamOneCount,teamTwoCount;
    String teamOne, teamTwo, captain, viceCaptain;

    public PlayerValuesModel() {
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public Double getLeftCredit() {
        return leftCredit;
    }

    public void setLeftCredit(Double leftCredit) {
        this.leftCredit = leftCredit;
    }

    public Integer getWK() {
        return WK;
    }

    public void setWK(Integer WK) {
        this.WK = WK;
    }

    public Integer getBT() {
        return BT;
    }

    public void setBT(Integer BT) {
        this.BT = BT;
    }

    public Integer getAR() {
        return AR;
    }

    public void setAR(Integer AR) {
        this.AR = AR;
    }

    public Integer getBR() {
        return BR;
    }

    public void setBR(Integer BR) {
        this.BR = BR;
    }

    public Integer getTeamOneCount() {
        return teamOneCount;
    }

    public void setTeamOneCount(Integer teamOneCount) {
        this.teamOneCount = teamOneCount;
    }

    public Integer getTeamTwoCount() {
        return teamTwoCount;
    }

    public void setTeamTwoCount(Integer teamTwoCount) {
        this.teamTwoCount = teamTwoCount;
    }

    public String getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(String teamOne) {
        this.teamOne = teamOne;
    }

    public String getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(String teamTwo) {
        this.teamTwo = teamTwo;
    }

    public String getCaptain() {
        return captain;
    }

    public void setCaptain(String captain) {
        this.captain = captain;
    }

    public String getViceCaptain() {
        return viceCaptain;
    }

    public void setViceCaptain(String viceCaptain) {
        this.viceCaptain = viceCaptain;
    }

    public PlayerValuesModel(String teamCode, Double leftCredit, Integer WK, Integer BT, Integer AR, Integer BR, Integer teamOneCount, Integer teamTwoCount, String teamOne, String teamTwo, String captain, String viceCaptain) {
        this.teamCode = teamCode;
        this.leftCredit = leftCredit;
        this.WK = WK;
        this.BT = BT;
        this.AR = AR;
        this.BR = BR;
        this.teamOneCount = teamOneCount;
        this.teamTwoCount = teamTwoCount;
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        this.captain = captain;
        this.viceCaptain = viceCaptain;
    }
}
