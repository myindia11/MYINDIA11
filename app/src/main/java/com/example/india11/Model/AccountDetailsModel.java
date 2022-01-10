package com.example.india11.Model;

public class AccountDetailsModel {
    String holderName, bankName, accountNumber, ifsCode;

    public AccountDetailsModel() {
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfsCode() {
        return ifsCode;
    }

    public void setIfsCode(String ifsCode) {
        this.ifsCode = ifsCode;
    }

    public AccountDetailsModel(String holderName, String bankName, String accountNumber, String ifsCode) {
        this.holderName = holderName;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.ifsCode = ifsCode;
    }
}
