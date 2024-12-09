package com.library.dao;
import com.library.config.DatabaseConfig;
import com.library.controller.Subject;
import com.library.models.BorrowingRecord;
import com.library.models.Fine;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FineDao extends Subject implements DAO<Fine> {
    private static FineDao instance = null;

    private FineDao() {
    }

    public static FineDao getInstance() {
        if (instance==null) {
            instance = new FineDao();
        }
        return instance;
    }

    //get all fines in db
    public List<Fine> getAll() {
        List<BorrowingRecord> br = new BorrowingRecordDao().getLent();
        for (BorrowingRecord brr : br) {
            checkAndAddFine(brr);
        }

        String sql = "select * from Fines";
        List<Fine> fineList = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
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

    //check if fine record for that overdue borrowing is exist
    public boolean fineExists(int recordId) {
        String sql = "SELECT count(*) FROM fines WHERE record_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recordId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    //get list of paid fines
    public List<Fine> getPaid() {
        List<Fine> paidList = new ArrayList<>();
        String sql="select * from Fines where status = 'PAID'";
        try(Connection conn= DatabaseConfig.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
        ) {
            while (rs.next()) {
                Fine fine = new Fine();
                fine.setFineId(rs.getInt(1));
                fine.setUserId(rs.getInt(2));
                fine.setRecordId(rs.getInt(3));
                fine.setFineAmount(rs.getBigDecimal(4));
                fine.setDueDate(rs.getDate(5).toLocalDate());
                fine.setStatus(rs.getString(6));
                paidList.add(fine);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return paidList;
    }

    //get list of unpaid fines
    public List<Fine> getUnpaid() {
        List<Fine> unpaidList = new ArrayList<>();
        String sql="select * from Fines where status = 'UNPAID'";
        try(Connection conn= DatabaseConfig.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
        ) {
            while (rs.next()) {
                Fine fine = new Fine();
                fine.setFineId(rs.getInt(1));
                fine.setUserId(rs.getInt(2));
                fine.setRecordId(rs.getInt(3));
                fine.setFineAmount(rs.getBigDecimal(4));
                fine.setDueDate(rs.getDate(5).toLocalDate());
                fine.setStatus(rs.getString(6));
                unpaidList.add(fine);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return unpaidList;
    }

    //get fine details with borrowing record id
    public <U> Fine get(U id) {
        if (!(id instanceof Integer)) {
            throw new IllegalArgumentException("Expected an Integer for recordId");
        }
        int recordId = (Integer) id;
        String sql = "SELECT f.user_id, f.record_id, f.fine_amount, f.due_date, f.status, br.isbn " +
                "FROM fines f JOIN BorrowingRecords br ON f.record_id = br.record_id " +
                "WHERE f.record_id = ?";

        Fine fine = null;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, recordId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                fine=new Fine();
                fine.setUserId(rs.getInt("user_id"));
                fine.setRecordId(rs.getInt("record_id"));
                fine.setDueDate(rs.getDate("due_date").toLocalDate());
                fine.setStatus(rs.getString("status"));
                fine.setFineAmount(rs.getBigDecimal("fine_amount"));

            }
        } catch (SQLException e) {
            throw new RuntimeException("no fine found with recordId: " + id);
        }
        return fine;
    }

    //truong hop user gia han sau khi fine duoc tao thi minh delete
    public void delete(Fine fine) {
        String sql="delete from fines where fines.record_id=?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, fine.getRecordId());
            pstm.executeUpdate();
            notifyObservers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void checkAndAddFine(BorrowingRecord br) {
//        LocalDateTime dueDate = br.getBorrowDate().plusDays(14);
//        boolean getFine = br.getStatus().equals("lost");
//        if (isOverdue(dueDate)) {
//            br.setStatus("late");
//            BorrowingRecordDao borrowingRecordDao = new BorrowingRecordDao();
//            borrowingRecordDao.update(br);
//        }
        boolean getFine = br.getStatus().equals("lost") || br.getStatus().equals("late");
        if (getFine && !fineExists(br.getRecordId())) {
            System.out.println("Adding fine for record ID: " + br.getRecordId());
            add(br);
        }
    }

    public <U> void add(U r) {
        if(!(r instanceof BorrowingRecord record)) {
            throw new IllegalArgumentException("Not BorrowingRecord");
        }
        String sql="insert into fines(user_id, record_id, fine_amount, due_date, status) values(?,?,?,?,?)";

        try (
                Connection conn=DatabaseConfig.getConnection();
                PreparedStatement ps=conn.prepareStatement(sql);
        ) {
            ps.setInt(1,record.getUserId());
            ps.setInt(2,record.getRecordId());
            ps.setDouble(3,calculateFine(record));
            //sau 14 ngay muon khong tra thi thanh fine
            //sau do thi have 14 days to pay fine
            if (record.getStatus().equals("lost")) {
                ps.setObject(4, record.getBorrowDate().plusDays(14+30));
            } else {
                ps.setObject(4,record.getBorrowDate().plusDays(24));
            }
            ps.setString(5,"UNPAID");

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public double calculateFine(BorrowingRecord record) {
        //10k each day overdue
        if (record.getStatus().equals("late")) {
            //only used for local date
            //Period period=Period.between(LocalDateTime.now(), record.getReturnDate());
            return daysOverdue(record) * 5000;
        } else if (record.getStatus().equals("lost")) {
            return 200000;
        }
        return 0;
    }


    public long daysOverdue(BorrowingRecord record) {
        Duration duration = Duration.between(record.getBorrowDate().plusDays(14), LocalDateTime.now());;
        return duration.toDays();
    }

    //change fine status if it was paid
    public void changeFineStatus(int recordId) {
        String sql = "UPDATE fines SET status=? WHERE record_id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            BorrowingRecordDao brDao = new BorrowingRecordDao();
            BorrowingRecord br=brDao.get(recordId);
            if (br.getStatus().equals("late")) {
                DocumentDao documentDao = new DocumentDao();
                documentDao.updateQuantity(br.getISBN(), "returned");
            }
            ps.setString(1,"PAID");
            ps.setInt(2, recordId);
            ps.executeUpdate();
            notifyObservers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //get fine information by user id
    public List<Fine> getFinesByUserId(int userId) {
        List<Fine> fines = new ArrayList<>();
        String sql = "SELECT * FROM fines WHERE user_id=?";
        try (Connection conn=DatabaseConfig.getConnection();
             PreparedStatement ps=conn.prepareStatement(sql)) {
            ps.setInt(1,userId);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
                Fine fine = new Fine();
                fine.setUserId(rs.getInt("user_id"));
                fine.setRecordId(rs.getInt("record_id"));
                fine.setDueDate(rs.getDate("due_date").toLocalDate());
                fine.setStatus(rs.getString("status"));
                fine.setFineAmount(rs.getBigDecimal("fine_amount"));
                fine.setFineId(rs.getInt("fine_id"));
                fines.add(fine);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return fines;
    }

    public List<Fine> getFinesByUsername(String username) {
        String userIdQuery = "SELECT user_id FROM users WHERE user_name like ?";
        String sql = "SELECT * FROM fines WHERE user_id = ?";
        List<Fine> fines = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement userIdPs = conn.prepareStatement(userIdQuery)) {

            userIdPs.setString(1, "%"+username+"%");
            try (ResultSet userIdRs = userIdPs.executeQuery()) {
                if (userIdRs.next()) {
                    int userId = userIdRs.getInt("user_id");

                    //get reservations for the user_id
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, userId);
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                Fine fine = new Fine();
                                fine.setUserId(rs.getInt("user_id"));
                                fine.setRecordId(rs.getInt("record_id"));
                                fine.setDueDate(rs.getDate("due_date").toLocalDate());
                                fine.setStatus(rs.getString("status"));
                                fine.setFineAmount(rs.getBigDecimal("fine_amount"));
                                fine.setFineId(rs.getInt("fine_id"));
                                fines.add(fine);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching fines for username: " + username, e);
        }
        return fines;
    }
}
