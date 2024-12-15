package com.library.dao;

import com.library.config.DatabaseConfig;
import com.library.models.User;
import com.library.utils.DateFormat;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class UserDao implements DAO<User> {
    //get user by id
    public <U> User get(U id) {
        User user = null;
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, (int)id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setUsername(rs.getString("user_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password_hash"));
                user.setUserRole(rs.getString("user_role"));
                user.setAccountStatus(rs.getString("account_status"));

                // Use DateFormat to convert SQL Timestamp to LocalDateTime
                user.setJoinDate(DateFormat.toLocalDate(rs.getDate("join_date")));
                user.setLastLoginDate(DateFormat.toLocalDate(rs.getDate("last_login")));
                user.setActive(rs.getBoolean("account_locked"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseConfig.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setUsername(rs.getString("user_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password_hash"));
                user.setUserRole(rs.getString("user_role"));
                user.setAccountStatus(rs.getString("account_status"));

                user.setJoinDate(DateFormat.toLocalDate(rs.getDate("join_date")));

                user.setLastLoginDate(DateFormat.toLocalDate(rs.getDate("last_login")));
                user.setActive(rs.getBoolean("account_locked"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void updatePassword(User user) {
        String sql = "UPDATE users SET user_name = ?, password_hash = ? WHERE user_id = ?";

        String newPasswordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        try (Connection conn = DatabaseConfig.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, newPasswordHash.trim());
            stmt.setInt(3, user.getUserId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateAccount(User user) {
        String sql = "UPDATE users SET account_status = ?, account_locked = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getAccountStatus());
            stmt.setBoolean(2, !user.getAccountStatus().equals("active"));
            stmt.setInt(3, user.getUserId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateLastLogin(String username) {
        String sql = "UPDATE users SET last_login=? WHERE user_name = ?";
        try (Connection conn = DatabaseConfig.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, DateFormat.toSqlDate(LocalDate.now()));

            //stmt.setInt(8, user.getLoginAttempts());
            stmt.setString(2, username);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(User user) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to create a new user with hashed password
    public <U> void add(U u) {
        if(!(u instanceof User user)) {
            throw new IllegalArgumentException("User is not an instance of User");
        }
        String insert = "INSERT INTO users (full_name, user_name, email, password_hash, user_role, account_status, join_date, last_login, account_locked) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(insert)
        ) {
            //hash password using BCrypt
            String hashedPass = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            // Set values in SQL
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, hashedPass);
            ps.setString(5, user.getUserRole());
            ps.setString(6, user.getAccountStatus());
            ps.setDate(7, DateFormat.toSqlDate(user.getJoinDate()));
            ps.setDate(8, DateFormat.toSqlDate(user.getLastLoginDate()) );
            ps.setBoolean(9, !user.isActive());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Find a user by username for login check
    public static User findUserByName(String username) {
        String sql = "SELECT * FROM users WHERE user_name = ?";
        try (
                Connection cn = DatabaseConfig.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("user_name"));
                user.setUserRole(rs.getString("user_role"));
                user.setAccountStatus(rs.getString("account_status"));
                user.setPassword(rs.getString("password_hash"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setJoinDate(DateFormat.toLocalDate(rs.getDate("join_date")));
                user.setLastLoginDate(DateFormat.toLocalDate(rs.getDate("last_login")));
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("This username doesn't exist, cannot find user by name!" + e.getMessage());
        }
        return null;
    }

    //method to search user with result show dynamically
    public static List<User> searchByEmail(String email) throws SQLException {
        List<User> result = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE email like ?";
        try (
                Connection cn = DatabaseConfig.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {
            ps.setString(1, "%"+email+"%");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("user_name"));
                user.setUserRole(rs.getString("user_role"));
                user.setAccountStatus(rs.getString("account_status"));
                user.setPassword(rs.getString("password_hash"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setJoinDate(DateFormat.toLocalDate(rs.getDate("join_date")));
                user.setLastLoginDate(DateFormat.toLocalDate(rs.getDate("last_login")));
                result.add(user);
            }
        } catch (SQLException e) {
            throw new SQLException("This username doesn't exist!" + e.getMessage());
        }
        return result;
    }



    /**
     * get a list of all users having fines
     * @return list of users having fines
     */
    public static List<User> getUserHasFine() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT DISTINCT u.user_id, u.full_name, u.user_name, u.email, u.password_hash, \n" +
                "                        u.user_role, u.account_status, u.join_date, u.last_login, u.account_locked from users u\n"
                +"join fines f on u.user_id = f.user_id\n";
        try (Connection conn = DatabaseConfig.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setUsername(rs.getString("user_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password_hash"));
                user.setUserRole(rs.getString("user_role"));
                user.setAccountStatus(rs.getString("account_status"));
                user.setJoinDate(DateFormat.toLocalDate(rs.getDate("join_date")));
                user.setLastLoginDate(DateFormat.toLocalDate(rs.getDate("last_login")));
                user.setActive(rs.getBoolean("account_locked"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    //authenticate user using username and password
    public static boolean authenticateAdmin(String username, String password) throws Exception {
        User user = findUserByName(username);
        if (user == null) return false;
        return BCrypt.checkpw(password, user.getPassword()) && user.getUserRole().equals("admin");
    }

    public static boolean authenticateUser(String username, String password) throws Exception {
        User user = findUserByName(username);
        if (user == null) return false;
        return BCrypt.checkpw(password, user.getPassword()) && user.getUserRole().equals("reader");
    }

}
