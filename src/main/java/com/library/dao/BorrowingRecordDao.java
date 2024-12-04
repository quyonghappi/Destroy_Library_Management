package com.library.dao;

import com.library.config.DatabaseConfig;
import com.library.models.BorrowingRecord;
import com.library.models.Document;
import com.library.models.User;
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
    // CHECK XEM QUAS 14 NGAY THI PHAT THOI
    public List<BorrowingRecord> getLent() {
        String updateSql = """
        UPDATE borrowingrecords
        SET status = CASE
            WHEN status = 'borrowed' AND borrow_date < NOW() - INTERVAL 14 DAY THEN 'late'
            WHEN status = 'borrowed' AND borrow_date < NOW() - INTERVAL 30 DAY THEN 'lost'
            ELSE status
        END
        WHERE status IN ('borrowed', 'late', 'lost');
    """;

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Trả về danh sách đã cập nhật
        return getRecord("SELECT * FROM borrowingrecords WHERE status IN ('borrowed', 'late', 'lost')");
    }


    //get returned
    public List<BorrowingRecord> getReturned() {
        String sql= "select * from borrowingrecords where status='returned'";
        return getRecord(sql);
    }

//    //get late
    public List<BorrowingRecord> getLate() {
        List<BorrowingRecord> list = getLent();
        FineDao fineDao = new FineDao();
        for (BorrowingRecord borrowingRecord : list) {
            fineDao.checkAndAddFine(borrowingRecord);
        }
        String sql = "select * from borrowingrecords where status='late'";
        List<BorrowingRecord> lateList = getRecord(sql);
        return lateList;
    }

    //get lost, already have this in doc dao
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
        List<BorrowingRecord> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int recordId = rs.getInt("record_id");
                int userId = rs.getInt("user_id");
                String isbn = rs.getString("isbn");

                Timestamp borrowDate = rs.getTimestamp("borrow_date");
                Timestamp returnDate = rs.getTimestamp("return_date");

                LocalDateTime borrowDateTime = borrowDate != null ? DateFormat.toLocalDateTime(borrowDate) : null;
                LocalDateTime returnDateTime = returnDate != null ? DateFormat.toLocalDateTime(returnDate) : null;

                String status = rs.getString("status");
                list.add(new BorrowingRecord(recordId, userId, isbn, borrowDateTime, returnDateTime, status));
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
                PreparedStatement ps=conn.prepareStatement(sql)
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
                PreparedStatement ps= conn.prepareStatement(sql)
        ) {
            ps.setString(1, br.getStatus());
            ps.setTimestamp(2, DateFormat.toSqlTimestamp(br.getReturnDate()));
            ps.setInt(3, br.getRecordId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (br.getStatus().equals("returned") || br.getStatus().equals("lost")) {
            DocumentDao documentDao = new DocumentDao();
            documentDao.updateQuantity(br.getISBN(), br.getStatus());
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

    public List<BorrowingRecord> searchByIsbn(String isbn) {
        List<BorrowingRecord> borrowingRecords = new ArrayList<>();

        // Start building the query
        String sql = "SELECT * FROM borrowingrecords WHERE status IN ('borrowed', 'lost', 'late')";

        // Append condition for isbn if provided
        if (isbn != null && !isbn.trim().isEmpty()) {
            sql += " AND isbn LIKE ?";
        }

        // Establish database connection and prepare statement
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set the isbn parameter if necessary
            if (isbn != null && !isbn.trim().isEmpty()) {
                ps.setString(1, "%" + isbn.trim() + "%");
            }

            // Execute the query and process the result set
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int recordId = rs.getInt("record_id");
                    int userId = rs.getInt("user_id");
                    String isbnResult = rs.getString("isbn");
                    Timestamp borrowDate = rs.getTimestamp("borrow_date");
                    Timestamp returnDate = rs.getTimestamp("return_date");

                    // Convert the Timestamp to LocalDateTime if available
                    LocalDateTime borrowDateTime = borrowDate != null ? DateFormat.toLocalDateTime(borrowDate) : null;
                    LocalDateTime returnDateTime = returnDate != null ? DateFormat.toLocalDateTime(returnDate) : null;

                    String status = rs.getString("status");

                    // Add the record to the list
                    borrowingRecords.add(new BorrowingRecord(recordId, userId, isbnResult, borrowDateTime, returnDateTime, status));
                }
            }
        } catch (SQLException e) {
            // Log or handle the exception
            System.err.println("Error executing SQL query: " + e.getMessage());
            e.printStackTrace();  // or use a logging framework
        }

        return borrowingRecords;
    }

    public List<BorrowingRecord> getReturnByISBN(String isbn) {
        String sql = "SELECT * FROM borrowingrecords WHERE status IN ('returned')";
        List<BorrowingRecord> returnISBN = new ArrayList<>();

        // Append condition for isbn if provided
        if (isbn != null && !isbn.trim().isEmpty()) {
            sql += " AND isbn LIKE ?";
        }

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (isbn != null && !isbn.trim().isEmpty()) {
                ps.setString(1, "%" + isbn.trim() + "%");
            }

            // Execute the query and process the result set
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int recordId = rs.getInt("record_id");
                    int userId = rs.getInt("user_id");
                    String isbnResult = rs.getString("isbn");
                    Timestamp borrowDate = rs.getTimestamp("borrow_date");
                    Timestamp returnDate = rs.getTimestamp("return_date");

                    // Convert the Timestamp to LocalDateTime if available
                    LocalDateTime borrowDateTime = borrowDate != null ? DateFormat.toLocalDateTime(borrowDate) : null;
                    LocalDateTime returnDateTime = returnDate != null ? DateFormat.toLocalDateTime(returnDate) : null;

                    String status = rs.getString("status");

                    // Add the record to the list
                    returnISBN.add(new BorrowingRecord(recordId, userId, isbnResult, borrowDateTime, returnDateTime, status));
                }
            }
        } catch (SQLException e) {
            // Log or handle the exception
            System.err.println("Error executing SQL query: " + e.getMessage());
            e.printStackTrace();  // or use a logging framework
        }

        return returnISBN;
    }




}
