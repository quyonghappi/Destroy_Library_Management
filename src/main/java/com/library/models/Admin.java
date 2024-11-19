package com.library.models;

import java.time.LocalDateTime;

public class Admin extends User {
    private int adminId;
    //private int userId;
    private LocalDateTime assignedDate;

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public LocalDateTime getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDateTime assignedDate) {
        this.assignedDate = assignedDate;
    }
}
