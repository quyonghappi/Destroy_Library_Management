package com.library.dao;

import com.library.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminDao {

    //record an admin action into the AdminActions table
    public void recordAdminAction(int adminId, String actionType, String description) {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = DatabaseConfig.getConnection();
            String sql = "INSERT INTO AdminActions (admin_id, action_type,action_date) " +
                    "VALUES (?, ?, CURRENT_TIMESTAMP)";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, adminId);
            ps.setString(2, actionType);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //them sach
    //public void addBook()
    //xoa sach
    //cho muon sach
    //nhan sach tra
    //them nguoi dung
    //xoa nguoi dung
    //thu tien phat

}
