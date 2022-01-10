package com.example.india11.Model;

public class WithdrawlRequestModel {
    String accountHolderName, accountNumber, bankName, ifsCode, amountToWithdraw, referenceId, dateOfRequest, status, mobileNo, emailId;

    public WithdrawlRequestModel() {
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIfsCode() {
        return ifsCode;
    }

    public void setIfsCode(String ifsCode) {
        this.ifsCode = ifsCode;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public WithdrawlRequestModel(String accountHolderName, String accountNumber, String bankName, String ifsCode, String amountToWithdraw, String referenceId, String dateOfRequest, String status, String mobileNo, String emailId) {
        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.ifsCode = ifsCode;
        this.amountToWithdraw = amountToWithdraw;
        this.referenceId = referenceId;
        this.dateOfRequest = dateOfRequest;
        this.status = status;
        this.mobileNo = mobileNo;
        this.emailId = emailId;
    }
}
