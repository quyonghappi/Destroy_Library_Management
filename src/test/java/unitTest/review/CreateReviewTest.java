package unitTest.review;
import com.library.models.Review;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateReviewTest {

    @Test
    void testTC01() {
        assertDoesNotThrow(() -> new Review(1, 100, "9781111111111", 5.0, "Sách hay", LocalDateTime.of(2025, 12, 1, 0, 0)));
    }

    @Test
    void testTC02() {
        assertDoesNotThrow(() -> new Review(1, 100, "9781111111111", 1.0, "Sách hay", LocalDateTime.of(2025, 12, 1, 0, 0)));
    }

    @Test
    void testTC03() {
        assertDoesNotThrow(() -> new Review(1, 100, "9781111111111", 5.0, "Sách hay", LocalDateTime.of(2025, 12, 1, 0, 0)));
    }

    @Test
    void testTC04() {
        assertThrows(IllegalArgumentException.class, () -> new Review(1, 100, "9781111111111", 0.9, "Sách hay", LocalDateTime.of(2025, 12, 1, 0, 0)));
    }

    @Test
    void testTC05() {
        assertThrows(IllegalArgumentException.class, () -> new Review(1, 100, "9781111111111", 5.1, "Sách hay", LocalDateTime.of(2025, 12, 1, 0, 0)));
    }

    @Test
    void testTC06() {
        assertDoesNotThrow(() -> new Review(1, 100, "9781111111111", 5.0, "Sách hay", LocalDateTime.now()));
    }

    @Test
    void testTC07() {
        assertThrows(IllegalArgumentException.class, () -> new Review(1, 100, "9781111111111", 5.0, "Sách hay", LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testTC08() {
        assertThrows(IllegalArgumentException.class, () -> new Review(1, 100, "9781111111111", 5.0, "Sách hay", null));
    }

    @Test
    void testTC09() {
        assertDoesNotThrow(() -> new Review(1, 100, "9781111111111", 5.0, "", LocalDateTime.of(2025, 12, 1, 0, 0)));
    }

    @Test
    void testTC10() {
        assertThrows(IllegalArgumentException.class, () -> new Review(1, 100, "9781111111111", 5.0, null, LocalDateTime.of(2025, 12, 1, 0, 0)));
    }

    @Test
    void testTC11() {
        assertThrows(IllegalArgumentException.class, () -> new Review(1, 100, null, 5.0, "Sách hay", LocalDateTime.of(2025, 12, 1, 0, 0)));
    }

    @Test
    void testTC12() {
        assertThrows(IllegalArgumentException.class, () -> new Review(1, 100, "", 5.0, "Sách hay", LocalDateTime.of(2025, 12, 1, 0, 0)));
    }

    @Test
    void testTC13() {
        assertThrows(IllegalArgumentException.class, () -> new Review(1, 0, "9781111111111", 5.0, "Sách hay", LocalDateTime.of(2025, 12, 1, 0, 0)));
    }

    @Test
    void testTC14() {
        assertThrows(IllegalArgumentException.class, () -> new Review(0, 100, "9781111111111", 5.0, "Sách hay", LocalDateTime.of(2025, 12, 1, 0, 0)));
    }
}