package com.example.india11.Model;

public class WalletModel {
    String amountToWithdraw, referenceId, dateOfRequest,transactionType;

    public WalletModel() {
    }

    public String getAmountToWithdraw() {
        return amountToWithdraw;
    }

    public void setAmountToWithdraw(String amountToWithdraw) {
        this.amountToWithdraw = amountToWithdraw;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getDateOfRequest() {
        return dateOfRequest;
    }

    public void setDateOfRequest(String dateOfRequest) {
        this.dateOfRequest = dateOfRequest;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public WalletModel(String amountToWithdraw, String referenceId, String dateOfRequest, String transactionType) {
        this.amountToWithdraw = amountToWithdraw;
        this.referenceId = referenceId;
        this.dateOfRequest = dateOfRequest;
        this.transactionType = transactionType;
    }
}
