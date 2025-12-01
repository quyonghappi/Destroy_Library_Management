package unitTest.review;
import com.library.models.Review;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetRatingTest {

    @Test
    void testTC01() {
        Review review = new Review();
        review.setRating(3.5);
        assertEquals(3.5, review.getRating());
    }

    @Test
    void testTC02() {
        Review review = new Review();
        review.setRating(1.0);
        assertEquals(1.0, review.getRating());
    }

    @Test
    void testTC03() {
        Review review = new Review();
        review.setRating(5.0);
        assertEquals(5.0, review.getRating());
    }

    @Test
    void testTC04() {
        Review review = new Review();
        review.setRating(1.1);
        assertEquals(1.1, review.getRating());
    }

    @Test
    void testTC05() {
        Review review = new Review();
        review.setRating(4.9);
        assertEquals(4.9, review.getRating());
    }

    @Test
    void testTC06() {
        Review review = new Review();
        assertThrows(IllegalArgumentException.class, () -> review.setRating(0.9));
    }

    @Test
    void testTC07() {
        Review review = new Review();
        assertThrows(IllegalArgumentException.class, () -> review.setRating(5.1));
    }

    @Test
    void testTC08() {
        Review review = new Review();
        assertThrows(IllegalArgumentException.class, () -> review.setRating(-1.0));
    }
}