package com.library.dao;

import com.library.models.Document;
import com.library.config.DatabaseConfig;
import com.library.models.User;

import java.sql.*;

public class DocumentDao {

    //insert document into database
    public void insertDocument(Document doc) throws SQLException {

        try {
            //get or create authorid
            int authorId = getOrCreateAuthorId(doc.getAuthor().getName());
            doc.setAuthorId(authorId);

            //get or create publisherid
            int publisherId = getOrCreatePublisherId(doc.getPublisher().getName());
            doc.setPublisherId(publisherId);

            //get or create categoryid
            int categoryId = getOrCreateCategoryId(doc.getCategory().getName());
            doc.setCategoryId(categoryId);


            //sql query to insert or update a document
            String sql = "INSERT INTO documents (title, author_id, publisher_id, isbn, category_id, publication_year, " +
                    "quantity, description, location, preview_link) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " + // Change to 10 placeholders
                    "ON DUPLICATE KEY UPDATE " +
                    "title = VALUES(title), " +
                    "author_id = VALUES(author_id), " +
                    "publisher_id = VALUES(publisher_id), " +
                    "category_id = VALUES(category_id), " +
                    "publication_year = VALUES(publication_year), " +
                    "description = VALUES(description), " +
                    "location = VALUES(location), " +
                    "preview_link = VALUES(preview_link)";


            try (Connection connection = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = connection.prepareStatement(sql)) {

                pstmt.setString(1, doc.getTitle());
                pstmt.setInt(2, authorId);
                pstmt.setInt(3, publisherId); 
                pstmt.setString(4, doc.getISBN());
                pstmt.setInt(5, categoryId);

                if (doc.getPublicationYear() != 0) {
                    pstmt.setInt(6, doc.getPublicationYear());
                } else {
                    pstmt.setNull(6, java.sql.Types.INTEGER);
                }
                pstmt.setInt(7, doc.getQuantity());
//               pstmt.setInt(8, doc.getAvailableCopies());
                pstmt.setString(8, doc.getDescription());
                pstmt.setString(9, doc.getLocation());
                pstmt.setString(10, doc.getPreviewLink());

                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error inserting document: " + e.getMessage());
            throw e; //re-throw the exception for further handling
        }
    }

    //find a document by ISBN
    public Document findByISBN(String isbn) throws SQLException {
        String selectSql = "SELECT * FROM documents WHERE isbn = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectSql)) {

            ps.setString(1, isbn);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                Document doc = new Document();
                doc.setISBN(resultSet.getString("isbn"));
                doc.setTitle(resultSet.getString("title"));
                doc.setAuthorId(resultSet.getInt("author_id"));
                doc.setPublisherId(resultSet.getInt("publisher_id"));  // Changed to correct field
                doc.setCategoryId(resultSet.getInt("category_id"));  // Changed to correct field
                doc.setPublicationYear(resultSet.getInt("publication_year"));
//                doc.setTotalCopies(resultSet.getInt("total_copies"));
//                doc.setAvailableCopies(resultSet.getInt("available_copies"));
                doc.setQuantity(resultSet.getInt("quantity"));
                doc.setDescription(resultSet.getString("description"));
                doc.setLocation(resultSet.getString("location"));
                doc.setPreviewLink(resultSet.getString("preview_link"));
                return doc;
            }
        }

        return null;
    }

    //delete doc
    public void delete(Document doc) throws SQLException {
        Connection conn = DatabaseConfig.getConnection();
        String sql = "DELETE FROM documents WHERE title= ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doc.getTitle());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //when user return book or borrow book, update available copies
    public void updateQuantity(String isbn, String status) throws SQLException {
        Connection conn = DatabaseConfig.getConnection();
        String sql ="update documents set quantity= quantity + ? where isbn = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            if (status.equals("returned")) ps.setInt(1, 1);
            else if(status.equals("borrowed") || status.equals("lost")) ps.setInt(1, -1);
            ps.setString(2, isbn);
            //execute the update
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

    //methods to fetch or create author, publisher, and category IDs
    public int getOrCreateAuthorId(String authorName) throws SQLException {
        String selectSql = "SELECT author_id FROM Authors WHERE name = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectSql)) {

            pstmt.setString(1, authorName);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("author_id");
            }
        }

        //if author doesnt exist then insert it
        return insertAuthor(authorName);
    }

    public int getOrCreatePublisherId(String publisherName) throws SQLException {
        String selectSql = "SELECT publisher_id FROM Publishers WHERE name = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectSql)) {

            pstmt.setString(1, publisherName);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("publisher_id");
            }
        }

        //if publisher doesnt exist, insert
        return insertPublisher(publisherName);
    }

    public int getOrCreateCategoryId(String categoryName) throws SQLException {
        String selectSql = "SELECT category_id FROM Categories WHERE name = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectSql)) {

            pstmt.setString(1, categoryName);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("category_id");
            }
        }

        //if category doesnt exist then insert
        return insertCategory(categoryName);
    }

    //insert author, publisher, and category if they do not exist
    public int insertAuthor(String authorName) throws SQLException {
        String sql = "INSERT INTO authors (name) VALUES (?)";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, authorName);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public int insertPublisher(String publisherName) throws SQLException {
        String sql = "INSERT INTO publishers (name) VALUES (?)";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, publisherName);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public int insertCategory(String categoryName) throws SQLException {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, categoryName);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }
}
