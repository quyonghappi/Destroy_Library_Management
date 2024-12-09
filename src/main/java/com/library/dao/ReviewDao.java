package com.library.dao;

import com.library.config.DatabaseConfig;
import com.library.models.BorrowingRecord;
import com.library.models.Review;
import com.library.models.User;
import com.library.utils.DateFormat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao implements DAO<Review> {
    //get all reviews and order them by rating in descending order
    public List<Review> getAll() {
        String sql="select * from Reviews " +
                "order by rating desc";
        List<Review> reviews = new ArrayList<>();
        try (
                Connection conn= DatabaseConfig.getConnection();
                PreparedStatement ps=conn.prepareStatement(sql);
        ) {
            ResultSet rs=ps.executeQuery();
            Review review = new Review();
            while(rs.next()) {
                review.setReviewId(rs.getInt("review_id"));
                review.setIsbn(rs.getString("isbn"));
                review.setUserId(rs.getInt("user_id"));
                review.setRating(rs.getDouble("rating"));
                review.setComment(rs.getString("comment"));
                review.setReviewDate(DateFormat.toLocalDateTime(rs.getTimestamp("review_date")));
                reviews.add(review);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("can not retrieve reviews");
        }
        return reviews;
    }

    //get review by its id
    public <U> Review get(U id) {
        if (!(id instanceof Integer reviewId)) {
            throw new RuntimeException("reviewId must be an instance of Integer");
        }
        String sql="select * from Reviews where review_id=?";
        Review review = null;
        try (
                Connection conn =  DatabaseConfig.getConnection();
                PreparedStatement ps=conn.prepareStatement(sql)
        ) {
            ps.setInt(1, reviewId);
            ResultSet rs=ps.executeQuery();
            if (rs.next()) {
                review.setReviewId(rs.getInt("review_id"));
                review.setIsbn(rs.getString("isbn"));
                review.setUserId(rs.getInt("user_id"));
                review.setRating(rs.getDouble("rating"));
                review.setComment(rs.getString("comment"));
                review.setReviewDate(DateFormat.toLocalDateTime(rs.getTimestamp("review_date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("can not retrieve reviews");
        }
        return review;
    }

    //when users create their reviews for a book
    //check xem user nay phai muon sach nay roi thi moi cho review
    public <U> void add(U r) {
        if (!(r instanceof Review review)) {
            throw new IllegalArgumentException("review must be an instance of Review");
        }
        int userId = review.getUserId();
        UserDao userDao = new UserDao();
        User user = userDao.get(userId);
        BorrowingRecordDao borrowingRecordDao = new BorrowingRecordDao();
        List<BorrowingRecord> userBooksList = borrowingRecordDao.getByUserName(user.getUsername());
        for (BorrowingRecord borrowingRecord : userBooksList) {
            //means that user nay da muon cuon sach nay
            if(borrowingRecord.getISBN().equals(review.getIsbn())) {
                String sql = "insert into reviews(user_id, isbn, rating, comment, review_date) values(?,?,?,?,?)";
                try (
                        Connection conn=DatabaseConfig.getConnection();
                        PreparedStatement ps=conn.prepareStatement(sql);
                ) {
                    ps.setInt(1, review.getUserId());
                    ps.setString(2, review.getIsbn());
                    ps.setDouble(3, review.getRating());
                    ps.setString(4, review.getComment());
                    ps.setTimestamp(5, DateFormat.toSqlTimestamp(review.getReviewDate()));

                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException("cannot add new review" + e.getMessage());
                }
                return;
            }
        }
    }

    public void delete(Review review) {
        String sql="delete from reviews where review_id=?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, review.getReviewId());
            pstm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //use to display all reviews for each book
    public List<Review> getByIsbn(String isbn) {
        String sql = "select * from Reviews where isbn=?";
        List<Review> reviews = new ArrayList<>();
        try (
                Connection conn= DatabaseConfig.getConnection();
                PreparedStatement ps=conn.prepareStatement(sql);
        ) {
            ps.setString(1, isbn);
            ResultSet rs=ps.executeQuery();
            Review review = new Review();
            while(rs.next()) {
                review.setReviewId(rs.getInt("review_id"));
                review.setIsbn(rs.getString("isbn"));
                review.setUserId(rs.getInt("user_id"));
                review.setRating(rs.getDouble("rating"));
                review.setComment(rs.getString("comment"));
                review.setReviewDate(DateFormat.toLocalDateTime(rs.getTimestamp("review_date")));
                reviews.add(review);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("can not retrieve reviews");
        }
        return reviews;
    }

    //filter để display reviews
    public List<Review> getByRating(double smallRating, double largeRating) {
        String sql = "select * from Reviews where rating>=? and rating<=?";
        List<Review> reviews = new ArrayList<>();
        try (
                Connection conn= DatabaseConfig.getConnection();
                PreparedStatement ps=conn.prepareStatement(sql);
        ) {
            ps.setDouble(1, smallRating);
            ps.setDouble(2, largeRating);
            ResultSet rs=ps.executeQuery();
            Review review = new Review();
            while(rs.next()) {
                review.setReviewId(rs.getInt("review_id"));
                review.setIsbn(rs.getString("isbn"));
                review.setUserId(rs.getInt("user_id"));
                review.setRating(rs.getDouble("rating"));
                review.setComment(rs.getString("comment"));
                review.setReviewDate(DateFormat.toLocalDateTime(rs.getTimestamp("review_date")));
                reviews.add(review);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("can not retrieve reviews");
        }
        return reviews;

    }

}
