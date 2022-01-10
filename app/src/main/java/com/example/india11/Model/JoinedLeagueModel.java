package com.example.india11.Model;

public class JoinedLeagueModel {
    String UID, FID, CID, SCID, TID,creationId, paid;
    Integer rank;
    Double obtainedPoints;

    public JoinedLeagueModel() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getFID() {
        return FID;
    }

    public void setFID(String FID) {
        this.FID = FID;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getSCID() {
        return SCID;
    }

    public void setSCID(String SCID) {
        this.SCID = SCID;
    }

    public String getTID() {
        return TID;
    }

    public void setTID(String TID) {
        this.TID = TID;
    }

    public String getCreationId() {
        return creationId;
    }

    public void setCreationId(String creationId) {
        this.creationId = creationId;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Double getObtainedPoints() {
        return obtainedPoints;
    }

    public void setObtainedPoints(Double obtainedPoints) {
        this.obtainedPoints = obtainedPoints;
    }

    public JoinedLeagueModel(String UID, String FID, String CID, String SCID, String TID, String creationId, String paid, Integer rank, Double obtainedPoints) {
        this.UID = UID;
        this.FID = FID;
        this.CID = CID;
        this.SCID = SCID;
        this.TID = TID;
        this.creationId = creationId;
        this.paid = paid;
        this.rank = rank;
        this.obtainedPoints = obtainedPoints;
    }
}
