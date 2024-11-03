package com.library.dao;

import com.library.config.DatabaseConfig;
import com.library.models.Author;
import com.library.models.Category;
import com.library.models.Document;
import com.library.models.Publisher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDao implements DAO<Document> {

    //insert document into database
    public <U> void add(U d) throws SQLException {
        if (!(d instanceof Document doc)) {
            throw new SQLException("Not a Document");
        }
        try {
            //get or create author id then insert if not exist
            int authorId = getOrCreateAuthorId(doc.getAuthor().getName());
            doc.setAuthorId(authorId);

            //get or create publisher id then insert if not exist
            int publisherId = getOrCreatePublisherId(doc.getPublisher().getName());
            doc.setPublisherId(publisherId);

            //get or create category id then insert if not exist
            int categoryId = getOrCreateCategoryId(doc.getCategory().getName());
            doc.setCategoryId(categoryId);


            //sql query to insert or update a document
            String insertSql = "INSERT INTO documents (title, author_id, publisher_id, isbn, category_id, publication_year, " +
                    "quantity, pages, description, location, preview_link, book_image) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " + // Updated to 12 placeholders
                    "ON DUPLICATE KEY UPDATE " +
                    "title = VALUES(title), " +
                    "author_id = VALUES(author_id), " +
                    "publisher_id = VALUES(publisher_id), " +
                    "category_id = VALUES(category_id), " +
                    "publication_year = VALUES(publication_year), " +
                    "quantity = VALUES(quantity), " +
                    "pages = VALUES(pages), " +
                    "description = VALUES(description), " +
                    "location = VALUES(location), " +
                    "preview_link = VALUES(preview_link), " +
                    "book_image = VALUES(book_image)";

            try (Connection connection = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = connection.prepareStatement(insertSql)) {

                pstmt.setString(1, doc.getTitle());
                pstmt.setInt(2, authorId);  //use authorId
                pstmt.setInt(3, publisherId);  //use publisherId
                pstmt.setString(4, doc.getISBN());
                pstmt.setInt(5, categoryId);  //use categoryId

                if (doc.getPublicationYear() != 0) {
                    pstmt.setInt(6, doc.getPublicationYear());
                } else {
                    pstmt.setNull(6, Types.INTEGER);
                }
                pstmt.setInt(7, doc.getQuantity());
                pstmt.setInt(8, doc.getPage());
                pstmt.setString(9, doc.getDescription());
                pstmt.setString(10, doc.getLocation());
                pstmt.setString(11, doc.getPreviewLink());
                pstmt.setString(12, doc.getImageLink());

                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error inserting document: " + e.getMessage());
            throw e;
        }
    }

    public List<Document> getAll() {
        String sql = "select * from documents";
        List<Document> documents = new ArrayList<Document>();
        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");
                int categoryId = rs.getInt("category_id");
                int authorId = rs.getInt("author_id");
                int publisherId = rs.getInt("publisher_id");
                int publicationYear = rs.getInt("publication_year");
                int quantity = rs.getInt("quantity");
                int page = rs.getInt("pages");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String imageUrl = rs.getString("book_image");
                String previewUrl = rs.getString("preview_link");
                documents.add(new Document(isbn, title, categoryId, authorId, publisherId, publicationYear, quantity, description, location, page, previewUrl, imageUrl));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return documents;
    }


    //get a document by ISBN
    public <U> Document get(U isbn) throws SQLException {
        String selectSql = "SELECT * FROM documents WHERE isbn = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectSql)) {

            ps.setString(1, (String) isbn);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                Document doc = new Document();
                doc.setISBN(resultSet.getString("isbn"));
                doc.setTitle(resultSet.getString("title"));
                doc.setAuthorId(resultSet.getInt("author_id"));
                doc.setPublisherId(resultSet.getInt("publisher_id"));
                doc.setCategoryId(resultSet.getInt("category_id"));
                doc.setPublicationYear(resultSet.getInt("publication_year"));
                doc.setQuantity(resultSet.getInt("quantity"));
                doc.setPage(resultSet.getInt("pages"));  // Get number_of_pages
                doc.setDescription(resultSet.getString("description"));
                doc.setLocation(resultSet.getString("location"));
                doc.setPreviewLink(resultSet.getString("preview_link"));
                doc.setImageLink(resultSet.getString("book_image"));  // Get book_image
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
        String sql = "update documents set quantity= quantity + ? where isbn = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            if (status.equals("returned")) ps.setInt(1, 1);
            else if (status.equals("borrowed") || status.equals("lost")) ps.setInt(1, -1);
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
             PreparedStatement ps = connection.prepareStatement(selectSql)) {

            ps.setString(1, authorName.trim());
            ResultSet resultSet = ps.executeQuery();
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
             PreparedStatement ps = connection.prepareStatement(selectSql)) {

            ps.setString(1, publisherName.trim());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("publisher_id");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            //if found duplicate entry,re-fetch the publisherid
            return getOrCreatePublisherId(publisherName);
        }

        //if publisher doesnt exist, insert
        return insertPublisher(publisherName);
    }

    public int getOrCreateCategoryId(String categoryName) throws SQLException {
        String selectSql = "SELECT category_id FROM Categories WHERE name = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectSql)) {

            ps.setString(1, categoryName.trim());
            ResultSet resultSet = ps.executeQuery();
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
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, authorName);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public int insertPublisher(String publisherName) throws SQLException {
        String sql = "INSERT INTO publishers (name) VALUES (?)";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, publisherName);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public int insertCategory(String categoryName) throws SQLException {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, categoryName);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    private boolean checkIfExists(String table, String column, String value) throws SQLException {
        String selectSql = "SELECT 1 FROM " + table + " WHERE " + column + " = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectSql)) {

            ps.setString(1, value);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        }
    }

    public Publisher getPublisher(int id) {
        String sql = "select * from publishers where publisher_id =?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int publisher_id = resultSet.getInt("publisher_id");
                String name = resultSet.getString("name");
                return new Publisher(name, publisher_id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Category getCategory(int id) {
        String sql = "select * from categories where category_id =?" ;
        try (Connection connection= DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int category_id = resultSet.getInt("category_id");
                String category_name = resultSet.getString("category_name");
                return new Category(category_id,category_name);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Author getAuthor(int id) {
        String sql = "select * from authors where author_id =?" ;
        try (Connection connection= DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int author_id = resultSet.getInt("author_id");
                String name = resultSet.getString("name");
                return new Author(author_id,name);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}

