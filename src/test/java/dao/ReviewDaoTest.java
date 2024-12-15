package dao;
import com.library.config.DatabaseConfig;
import com.library.dao.ReviewDao;
import com.library.models.Review;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReviewDaoTest {

    private Connection connection;
    private ReviewDao reviewDao;

    @BeforeAll
    void setUpDatabase() throws Exception {
        connection = DatabaseConfig.getConnection();
        reviewDao = new ReviewDao();

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                INSERT INTO Reviews (user_id, isbn, rating, comment, review_date) VALUES 
                (45, '9781000204476', 4.5, 'Great book!', CURRENT_TIMESTAMP),
                (45, '9781000204476', 3.8, 'Good read.', CURRENT_TIMESTAMP),
                (45, '9781000204476', 5.0, 'Excellent!', CURRENT_TIMESTAMP);
            """);
        }
    }

    @Test
    void testGetAll() {
        List<Review> reviews = reviewDao.getAll();
        assertNotNull(reviews);
        assertEquals(22, reviews.size(), "There should be 3 reviews in the database.");
    }

    @Test
    void testGetById() {
        Review review = reviewDao.get(1);
        assertNotNull(review);
        assertEquals(1, review.getReviewId());
        assertEquals("9798597776415", review.getIsbn());
    }

    @Test
    void testAdd() {
        Review review = new Review();
        review.setUserId(45);
        review.setIsbn("9789350041246");
        review.setRating(4.0);
        review.setComment("Interesting book.");
        review.setReviewDate(LocalDateTime.now());

        reviewDao.add(review);

        List<Review> reviews = reviewDao.getAll();
        assertEquals(22, reviews.size(), "There should be 4 reviews after adding a new one.");
    }

    @Test
    void testGetByIsbn() {
        List<Review> reviews = reviewDao.getByIsbn("9788194401872");
        assertNotNull(reviews);
        assertEquals(3, reviews.size(), "There should be 1 reviews for the ISBN: 9788194401872.");
    }

    @Test
    void testGetByRating() {
        List<Review> reviews = reviewDao.getByRating(4.0, 5.0);
        assertNotNull(reviews);
        assertEquals(12, reviews.size(), "There should be 10 reviews with ratings between 4.0 and 5.0.");
    }
}
