package com.library.dao;

import com.library.controller.start.Check;
import com.library.models.User;

import java.time.LocalDate;

public class insertAdmin {
    private static UserDao userDao = new UserDao();
    public static void main(String[] args) {
        try {
            String fullName = "Hoang Duong";
            String username = "duongg";
            String email = "duongnn246@gmail.com";
            String password = "123456";
            User user = new User(fullName, username, email, password);
            user.setUserRole("Admin");
            user.setJoinDate(LocalDate.now());
            user.setAccountStatus("Active");
            user.setActive(true);
            if (userDao.findUserByName(username) != null
                    || !Check.isValidUsername(username)
                    || !Check.isValidEmail(email)
                    || !Check.isValidFullName(fullName)) {
                System.out.println("User already exists. Please choose a different username.");
                return;
            }

            try {
                userDao.add(user);
                System.out.println("User created successfully!");
            } catch (Exception e) {
                System.err.println("Error creating user: " + e.getMessage());
                e.printStackTrace(); // Optional: Print stack trace for debugging
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
