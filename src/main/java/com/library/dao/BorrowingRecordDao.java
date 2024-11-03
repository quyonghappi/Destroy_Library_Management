package com.library.dao;

import com.library.config.DatabaseConfig;
import com.library.models.BorrowingRecord;
import com.library.models.Fine;
import com.library.utils.DateFormat;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BorrowingRecordDao implements DAO<BorrowingRecord> {

    public void delete(BorrowingRecord borrowingRecord) {
        String sql = "delete from borrowingrecords where record_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, borrowingRecord.getRecordId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<BorrowingRecord> getAll() {
        String sql = "SELECT * FROM borrowingrecords";
        return getRecord(sql);
    }

    public <U> BorrowingRecord get(U recordId) {
        String sql = "SELECT * FROM borrowingrecords where record_id = ?";
        BorrowingRecord borrowingRecord = null;
        try (Connection conn= DatabaseConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, (int)recordId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String isbn = rs.getString("isbn");
//                Timestamp borrowDate = rs.getTimestamp("borrow_date");
//                Timestamp returnDate = rs.getTimestamp("return_date");
                Timestamp borrowDate=rs.getTimestamp("borrow_date");
                Timestamp returnDate=rs.getTimestamp("return_date");
                String status = rs.getString("status");
                borrowingRecord= new BorrowingRecord((int)recordId,userId,isbn, DateFormat.toLocalDateTime(borrowDate), DateFormat.toLocalDateTime(returnDate), status);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return borrowingRecord;
    }


    //get lent book
    public List<BorrowingRecord> getLent() {
        String sql = "select * from borrowingrecords where status='borrowed'";
        return getRecord(sql);
    }

    //get returned
    public List<BorrowingRecord> getReturned() {
        String sql= "select * from borrowingrecords where status='returned'";
        return getRecord(sql);
    }

    //get late
    public List<BorrowingRecord> getLate() {
        List<BorrowingRecord> list = getLent();
        FineDao fineDao = new FineDao();
        for (BorrowingRecord borrowingRecord : list) {
            fineDao.checkAndAddFine(borrowingRecord);
        }
        String sql = "select * from borrowingrecords where status='late'";
        return getRecord(sql);
    }

    //get lost
    public List<BorrowingRecord> getLost() {
        List<BorrowingRecord> list = getLent();
        FineDao fineDao = new FineDao();
        for (BorrowingRecord borrowingRecord : list) {
            fineDao.checkAndAddFine(borrowingRecord);
        }
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
                Timestamp borrowDate=rs.getTimestamp("borrow_date");
                Timestamp returnDate=rs.getTimestamp("return_date");
                String status = rs.getString("status");
                list.add(new BorrowingRecord(recordId,userId,isbn, DateFormat.toLocalDateTime(borrowDate), DateFormat.toLocalDateTime(returnDate), status));
                //list.add(new BorrowingRecord(recordId,userId,isbn, DateFormat.toLocalDate(borrowDate),DateFormat.toLocalDateTime(returnDate),status));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    //use when user want to borrow book, so we add new record
    public <U> void add(U br) {
        if (!(br instanceof BorrowingRecord borrowingRecord)) {
            throw new IllegalArgumentException("BorrowingRecord is not a BorrowingRecord");
        }
        String sql="insert into borrowingrecords(user_id, isbn, borrow_date,return_date, status) values(?,?,?,?,?)";
        try(
                Connection conn=DatabaseConfig.getConnection();
                PreparedStatement ps=conn.prepareStatement(sql);
        ) {
            //  ps.setInt(1, borrowingRecord.getRecordId());
            ps.setInt(1, borrowingRecord.getUserId());
            ps.setString(2, borrowingRecord.getISBN());
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.setTimestamp(4, null);
            ps.setString(5, borrowingRecord.getStatus());
            ps.executeUpdate();
            DocumentDao documentDao = new DocumentDao();
            documentDao.updateQuantity(borrowingRecord.getISBN(), borrowingRecord.getStatus());
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
            ps.setTimestamp(2, DateFormat.toSqlTimestamp(br.getReturnDate()));
            ps.setInt(3, br.getRecordId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void returnDoc(BorrowingRecord br) throws SQLException {
        br.setStatus("returned");
        br.setReturnDate(LocalDateTime.now());
        update(br);
        DocumentDao documentDao = new DocumentDao();
        documentDao.updateQuantity(br.getISBN(), br.getStatus());
    }

    //get borrowing record by id
    public BorrowingRecord get(int id) {
        String sql = "SELECT * FROM BorrowingRecords WHERE record_id = ?";
        BorrowingRecord borrowingRecord = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                borrowingRecord = new BorrowingRecord();
                borrowingRecord.setRecordId(rs.getInt("record_id"));
                borrowingRecord.setUserId(rs.getInt("user_id"));
                borrowingRecord.setISBN(rs.getString("isbn"));
                borrowingRecord.setBorrowDate(rs.getTimestamp("borrow_date").toLocalDateTime());
                borrowingRecord.setStatus(rs.getString("status"));
                //neu chua return thi return date bang null so we have to check
                if (rs.getString("status").equals("returned")) {
                    borrowingRecord.setReturnDate(rs.getTimestamp("return_date").toLocalDateTime());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowingRecord;
    }

}
