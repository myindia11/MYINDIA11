package com.example.india11.Model;

public class UserInfoModel {
    String userName, userMobile, userEmail, enteredReferralCode, userReferralCode, userPassword, uid, joinedOn,profilePic, nickName;
    Integer level;


    public UserInfoModel() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getEnteredReferralCode() {
        return enteredReferralCode;
    }

    public void setEnteredReferralCode(String enteredReferralCode) {
        this.enteredReferralCode = enteredReferralCode;
    }

    public String getUserReferralCode() {
        return userReferralCode;
    }

    public void setUserReferralCode(String userReferralCode) {
        this.userReferralCode = userReferralCode;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getJoinedOn() {
        return joinedOn;
    }

    public void setJoinedOn(String joinedOn) {
        this.joinedOn = joinedOn;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public UserInfoModel(String userName, String userMobile, String userEmail, String enteredReferralCode, String userReferralCode, String userPassword, String uid, String joinedOn, String profilePic, String nickName, Integer level) {
        this.userName = userName;
        this.userMobile = userMobile;
        this.userEmail = userEmail;
        this.enteredReferralCode = enteredReferralCode;
        this.userReferralCode = userReferralCode;
        this.userPassword = userPassword;
        this.uid = uid;
        this.joinedOn = joinedOn;
        this.profilePic = profilePic;
        this.nickName = nickName;
        this.level = level;
    }
}
