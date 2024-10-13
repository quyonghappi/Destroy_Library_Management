package com.library.dao;

import com.library.config.DatabaseConfig;
import com.library.models.BorrowingRecord;
import com.library.utils.DateFormat;

import javax.print.Doc;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BorrowingRecordDao {

    //delete record
    public void delete(BorrowingRecord borrowingRecord) {
        String sql = "delete from borrowingrecords where record_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, borrowingRecord.getRecordId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //get lent book
    public List<BorrowingRecord> getLent() {
        String sql = "select * from borrowingrecords where status='borrowed'";
        return getRecord(sql);
    }

    //get returned to display on overview scene
    public List<BorrowingRecord> getReturned() {
        String sql= "select * from borrowingrecords where status='returned'";
        return getRecord(sql);
    }

    //get late to display on overview scene
    public List<BorrowingRecord> getLate() {
        String sql = "select * from borrowingrecords where status='late'";
        return getRecord(sql);
    }

    //get lost to display on overview scene
    public List<BorrowingRecord> getLost() {
        String sql = "select * from borrowingrecords where status='Lost'";
        return getRecord(sql);
    }

    private List<BorrowingRecord> getRecord(String sql) {
        List<BorrowingRecord> list = new ArrayList<BorrowingRecord>();
        try (Connection conn= DatabaseConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int recordId = rs.getInt("record_id");
                int userId = rs.getInt("user_id");
                String isbn = rs.getString("isbn");
//                Timestamp borrowDate = rs.getTimestamp("borrow_date");
//                Timestamp returnDate = rs.getTimestamp("return_date");
                Date borrowDate=rs.getDate("borrow_date");
                Date returnDate=rs.getDate("return_date");
                String status = rs.getString("status");
                list.add(new BorrowingRecord(recordId,userId,isbn, DateFormat.toLocalDate(borrowDate), DateFormat.toLocalDate(returnDate), status));
                //list.add(new BorrowingRecord(recordId,userId,isbn, DateFormat.toLocalDate(borrowDate),DateFormat.toLocalDateTime(returnDate),status));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    //use when user want to borrow book, so we add new record
    public void add(BorrowingRecord borrowingRecord) {
        String sql="insert into borrowingrecords(record_id, user_id, isbn, borrow_date,return_date, status) values(?,?,?,?)";
        try(
                Connection conn=DatabaseConfig.getConnection();
                PreparedStatement ps=conn.prepareStatement(sql);
        ) {
            ps.setInt(1, borrowingRecord.getRecordId());
            ps.setInt(2, borrowingRecord.getUserId());
            ps.setString(3, borrowingRecord.getIsbn());
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            ps.setString(6, borrowingRecord.getStatus());
            ps.executeUpdate();
            DocumentDao documentDao = new DocumentDao();
            documentDao.updateQuantity(borrowingRecord.getIsbn(), borrowingRecord.getStatus());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //update when user return book or otherwise
    public void update(BorrowingRecord br) throws SQLException {
        String sql="update borrowingrecords set status=?, return_date=? where record_id=?";
        try(
                Connection conn=DatabaseConfig.getConnection();
                PreparedStatement ps= conn.prepareStatement(sql);
        ) {
            ps.setString(1, br.getStatus());
            ps.setDate(2, DateFormat.toSqlDate(br.getReturnDate()));
            ps.setInt(3, br.getRecordId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void returnDoc(BorrowingRecord br) throws SQLException {
        br.setStatus("returned");
        br.setReturnDate(LocalDate.now());
        update(br);
        DocumentDao documentDao = new DocumentDao();
        documentDao.updateQuantity(br.getIsbn(), br.getStatus());
    }


}
