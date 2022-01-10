package com.example.india11.Model;

public class TossJoinModel {
    String userId, contestId, subContestId, winningStatus, selectedTeam, actualTeam,paidStatus,userFid;
    Long entryFee;

    public TossJoinModel() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    public String getSubContestId() {
        return subContestId;
    }

    public void setSubContestId(String subContestId) {
        this.subContestId = subContestId;
    }

    public String getWinningStatus() {
        return winningStatus;
    }

    public void setWinningStatus(String winningStatus) {
        this.winningStatus = winningStatus;
    }

    public String getSelectedTeam() {
        return selectedTeam;
    }

    public void setSelectedTeam(String selectedTeam) {
        this.selectedTeam = selectedTeam;
    }

    public String getActualTeam() {
        return actualTeam;
    }

    public void setActualTeam(String actualTeam) {
        this.actualTeam = actualTeam;
    }

    public String getPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(String paidStatus) {
        this.paidStatus = paidStatus;
    }

    public String getUserFid() {
        return userFid;
    }

    public void setUserFid(String userFid) {
        this.userFid = userFid;
    }

    public Long getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(Long entryFee) {
        this.entryFee = entryFee;
    }

    public TossJoinModel(String userId, String contestId, String subContestId, String winningStatus, String selectedTeam, String actualTeam, String paidStatus, String userFid, Long entryFee) {
        this.userId = userId;
        this.contestId = contestId;
        this.subContestId = subContestId;
        this.winningStatus = winningStatus;
        this.selectedTeam = selectedTeam;
        this.actualTeam = actualTeam;
        this.paidStatus = paidStatus;
        this.userFid = userFid;
        this.entryFee = entryFee;
    }
}
