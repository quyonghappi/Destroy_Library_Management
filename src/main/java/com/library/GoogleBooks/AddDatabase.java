package com.library.GoogleBooks;

import java.sql.*;

import static com.library.config.APIConfig.*;

public class AddDatabase {
    // insert author
    public static int getOrInsertAuthor(String authorName, Connection connection) throws Exception {
        String selectQuery = "SELECT author_id FROM Authors WHERE name = ?";
        PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
        selectStmt.setString(1, authorName);
        ResultSet rs = selectStmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("author_id");
        }

        String insertQuery = "INSERT INTO Authors (name) VALUES (?)";
        PreparedStatement insertStmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
        insertStmt.setString(1, authorName);
        insertStmt.executeUpdate();

        ResultSet generatedKeys = insertStmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        } else {
            throw new Exception("Unable to add or retrieve the author");
        }
    }
    // insert publisher
    public static int getOrInsertPublisher(String publisherName, Connection connection) throws Exception {
        String selectQuery = "SELECT publisher_id FROM Publishers WHERE name = ?";
        PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
        selectStmt.setString(1, publisherName);
        ResultSet rs = selectStmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("publisher_id");
        }

        String insertQuery = "INSERT INTO Publishers (name, address, contact_info) VALUES (?, ?, ?)";
        PreparedStatement insertStmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
        insertStmt.setString(1, publisherName);
        insertStmt.setString(2, "Address not available");
        insertStmt.setString(3, "Contact info not available");
        insertStmt.executeUpdate();

        ResultSet generatedKeys = insertStmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        } else {
            throw new Exception("Unable to add or retrieve the publisher");
        }
    }
    // insert category
    public static int getOrInsertCategory(String categoryName, Connection connection) throws Exception {
        String selectQuery = "SELECT category_id FROM Categories WHERE name = ?";
        PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
        selectStmt.setString(1, categoryName);
        ResultSet rs = selectStmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("category_id");
        }

        String insertQuery = "INSERT INTO Categories (name, description) VALUES (?, ?)";
        PreparedStatement insertStmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
        insertStmt.setString(1, categoryName);
        insertStmt.setString(2, "No description available");
        insertStmt.executeUpdate();

        ResultSet generatedKeys = insertStmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        } else {
            throw new Exception("Unable to add or retrieve the category");
        }
    }
    // insert document
    public static void insertDocumentIntoDatabase(String title, String authorName, String publisherName, String isbn, String categoryName, int publicationYear) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);

            int authorId = getOrInsertAuthor(authorName, connection);
            int publisherId = getOrInsertPublisher(publisherName, connection);
            int categoryId = getOrInsertCategory(categoryName, connection);

            String insertBookQuery = "INSERT INTO Documents (title, author_id, publisher_id, isbn, category_id, publication_year, total_copies, available_copies, description, location) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertBookStmt = connection.prepareStatement(insertBookQuery);
            insertBookStmt.setString(1, title);
            insertBookStmt.setInt(2, authorId);
            insertBookStmt.setInt(3, publisherId);
            insertBookStmt.setString(4, isbn);
            insertBookStmt.setInt(5, categoryId);
            insertBookStmt.setInt(6, publicationYear);
            insertBookStmt.setInt(7, 10);
            insertBookStmt.setInt(8, 10);
            insertBookStmt.setString(9, "No description");
            insertBookStmt.setString(10, "Hanoi Library");

            insertBookStmt.executeUpdate();

            connection.commit();
            System.out.println("Book inserted successfully with Document ID: " + isbn);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (Exception closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
}
