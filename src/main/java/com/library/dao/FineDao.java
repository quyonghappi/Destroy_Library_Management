package com.library.dao;
import com.library.config.DatabaseConfig;
import com.library.models.BorrowingRecord;
import com.library.models.Fine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FineDao {

    public List<Fine> getFines() {
        String sql = "select * from Fines";
        List<Fine> fineList= new ArrayList<>();

        try(Connection conn = DatabaseConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                Fine fine = new Fine();
                fine.setFineId(rs.getInt(1));
                fine.setUserId(rs.getInt(2));
                fine.setRecordId(rs.getInt(3));
                fine.setFineAmount(rs.getBigDecimal(4));
                fine.setDueDate(rs.getDate(5).toLocalDate());
                fine.setStatus(rs.getString(6));
                fineList.add(fine);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return fineList;
    }

    //get fine details
    public List<Fine> getFineDetailsWithRecordId() {
        String sql = "SELECT f.user_id, f.record_id, f.fine_amount,f.due_date, f.status, br.isbn " +
                "FROM Fines f JOIN BorrowingRecords br ON f.record_id = br.record_id";
        List<Fine> fineDetails=new ArrayList<>();

        try(Connection conn=DatabaseConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                Fine fine = new Fine();
                fine.setUserId(rs.getInt("user_id"));
                fine.setRecordId(rs.getInt("record_id"));
                fine.setDueDate(rs.getDate("due_date").toLocalDate());
                fine.setStatus(rs.getString("status"));
                fine.setFineAmount(rs.getBigDecimal("fine_amount"));
                fineDetails.add(fine);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return fineDetails;
    }

    //if it was paid
    public void deleteFine(Fine fine) {
        String sql = "delete from fines where fine_id=?";
        try(Connection conn = DatabaseConfig.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, fine.getFineId());
            pstm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkAndAddFine(BorrowingRecord br) {
        LocalDateTime dueDate=br.getReturnDate();
        boolean getFine=br.getStatus().equals("lost");
        if (isOverdue(dueDate)||getFine) {
            addFine(br);
        }


    }

    public void addFine(BorrowingRecord record) {
        String sql="insert into fines(user_id, record_id, fine_amount, due_date, status) values(?,?,?,?,?)";

        try(
                Connection conn=DatabaseConfig.getConnection();
                PreparedStatement ps=conn.prepareStatement(sql);
                ) {
            ps.setInt(1,record.getUserId());
            ps.setInt(2,record.getRecordId());
            ps.setDouble(3,calculateFine(record));
            ps.setObject(4, LocalDate.now());
            ps.setString(5, "UNPAID");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public double calculateFine(BorrowingRecord record) {
        //10k each day overdue
        if(isOverdue(record.getReturnDate())) {
            //only used for local date
            //Period period=Period.between(LocalDateTime.now(), record.getReturnDate());
            Duration duration=Duration.between(record.getReturnDate(),LocalDate.now());
            long days=duration.toDays();
            return days*10000;
        }
        else if (record.getStatus().equals("lost")) {
            return 5000000;
        }
        return 0;
    }

    public boolean isOverdue(LocalDateTime dueDate) {
        return LocalDateTime.now().isAfter(dueDate);
    }

}
