package dao;
import com.library.config.DatabaseConfig;
import com.library.dao.DocumentDao;
import com.library.models.Document;
import com.library.utils.DateFormat;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DocumentDaoTest {

    private Connection connection;
    private DocumentDao dao = DocumentDao.getInstance();

    @BeforeAll
    public void setUpDatabase() throws Exception {
        connection = DatabaseConfig.getConnection();
        try (Statement stmt = connection.createStatement()) {
            //create table
            stmt.execute("""
                create table if not exists documents (
                    title VARCHAR(255) NOT NULL,
                    author_id INT NOT NULL,
                    publisher_id INT NOT NULL,
                    isbn VARCHAR(13) UNIQUE NOT NULL,
                    category_id INT NOT NULL,
                    publication_year INT CHECK (publication_year BETWEEN 1900 AND 2024),
                    quantity INT NOT NULL,
                    pages int not null,
                    description TEXT,
                    location VARCHAR(255) NOT NULL,
                    preview_link TEXT,
                    book_image TEXT,
                    added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (author_id) REFERENCES Authors(author_id) ON DELETE  RESTRICT ON UPDATE CASCADE,
                    FOREIGN KEY (publisher_id) REFERENCES Publishers(publisher_id) ON DELETE  RESTRICT ON UPDATE CASCADE,
                    FOREIGN KEY (category_id) REFERENCES Categories(category_id) ON DELETE  RESTRICT ON UPDATE CASCADE
                );
            """);

            //insert sample data
            stmt.execute("""
                insert into documents (isbn, title, author_id, publisher_id, category_id, 
                                       publication_year, quantity, pages, description, 
                                       location, preview_link, book_image, added_date)
                VALUES 
                    ('1234567890124', 'Book 1', 1, 1, 1, 2000, 5, 200, 'description 1', 'library A', 
                     'http://example.com/preview1', 'http://example.com/image1', CURRENT_TIMESTAMP),
                    ('9876543210989', 'Book 2', 2, 2, 2, 2005, 6, 350, 'description 2', 'library B', 
                     'http://example.com/preview2', 'http://example.com/image2', CURRENT_TIMESTAMP);
            """);
        }
    }

//    @AfterAll
//    public void tearDownDatabase() throws Exception {
//        //drop the Documents table and close the connection
//        String sql = "drop table documents;";
//        try (Statement stmt = connection.createStatement()) {
//            stmt.execute(sql);
//        }
//        DatabaseConfig.closeConnection(connection);
//    }

    //768 is the number of books trong db cua manh, sau do + 2 vao thanh 770 do insert
    // tlinh thay bang so cua tlinh de thuyet trinh cho dung nhe
    @Test
    public void testGetAll() {
        List<Document> documents = dao.getAll();
        assertNotNull(documents);
        assertEquals(770, documents.size(), "There should be 768 documents in the database");
    }

    @Test
    public void testSearchByTitle() {
        List<Document> documents = DocumentDao.searchByTitle("java");
        assertNotNull(documents);
        //thay bang so cua tlinh nhe
        assertEquals(52, documents.size(), "There are 52 document with title containing 'java'");
        assertEquals("9780071606301", documents.get(0).getISBN());
    }

    @Test
    public void testSearchByIsbn() {
        List<Document> documents = DocumentDao.searchByIsbn("9780071606301");
        assertNotNull(documents);
        assertEquals(1, documents.size(), "There should be 1 document with the given ISBN: " + "9780071606301");
        assertEquals("Java The Complete Reference, 8th Edition ", documents.get(0).getTitle());
    }

    @Test
    public void testGetAvailableList() {
        List<Document> documents = dao.getAvailableList();
        assertNotNull(documents);
        assertEquals(737, documents.size(), "There are 737 available documents");
        assertEquals("1234567890124", documents.get(0).getISBN());
    }

    @Test
    public void testGetLostList() {
        List<Document> documents = dao.getLostList();
        assertNotNull(documents);
        assertEquals(16, documents.size(), "there are 16 books having lost copies.");
    }

    @Test
    public void testDeleteDocument() {
        Document document1 = new Document("1234567890124", "Book 1", 1, 1, 1, 2000, 5,
                "description 1", "library A", 200,
                "http://example.com/preview1", "http://example.com/image1", LocalDateTime.now());
        Document document2 = new Document("9876543210989", "Book 2", 2, 2, 2, 2005, 6,
                "description 2", "library B", 350,
                "http://example.com/preview2", "http://example.com/image2", LocalDateTime.now());
        dao.delete(document1);
        dao.delete(document2);

        List<Document> documents = dao.getAll();
        assertEquals(768, documents.size(), "There should be 768 document left after deletion.");
        assertEquals("9780070004429", documents.get(0).getISBN());
    }

    @Test
    public void testEditBook() {
        dao.editBook("9780071315159", "com/library", 3);
        List<Document> documents = dao.searchByIsbn("9780071315159");
        assertEquals(1, documents.size());
        Document document = documents.get(0);
        assertEquals(3, document.getQuantity(), "Quantity was updated to 3");
        assertEquals("com/library", document.getLocation(), "Location was updated to library.");
    }
}
