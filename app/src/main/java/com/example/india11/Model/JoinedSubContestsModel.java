package com.example.india11.Model;

public class JoinedSubContestsModel {
    String scid, winningPercent, leagueType,totalSpots, tId,cId;
    Long entryFee, prizePool;

    public JoinedSubContestsModel() {
    }

    public String getScid() {
        return scid;
    }

    public void setScid(String scid) {
        this.scid = scid;
    }

    public String getWinningPercent() {
        return winningPercent;
    }

    public void setWinningPercent(String winningPercent) {
        this.winningPercent = winningPercent;
    }

    public String getLeagueType() {
        return leagueType;
    }

    public void setLeagueType(String leagueType) {
        this.leagueType = leagueType;
    }

    public String getTotalSpots() {
        return totalSpots;
    }

    public void setTotalSpots(String totalSpots) {
        this.totalSpots = totalSpots;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public Long getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(Long entryFee) {
        this.entryFee = entryFee;
    }

    public Long getPrizePool() {
        return prizePool;
    }

    public void setPrizePool(Long prizePool) {
        this.prizePool = prizePool;
    }

    public JoinedSubContestsModel(String scid, String winningPercent, String leagueType, String totalSpots, String tId, String cId, Long entryFee, Long prizePool) {
        this.scid = scid;
        this.winningPercent = winningPercent;
        this.leagueType = leagueType;
        this.totalSpots = totalSpots;
        this.tId = tId;
        this.cId = cId;
        this.entryFee = entryFee;
        this.prizePool = prizePool;
    }
}
