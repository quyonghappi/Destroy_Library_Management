package unitTest.review;
import com.library.models.Review;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SetReviewDateTest {

    @Test
    void testTC01() {
        Review review = new Review();
        LocalDateTime pastDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        review.setReviewDate(pastDate);
        assertEquals(pastDate, review.getReviewDate());
    }

    @Test
    void testTC02() {
        Review review = new Review();
        LocalDateTime futureDate = LocalDateTime.now().plusMinutes(1);
        assertThrows(IllegalArgumentException.class, () -> review.setReviewDate(futureDate));
    }

    @Test
    void testTC03() {
        Review review = new Review();
        assertThrows(IllegalArgumentException.class, () -> review.setReviewDate(null));
    }
}