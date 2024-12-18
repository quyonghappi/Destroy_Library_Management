package com.library.dao;

import com.library.config.DatabaseConfig;
import com.library.controller.Subject;
import com.library.models.Reservation;
import com.library.utils.DateFormat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDao extends Subject implements DAO<Reservation>  {
    private static ReservationDao instance;

    private ReservationDao() {}

    public static ReservationDao getInstance() {
        if (instance == null) {
            instance = new ReservationDao();
        }
        return instance;
    }

    //use when users change their mind
    public void delete(String isbn, int userId) {
        String sql = "delete from reservations where isbn = ? and user_id = ?";
        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, isbn);
            ps.setInt(2, userId);

            int rowsAffected = ps.executeUpdate();
            notifyObservers();
            if (rowsAffected > 0) {
                System.out.println("Reservation deleted successfully.");
            } else {
                System.out.println("No reservation found with the specified ISBN and username.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("cannot deleting reservation sent by " + userId, e);
        }
    }

    //overload delete method
    public void delete(Reservation reservation) {
        String sql="delete from reservations where reservation_id=?";
        try(Connection con= DatabaseConfig.getConnection();
            PreparedStatement pst=con.prepareStatement(sql)){
            pst.setInt(1,reservation.getReservationId());
            pst.executeUpdate();
            notifyObservers();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public <U> void add(U r) {
        if (!(r instanceof Reservation reservation)) {
            throw new IllegalArgumentException("r is not a Reservation");
        }
        String sql="insert into reservations(user_id, isbn, reservation_date,status) values(?,?,?,?)";
        try (
                Connection conn=DatabaseConfig.getConnection();
                PreparedStatement ps=conn.prepareStatement(sql)
        ) {
            ps.setInt(1,reservation.getUserId());
            ps.setString(2,reservation.getIsbn());
            ps.setTimestamp(3, DateFormat.toSqlTimestamp(reservation.getReservationDate()));
            ps.setString(4,"active");
            ps.executeUpdate();
            notifyObservers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //get reservation list with asc reservation_date
    public List<Reservation> getAll() {
        String sql="select * from reservations " +
                "order by reservation_date";
        List<Reservation> reservations = new ArrayList<>();
        try(Connection con= DatabaseConfig.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ResultSet rs=ps.executeQuery();
            while(rs.next()) {
                int reservation_id=rs.getInt("reservation_id");
                int user_id=rs.getInt("user_id");
                String isbn=rs.getString("isbn");
                LocalDateTime date=rs.getTimestamp("reservation_date").toLocalDateTime();
                String status=rs.getString("status");
                reservations.add(new Reservation(reservation_id,user_id,isbn,date,status));
            }
        } catch(SQLException e){
            throw new RuntimeException();
        }
        return reservations;
    }

    //get fulfilled list
    public List<Reservation> getFulfilledReservations() {
        String sql="select * from reservations where status = 'fulfilled' ";
        List<Reservation> reservations = new ArrayList<>();
        try(Connection con= DatabaseConfig.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ResultSet rs=ps.executeQuery();
            while(rs.next()) {
                int reservation_id=rs.getInt("reservation_id");
                int user_id=rs.getInt("user_id");
                String isbn=rs.getString("isbn");
                LocalDateTime date=rs.getTimestamp("reservation_date").toLocalDateTime();
                String status=rs.getString("status");
                reservations.add(new Reservation(reservation_id,user_id,isbn,date,status));
            }
        } catch(SQLException e){
            throw new RuntimeException();
        }
        return reservations;
    }

    //get cancelled reservation
    public List<Reservation> getCancelledReservations() {
        String sql="select * from reservations where status = 'cancelled' ";
        List<Reservation> reservations = new ArrayList<>();
        try(Connection con= DatabaseConfig.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ResultSet rs=ps.executeQuery();
            while(rs.next()) {
                int reservation_id=rs.getInt("reservation_id");
                int user_id=rs.getInt("user_id");
                String isbn=rs.getString("isbn");
                LocalDateTime date=rs.getTimestamp("reservation_date").toLocalDateTime();
                String status=rs.getString("status");
                reservations.add(new Reservation(reservation_id,user_id,isbn,date,status));
            }
        } catch(SQLException e){
            throw new RuntimeException();
        }
        return reservations;
    }

    public <U> Reservation get(U reservation_id) {
        String sql="select * from reservations where reservation_id=?";
        try(Connection conn=DatabaseConfig.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)) {
            ps.setInt(1,(int)reservation_id);
            ResultSet rs=ps.executeQuery();
            while(rs.next()) {

                int user_id=rs.getInt("user_id");
                String isbn=rs.getString("isbn");
                LocalDateTime date=rs.getTimestamp("reservation_date").toLocalDateTime();
                String status=rs.getString("status");
                return new Reservation((int)reservation_id,user_id,isbn,date,status);
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     *
     * @return num of requests corresponding to book's isbn
     */
    public int getByISBN(String isbn) {
        String sql="select count(*) from reservations where isbn=?";
        try(Connection conn=DatabaseConfig.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)) {
            ps.setString(1,isbn);
            ResultSet rs=ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1); //get the count value
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    //tim ra request moi nhat cua nguoi dung va lay status hien thi trong book detail scene
    public Reservation reservationExists(String isbn, int userId) {
        String sql = "select * from reservations where isbn = ? and user_id = ? " +
                "order by reservation_date desc " + "limit 1";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setReservationId(rs.getInt("reservation_id"));
                reservation.setUserId(rs.getInt("user_id"));
                reservation.setIsbn(rs.getString("isbn"));
                reservation.setStatus(rs.getString("status"));
                reservation.setReservationDate(rs.getTimestamp("reservation_date").toLocalDateTime());
                return reservation;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateStatus(int reservation_id, String status) {
        String sql="update reservations set status=? where reservation_id=?";
        try (Connection connection=DatabaseConfig.getConnection();
            PreparedStatement ps= connection.prepareStatement(sql)) {
            ps.setString(1,status);
            ps.setInt(2,reservation_id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Reservation status updated.");
            } else {
                System.out.println("No reservation found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        notifyObservers();
    }

    public List<Reservation> getByUserName(String username) {
        String userIdQuery = "SELECT user_id FROM users WHERE user_name like ?";
        String sql = "SELECT * FROM reservations WHERE user_id = ?";
        List<Reservation> reservations = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement userIdPs = conn.prepareStatement(userIdQuery)) {

            userIdPs.setString(1, "%"+username+"%");
            try (ResultSet userIdRs = userIdPs.executeQuery()) {
                if (userIdRs.next()) {
                    int userId = userIdRs.getInt("user_id");

                    //get reservations for the user_id
                    try (PreparedStatement reservationPs = conn.prepareStatement(sql)) {
                        reservationPs.setInt(1, userId);
                        try (ResultSet reservationRs = reservationPs.executeQuery()) {
                            while (reservationRs.next()) {
                                int reservationId = reservationRs.getInt("reservation_id");
                                String isbn = reservationRs.getString("isbn");
                                LocalDateTime date = reservationRs.getTimestamp("reservation_date").toLocalDateTime();
                                String status = reservationRs.getString("status");
                                reservations.add(new Reservation(reservationId, userId, isbn, date, status));
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching reservations for username: " + username, e);
        }

        return reservations;
    }


}
