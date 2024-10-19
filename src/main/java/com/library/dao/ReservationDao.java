package com.library.dao;

import com.library.config.DatabaseConfig;
import com.library.models.Reservation;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
