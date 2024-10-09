package com.library.models;

import java.time.LocalDateTime;

public class AdminAction {
    private int actionId;
    private int adminId;
    private String actionType;
    //private String actionDescription;
    private LocalDateTime actionDate;
    private Integer targetId;

    public int getActionId() {
        return actionId;
    }
    public void setActionId(int actionId) {
        this.actionId = actionId;
    }
    public int getAdminId() {
        return adminId;
    }
    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
    public String getActionType() {
        return actionType;
    }
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    public LocalDateTime getActionDate() {
        return actionDate;
    }
    public void setActionDate(LocalDateTime actionDate) {
        this.actionDate = actionDate;
    }
    public Integer getTargetId() {
        return targetId;
    }


}