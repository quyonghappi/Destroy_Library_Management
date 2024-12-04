package com.library.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int userId;
    private String fullName;
    private String username;
    private String password;
    private String email;
    //private String phone;
    //private String address;
    private String userRole="reader";
    private String accountStatus = "active";
    private LocalDate joinDate=LocalDate.now();
    private LocalDate lastLoginDate=LocalDate.now();
    private List<String> borrowedDocuments = new ArrayList<>();
    private boolean active = true;//check if account is locked

    public User() {
    }
    public User(int userId, String fullName, String name, String email, String passwordHash, String userRole, String accountStatus, LocalDate joinDate) {
        this.userId = userId;
        this.username = name;
        this.email = email;
        this.password = passwordHash;
        //this.phone = phone;
        this.userRole = userRole;
        this.accountStatus = accountStatus;
        this.joinDate = joinDate;
    }

    public User(String fullName, String name, String email, String passwordHash) {
        this.fullName = fullName;
        this.username = name;
        this.email = email;
        this.password = passwordHash;
    }

    public LocalDate getLastLoginDate() {
        return lastLoginDate;
    }
    public void setLastLoginDate(LocalDate lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    //    public String getPhone() {
//        return phone;
//    }
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
    public String getUserRole() {
        return userRole;
    }
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
    public String getAccountStatus() {
        return accountStatus;
    }
    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
    public LocalDate getJoinDate() {
        return joinDate;
    }
    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", name=" + username + "]";
    }
}
