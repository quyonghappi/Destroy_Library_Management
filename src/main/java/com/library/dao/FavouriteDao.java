package com.library.dao;

import com.library.config.DatabaseConfig;
import com.library.models.Favourite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavouriteDao implements DAO<Favourite> {
    public List<Favourite> getAll() {
        String sql = "select * from favouritebooks";
        List<Favourite> favourites = new ArrayList<Favourite>();
        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Favourite favourite = new Favourite();
                favourite.setFavouriteId(rs.getInt(1));
                favourite.setUserId(rs.getInt(2));
                favourite.setIsbn(rs.getString(3));
                favourites.add(favourite);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favourites;
    }

    public <U> Favourite get(U id) {
        if (!(id instanceof Integer)) {
            throw new IllegalArgumentException("Expected an Integer for id");
        }
        
        Favourite favourite = null;
        String sql = "select * from favouritebooks where favourite_id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, (Integer)id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                favourite=new Favourite();
                favourite.setUserId(rs.getInt("user_id"));
                favourite.setIsbn(rs.getString("isbn"));
                favourite.setFavouriteId(rs.getInt("favourite_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("no favourite found with recordId: " + id);
        }
        return favourite;

    }

    public <U> void add(U f) {
        if(!(f instanceof Favourite favourite)) {
            throw new IllegalArgumentException("Not favourite instance");
        }
        String sql="insert into favouritebooks(user_id, isbn) values(?,?)";

        try (
                Connection conn=DatabaseConfig.getConnection();
                PreparedStatement ps=conn.prepareStatement(sql);
        ) {
            ps.setInt(1,favourite.getUserId());
            ps.setString(2,favourite.getIsbn());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    //delete favourite book by its id
    public void delete(Favourite favourite) {
        String sql="delete from favouritebooks where favourite_id=?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, favourite.getFavouriteId());
            pstm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Favourite> getByUserName(String username) {
        String userIdQuery = "SELECT user_id FROM users WHERE user_name = ?";
        String sql = "SELECT * FROM favourites WHERE user_id = ?";
        List<Favourite> favourites = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement userIdPs = conn.prepareStatement(userIdQuery)) {

            userIdPs.setString(1, username);
            try (ResultSet userIdRs = userIdPs.executeQuery()) {
                if (userIdRs.next()) {
                    int userId = userIdRs.getInt("user_id");

                    //get favourites for the user_id
                    try (PreparedStatement favouritePs = conn.prepareStatement(sql)) {
                        favouritePs.setInt(1, userId);
                        try (ResultSet favouriteRs = favouritePs.executeQuery()) {
                            while (favouriteRs.next()) {
                                int favouriteId = favouriteRs.getInt("favourite_id");
                                String isbn = favouriteRs.getString("isbn");
                                favourites.add(new Favourite(favouriteId, userId, isbn));
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching favourites for username: " + username, e);
        }
        return favourites;
    }

}
