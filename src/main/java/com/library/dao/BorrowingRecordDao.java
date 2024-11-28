package com.library.dao;

import com.library.config.DatabaseConfig;
import com.library.models.BorrowingRecord;
import com.library.utils.DateFormat;

import java.sql.*;
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

    //get record by its id
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

//    //get late
//    public List<BorrowingRecord> getLate() {
//        List<BorrowingRecord> list = getLent();
//        FineDao fineDao = new FineDao();
//        for (BorrowingRecord borrowingRecord : list) {
//            fineDao.checkAndAddFine(borrowingRecord);
//        }
//        String sql = "select * from borrowingrecords where status='late'";
//        List<BorrowingRecord> lateList = getRecord(sql);
//        return lateList;
//    }

//    //get lost, already have this in doc dao
//    public List<BorrowingRecord> getLost() {
//        List<BorrowingRecord> list = getLent();
//        FineDao fineDao = new FineDao();
//        for (BorrowingRecord borrowingRecord : list) {
//            fineDao.checkAndAddFine(borrowingRecord);
//        }
//        String sql = "select * from borrowingrecords where status='Lost'";
//        return getRecord(sql);
//    }

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

    //update when user return book or a borrow is overdue
    public void update(BorrowingRecord br) {
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

    public List<BorrowingRecord> getByUserName(String userName) {
        String userIdQuery = "select user_id from users where user_name = ?";
        String sql = "select * from borrowingrecords where user_id=?";
        List<BorrowingRecord> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(userIdQuery)) {
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");

                try (PreparedStatement recordStmt = conn.prepareStatement(sql)) {
                    recordStmt.setInt(1, userId);
                    ResultSet recordRs = recordStmt.executeQuery();
                    while (recordRs.next()) {
                        int recordId = recordRs.getInt("record_id");
                        String isbn = recordRs.getString("isbn");
                        Timestamp borrowDate = recordRs.getTimestamp("borrow_date");
                        Timestamp returnDate = recordRs.getTimestamp("return_date");
                        String status = recordRs.getString("status");
                        list.add(new BorrowingRecord(
                                recordId, userId, isbn,
                                DateFormat.toLocalDateTime(borrowDate),
                                DateFormat.toLocalDateTime(returnDate),
                                status));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("cannot retrieve borrowing records for username: " + userName, e);
        }
        return list;
    }
}
