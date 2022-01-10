package com.example.india11.Model;

public class TossSubContestModel {
    String tossId,totalWinners;
    Integer joined, totalSpots;
    Long entryFee, prizePool;

    public TossSubContestModel() {
    }

    public String getTossId() {
        return tossId;
    }

    public void setTossId(String tossId) {
        this.tossId = tossId;
    }

    public String getTotalWinners() {
        return totalWinners;
    }

    public void setTotalWinners(String totalWinners) {
        this.totalWinners = totalWinners;
    }

    public Integer getJoined() {
        return joined;
    }

    public void setJoined(Integer joined) {
        this.joined = joined;
    }

    public Integer getTotalSpots() {
        return totalSpots;
    }

    public void setTotalSpots(Integer totalSpots) {
        this.totalSpots = totalSpots;
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

    public TossSubContestModel(String tossId, String totalWinners, Integer joined, Integer totalSpots, Long entryFee, Long prizePool) {
        this.tossId = tossId;
        this.totalWinners = totalWinners;
        this.joined = joined;
        this.totalSpots = totalSpots;
        this.entryFee = entryFee;
        this.prizePool = prizePool;
    }
}
