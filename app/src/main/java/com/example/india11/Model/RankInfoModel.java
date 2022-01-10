package com.example.india11.Model;

public class RankInfoModel {
    String rank;
    Long amount;

    public RankInfoModel() {
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public RankInfoModel(String rank, Long amount) {
        this.rank = rank;
        this.amount = amount;
    }
}
