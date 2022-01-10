package com.example.india11.Model;

public class GroupModel {
    String groupId, groupName, groupDescription, groupPic,groupCreatedTime, groupAdmin,groupStatus;
    Integer groupSize;

    public GroupModel() {
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupPic() {
        return groupPic;
    }

    public void setGroupPic(String groupPic) {
        this.groupPic = groupPic;
    }

    public String getGroupCreatedTime() {
        return groupCreatedTime;
    }

    public void setGroupCreatedTime(String groupCreatedTime) {
        this.groupCreatedTime = groupCreatedTime;
    }

    public String getGroupAdmin() {
        return groupAdmin;
    }

    public void setGroupAdmin(String groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }

    public Integer getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
    }

    public GroupModel(String groupId, String groupName, String groupDescription, String groupPic, String groupCreatedTime, String groupAdmin, String groupStatus, Integer groupSize) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupPic = groupPic;
        this.groupCreatedTime = groupCreatedTime;
        this.groupAdmin = groupAdmin;
        this.groupStatus = groupStatus;
        this.groupSize = groupSize;
    }
}
