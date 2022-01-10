package com.example.india11.Model;

public class InitialCreateTeamModel {
    String teamCode;
    Double leftCredit;
    Integer WK,BT,AR,BR;

    public InitialCreateTeamModel() {
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

    public InitialCreateTeamModel(String teamCode, Double leftCredit, Integer WK, Integer BT, Integer AR, Integer BR) {
        this.teamCode = teamCode;
        this.leftCredit = leftCredit;
        this.WK = WK;
        this.BT = BT;
        this.AR = AR;
        this.BR = BR;
    }
}
