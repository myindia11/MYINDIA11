package com.example.india11.Model;

public class JoinedTossModel {
    String tossId, selectedTeam;
    Long entry, prize;

    public JoinedTossModel() {
    }

    public String getTossId() {
        return tossId;
    }

    public void setTossId(String tossId) {
        this.tossId = tossId;
    }

    public String getSelectedTeam() {
        return selectedTeam;
    }

    public void setSelectedTeam(String selectedTeam) {
        this.selectedTeam = selectedTeam;
    }

    public Long getEntry() {
        return entry;
    }

    public void setEntry(Long entry) {
        this.entry = entry;
    }

    public Long getPrize() {
        return prize;
    }

    public void setPrize(Long prize) {
        this.prize = prize;
    }

    public JoinedTossModel(String tossId, String selectedTeam, Long entry, Long prize) {
        this.tossId = tossId;
        this.selectedTeam = selectedTeam;
        this.entry = entry;
        this.prize = prize;
    }
}
