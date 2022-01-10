package com.example.india11.Model;

public class SubContestsModel {
    String scid, tag, winningPercent, leagueType,totalSpots, leftSpots,totalWinners,multipleLeague;
    Long entryFee, prizePool;
    Integer joined;

    public SubContestsModel() {
    }

    public String getScid() {
        return scid;
    }

    public void setScid(String scid) {
        this.scid = scid;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public String getLeftSpots() {
        return leftSpots;
    }

    public void setLeftSpots(String leftSpots) {
        this.leftSpots = leftSpots;
    }

    public String getTotalWinners() {
        return totalWinners;
    }

    public void setTotalWinners(String totalWinners) {
        this.totalWinners = totalWinners;
    }

    public String getMultipleLeague() {
        return multipleLeague;
    }

    public void setMultipleLeague(String multipleLeague) {
        this.multipleLeague = multipleLeague;
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

    public Integer getJoined() {
        return joined;
    }

    public void setJoined(Integer joined) {
        this.joined = joined;
    }

    public SubContestsModel(String scid, String tag, String winningPercent, String leagueType, String totalSpots, String leftSpots, String totalWinners, String multipleLeague, Long entryFee, Long prizePool, Integer joined) {
        this.scid = scid;
        this.tag = tag;
        this.winningPercent = winningPercent;
        this.leagueType = leagueType;
        this.totalSpots = totalSpots;
        this.leftSpots = leftSpots;
        this.totalWinners = totalWinners;
        this.multipleLeague = multipleLeague;
        this.entryFee = entryFee;
        this.prizePool = prizePool;
        this.joined = joined;
    }
}
