package com.example.india11.Model;

public class AvailableContestsModel {
    String contestsId, contestsName, leagueType, matchTime, teamOne, teamOneLogo, teamTwo, teamTwoLogo, winningAmount,seriesName,seriesId;
    Integer people;

    public AvailableContestsModel() {
    }

    public String getContestsId() {
        return contestsId;
    }

    public void setContestsId(String contestsId) {
        this.contestsId = contestsId;
    }

    public String getContestsName() {
        return contestsName;
    }

    public void setContestsName(String contestsName) {
        this.contestsName = contestsName;
    }

    public String getLeagueType() {
        return leagueType;
    }

    public void setLeagueType(String leagueType) {
        this.leagueType = leagueType;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public String getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(String teamOne) {
        this.teamOne = teamOne;
    }

    public String getTeamOneLogo() {
        return teamOneLogo;
    }

    public void setTeamOneLogo(String teamOneLogo) {
        this.teamOneLogo = teamOneLogo;
    }

    public String getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(String teamTwo) {
        this.teamTwo = teamTwo;
    }

    public String getTeamTwoLogo() {
        return teamTwoLogo;
    }

    public void setTeamTwoLogo(String teamTwoLogo) {
        this.teamTwoLogo = teamTwoLogo;
    }

    public String getWinningAmount() {
        return winningAmount;
    }

    public void setWinningAmount(String winningAmount) {
        this.winningAmount = winningAmount;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public Integer getPeople() {
        return people;
    }

    public void setPeople(Integer people) {
        this.people = people;
    }

    public AvailableContestsModel(String contestsId, String contestsName, String leagueType, String matchTime, String teamOne, String teamOneLogo, String teamTwo, String teamTwoLogo, String winningAmount, String seriesName, String seriesId, Integer people) {
        this.contestsId = contestsId;
        this.contestsName = contestsName;
        this.leagueType = leagueType;
        this.matchTime = matchTime;
        this.teamOne = teamOne;
        this.teamOneLogo = teamOneLogo;
        this.teamTwo = teamTwo;
        this.teamTwoLogo = teamTwoLogo;
        this.winningAmount = winningAmount;
        this.seriesName = seriesName;
        this.seriesId = seriesId;
        this.people = people;
    }
}
