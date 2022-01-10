package com.example.india11.Model;

public class UserStatsModel {
    Integer played, won;
    Double invested, earned;

    public UserStatsModel() {
    }

    public Integer getPlayed() {
        return played;
    }

    public void setPlayed(Integer played) {
        this.played = played;
    }

    public Integer getWon() {
        return won;
    }

    public void setWon(Integer won) {
        this.won = won;
    }

    public Double getInvested() {
        return invested;
    }

    public void setInvested(Double invested) {
        this.invested = invested;
    }

    public Double getEarned() {
        return earned;
    }

    public void setEarned(Double earned) {
        this.earned = earned;
    }

    public UserStatsModel(Integer played, Integer won, Double invested, Double earned) {
        this.played = played;
        this.won = won;
        this.invested = invested;
        this.earned = earned;
    }
}
