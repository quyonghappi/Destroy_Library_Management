package com.library.dao;

import com.library.config.DatabaseConfig;
import com.library.models.Reservation;
import com.library.utils.DateFormat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDao {
    //use for denying or approving reservation, when approve thi phai add new record vao borrowing
    public void delete(Reservation reservation) {
        String sql="delete from reservations where reservation_id=?";
        try(Connection con= DatabaseConfig.getConnection();
            PreparedStatement pst=con.prepareStatement(sql)){
            pst.setInt(1,reservation.getReservationId());
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(Reservation reservation) {
        String sql="insert into reservations(user_id, isbn, reservation_date,status) values(?,?,?,?)";
        try (
                Connection conn=DatabaseConfig.getConnection();
                PreparedStatement ps=conn.prepareStatement(sql);
        ) {
            ps.setInt(1,reservation.getUserId());
            ps.setString(2,reservation.getIsbn());
            ps.setTimestamp(3, DateFormat.toSqlTimestamp(reservation.getReservationDate()));
            ps.setString(4,"active");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //get reservation list
    public List<Reservation> getReservations() {
        String sql="select * from reservations";
        List<Reservation> reservations = new ArrayList<Reservation>();
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

    public Reservation getById(int reservation_id) {
        String sql="select * from reservations where reservation_id=?";
        Reservation reservation=null;
        try(Connection conn=DatabaseConfig.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)) {
            ps.setInt(1,reservation_id);
            ResultSet rs=ps.executeQuery();
            while(rs.next()) {
                int user_id=rs.getInt("user_id");
                String isbn=rs.getString("isbn");
                LocalDateTime date=rs.getTimestamp("reservation_date").toLocalDateTime();
                String status=rs.getString("status");
                reservation = new Reservation(reservation_id,user_id,isbn,date,status);
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservation ;
    }

    public int getByISBN(String isbn) {
        String sql="select count(*) from reservations where isbn=?";
        int numOfReservations=0;
        try(Connection conn=DatabaseConfig.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)) {
            ps.setString(1,isbn);
            ResultSet rs=ps.executeQuery();
            while(rs.next()) {
                numOfReservations++;
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return numOfReservations;
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
    }
}