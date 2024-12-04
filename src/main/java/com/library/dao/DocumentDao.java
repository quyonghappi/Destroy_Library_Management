package com.library.dao;

import com.library.config.DatabaseConfig;
import com.library.models.Author;
import com.library.models.Category;
import com.library.models.Document;
import com.library.models.Publisher;
import com.library.utils.DateFormat;

import java.sql.*;
import java.time.LocalDateTime;
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
            String insertSql = "INSERT INTO Documents (title, author_id, publisher_id, isbn, category_id, publication_year, " +
                    "quantity, pages, description, location, preview_link, book_image, added_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
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
                pstmt.setTimestamp(13, DateFormat.toSqlTimestamp(LocalDateTime.now()));

                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error inserting document: " + e.getMessage());
            throw e;
        }
    }

    public List<Document> getAll() {
        String sql = "select * from Documents";
        return getRecord(sql);
    }

    //search book by title
    public static List<Document> searchByTitle(String title) {
        List<Document> Documents = new ArrayList<>();
        String sql = "SELECT * FROM Documents";

        // Nếu có từ khóa tìm kiếm, thêm điều kiện vào câu SQL
        if (title != null && !title.trim().isEmpty()) {
            sql += " WHERE title LIKE ?";
        }

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Nếu có từ khóa tìm kiếm, gán giá trị vào placeholder
            if (title != null && !title.trim().isEmpty()) {
                ps.setString(1, "%" + title.trim() + "%");
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Document doc = new Document();
                    doc.setISBN(resultSet.getString("isbn"));
                    doc.setTitle(resultSet.getString("title"));
                    doc.setAuthorId(resultSet.getInt("author_id"));
                    doc.setPublisherId(resultSet.getInt("publisher_id"));
                    doc.setCategoryId(resultSet.getInt("category_id"));
                    doc.setPublicationYear(resultSet.getInt("publication_year"));
                    doc.setQuantity(resultSet.getInt("quantity"));
                    doc.setPage(resultSet.getInt("pages"));
                    doc.setDescription(resultSet.getString("description"));
                    doc.setLocation(resultSet.getString("location"));
                    doc.setPreviewLink(resultSet.getString("preview_link"));
                    doc.setImageLink(resultSet.getString("book_image"));
                    doc.setAddedOn(resultSet.getTimestamp("added_date").toLocalDateTime());
                    Documents.add(doc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Documents;
    }

    public static List<Document> searchByIsbn(String isbn) {
        List<Document> Documents = new ArrayList<>();
        String sql = "SELECT * FROM Documents";

        if (isbn != null && !isbn.trim().isEmpty()) {
            sql += " WHERE isbn LIKE ?";
        }

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + isbn.trim() + "%");

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Document doc = new Document();
                    doc = new Document();
                    doc.setISBN(resultSet.getString("isbn"));
                    doc.setTitle(resultSet.getString("title"));
                    doc.setAuthorId(resultSet.getInt("author_id"));
                    doc.setPublisherId(resultSet.getInt("publisher_id"));
                    doc.setCategoryId(resultSet.getInt("category_id"));
                    doc.setPublicationYear(resultSet.getInt("publication_year"));
                    doc.setQuantity(resultSet.getInt("quantity"));
                    doc.setPage(resultSet.getInt("pages"));
                    doc.setDescription(resultSet.getString("description"));
                    doc.setLocation(resultSet.getString("location"));
                    doc.setPreviewLink(resultSet.getString("preview_link"));
                    doc.setImageLink(resultSet.getString("book_image"));
                    doc.setAddedOn(resultSet.getTimestamp("added_date").toLocalDateTime());
                    Documents.add(doc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Documents;
    }



    public static List<Document> searchByAuthor(String author) throws SQLException {
        List<Document> Documents = new ArrayList<>();
        String sql = """
        SELECT d.*
        FROM Documents d
        JOIN Authors a ON d.author_id = a.author_id
        """;

        if (author != null && !author.trim().isEmpty()) {
            sql += " WHERE a.name LIKE ?";
            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                if (author != null && !author.trim().isEmpty()) {
                    ps.setString(1, "%" + author.trim() + "%");
                }

                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        Document doc = new Document();
                        doc = new Document();
                        doc.setISBN(resultSet.getString("isbn"));
                        doc.setTitle(resultSet.getString("title"));
                        doc.setAuthorId(resultSet.getInt("author_id"));
                        doc.setPublisherId(resultSet.getInt("publisher_id"));
                        doc.setCategoryId(resultSet.getInt("category_id"));
                        doc.setPublicationYear(resultSet.getInt("publication_year"));
                        doc.setQuantity(resultSet.getInt("quantity"));
                        doc.setPage(resultSet.getInt("pages"));
                        doc.setDescription(resultSet.getString("description"));
                        doc.setLocation(resultSet.getString("location"));
                        doc.setPreviewLink(resultSet.getString("preview_link"));
                        doc.setImageLink(resultSet.getString("book_image"));
                        doc.setAddedOn(resultSet.getTimestamp("added_date").toLocalDateTime());
                        Documents.add(doc);
                    }
                }

            }
        }
        return Documents;
    }

    public static List<Document> searchByCategory(String category) throws SQLException {
        List<Document> Documents = new ArrayList<>();
        String sql = """
        SELECT d.*
        FROM Documents d
        JOIN Categories a ON d.category_id = a.category_id
        """;

        if (category != null && !category.trim().isEmpty()) {
            sql += " WHERE a.name LIKE ?";
            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                if (category != null && !category.trim().isEmpty()) {
                    ps.setString(1, "%" + category.trim() + "%");
                }

                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        Document doc = new Document();
                        doc = new Document();
                        doc.setISBN(resultSet.getString("isbn"));
                        doc.setTitle(resultSet.getString("title"));
                        doc.setAuthorId(resultSet.getInt("author_id"));
                        doc.setPublisherId(resultSet.getInt("publisher_id"));
                        doc.setCategoryId(resultSet.getInt("category_id"));
                        doc.setPublicationYear(resultSet.getInt("publication_year"));
                        doc.setQuantity(resultSet.getInt("quantity"));
                        doc.setPage(resultSet.getInt("pages"));
                        doc.setDescription(resultSet.getString("description"));
                        doc.setLocation(resultSet.getString("location"));
                        doc.setPreviewLink(resultSet.getString("preview_link"));
                        doc.setImageLink(resultSet.getString("book_image"));
                        doc.setAddedOn(resultSet.getTimestamp("added_date").toLocalDateTime());

                        Documents.add(doc);
                    }
                }

            }
        }
        return Documents;
    }

    //get list of available books
    public List<Document> getAvailableList() {
        String sql = "select * from Documents where quantity > 0";
        return getRecord(sql);
    }

    //get list of lost books
    public List<Document> getLostList() {
        String sql = "select * from Documents d\n"
                + "join borrowingrecords b on b.isbn=d.isbn\n"
                + "where b.status = 'lost'";
        return getRecord(sql);
    }

    //get recently added book
    public List<Document> getRecentAddedBooks() {
        List<Document> Documents = new ArrayList<>();
        String sql = "SELECT * FROM Documents "
                + "ORDER BY added_date DESC "
                + "LIMIT 55";

        return getRecord(sql);
    }


    private List<Document> getRecord(String sql) {
        List<Document> Documents = new ArrayList<>();
        try (
                Connection conn=DatabaseConfig.getConnection();
                PreparedStatement ps=conn.prepareStatement(sql)
        ) {
            ResultSet rs= ps.executeQuery();
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
                LocalDateTime addedDate = (rs.getTimestamp("added_date").toLocalDateTime());
                Documents.add(new Document(isbn, title, categoryId, authorId, publisherId, publicationYear, quantity, description, location, page, previewUrl, imageUrl,addedDate));
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return Documents;
    }


    //get a document by ISBN
    public <U> Document get(U isbn) {
        String selectSql = "SELECT * FROM Documents WHERE isbn = ?";

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
                doc.setAddedOn(resultSet.getTimestamp("added_date").toLocalDateTime());
                return doc;
            }
        } catch (SQLException e) {
            throw new RuntimeException("can not find document with" + isbn);
        }

        return null;
    }

    //delete doc by its isbn, do not use title cuz books can have same title
    public void delete(Document doc) throws SQLException {
        Connection conn = DatabaseConfig.getConnection();
        String sql = "DELETE FROM Documents WHERE isbn= ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doc.getISBN());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //edit number of book copies and its location
    public void editBook(String isbn, String location, int quantity) {
        String sql = "update Documents set quantity= quantity + ?, location = ? where isbn = ?";
        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setString(2, location);
            ps.setString(3, isbn);
            //execute the update
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
//        finally {
//            conn.close();
//        }
    }

    //when user return book or borrow book, update available copies
    public void updateQuantity(String isbn, String status) {
        String sql = "update Documents set quantity= quantity + ? where isbn = ?";
        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            if (status.equals("returned")) ps.setInt(1, 1);
            else if (status.equals("borrowed") || status.equals("lost")) ps.setInt(1, -1);
            ps.setString(2, isbn);
            //execute the update
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
//        finally {
//            conn.close();
//        }
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

        try {
            return insertAuthor(authorName);
        } catch (SQLIntegrityConstraintViolationException e) {
            // Nếu lỗi trùng lặp xảy ra, tìm lại ID
            return getOrCreateAuthorId(authorName);
        }
    }

    public synchronized int getOrCreatePublisherId(String publisherName) throws SQLException {
        String selectSql = "SELECT publisher_id FROM Publishers WHERE name = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectSql)) {

            ps.setString(1, publisherName.trim());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("publisher_id");
            }
        }

        try {
            return insertPublisher(publisherName);
        } catch (SQLIntegrityConstraintViolationException e) {
            // Nếu lỗi trùng lặp xảy ra, tìm lại ID
            return getOrCreatePublisherId(publisherName);
        }
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
        try {
            return insertCategory(categoryName);
        } catch (SQLIntegrityConstraintViolationException e) {
            // Nếu lỗi trùng lặp xảy ra, tìm lại ID
            return getOrCreateCategoryId(categoryName);
        }
    }

    //insert author, publisher, and category if they do not exist
    public int insertAuthor(String authorName) throws SQLException {
        String sql = "INSERT INTO Authors (name) VALUES (?)";
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
        String sql = "INSERT INTO Publishers (name) VALUES (?)";
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
        String sql = "INSERT INTO Categories (name) VALUES (?)";
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

    public Publisher getPublisher(int id) {
        String sql = "select * from Publishers where publisher_id =?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
        String sql = "select * from Categories where category_id =?" ;
        try (Connection connection= DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int category_id = resultSet.getInt("category_id");
                String category_name = resultSet.getString("name");
                return new Category(category_id,category_name);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Author getAuthor(int id) {
        String sql = "select * from Authors where author_id =?" ;
        try (Connection connection= DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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

    public static Document findByIsbn(String isbn) {
        String sql = "SELECT * FROM Documents";
        Document doc = new Document();
        if (isbn != null && !isbn.trim().isEmpty()) {
            sql += " WHERE isbn LIKE ?";
        }

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + isbn.trim() + "%");

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    doc.setISBN(resultSet.getString("isbn"));
                    doc.setTitle(resultSet.getString("title"));
                    doc.setAuthorId(resultSet.getInt("author_id"));
                    doc.setPublisherId(resultSet.getInt("publisher_id"));
                    doc.setCategoryId(resultSet.getInt("category_id"));
                    doc.setPublicationYear(resultSet.getInt("publication_year"));
                    doc.setQuantity(resultSet.getInt("quantity"));
                    doc.setPage(resultSet.getInt("pages"));
                    doc.setDescription(resultSet.getString("description"));
                    doc.setLocation(resultSet.getString("location"));
                    doc.setPreviewLink(resultSet.getString("preview_link"));
                    doc.setImageLink(resultSet.getString("book_image"));
                    doc.setAddedOn(resultSet.getTimestamp("added_date").toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doc;
    }

}

