package com.library.dao;

import com.library.models.User;

public class haha {
    private static UserDao userDao = new UserDao();
    public static void main(String[] args) {
        try {
            String fullName = "Nguyen Thuy Linh";
            String username = "Thuy Linh Nguyen";
            String email = "23021610@vnu.edu.vn";
            String password = "123456";
            User user = new User(fullName, username, email, password);
            user.setUserRole("Admin");
            if (userDao.findUserByName(username) != null) {
                System.out.println("User already exists. Please choose a different username.");
                return;
            }

            try {
                userDao.createUser(user);
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
