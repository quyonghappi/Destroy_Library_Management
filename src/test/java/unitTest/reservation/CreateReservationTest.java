package unitTest.reservation;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.library.models.Reservation;

public class CreateReservationTest {

    @Test
    void testTC01() {
        assertDoesNotThrow(() -> new Reservation(100, "9781234567890", LocalDateTime.of(2025, 12, 2, 10, 0), "active"));
    }

    @Test
    void testTC02() {
        assertDoesNotThrow(() -> new Reservation(1, "9781234567890", LocalDateTime.of(2025, 12, 2, 10, 0), "active"));
    }

    @Test
    void testTC03() {
        assertThrows(IllegalArgumentException.class, () -> new Reservation(0, "9781234567890", LocalDateTime.of(2025, 12, 2, 10, 0), "active"));
    }

    @Test
    void testTC04() {
        assertThrows(IllegalArgumentException.class, () -> new Reservation(-5, "9781234567890", LocalDateTime.of(2025, 12, 2, 10, 0), "active"));
    }

    @Test
    void testTC05() {
        assertThrows(IllegalArgumentException.class, () -> new Reservation(100, null, LocalDateTime.of(2025, 12, 2, 10, 0), "active"));
    }

    @Test
    void testTC06() {
        assertThrows(IllegalArgumentException.class, () -> new Reservation(100, "", LocalDateTime.of(2025, 12, 2, 10, 0), "active"));
    }

    @Test
    void testTC07() {
        assertThrows(IllegalArgumentException.class, () -> new Reservation(100, "123456789012", LocalDateTime.of(2025, 12, 2, 10, 0), "active"));
    }

    @Test
    void testTC08() {
        assertThrows(IllegalArgumentException.class, () -> new Reservation(100, "12345678901234", LocalDateTime.of(2025, 12, 2, 10, 0), "active"));
    }

    @Test
    void testTC09() {
        assertDoesNotThrow(() -> new Reservation(100, "9781234567890", LocalDateTime.now(), "active"));
    }

    @Test
    void testTC10() {
        assertThrows(IllegalArgumentException.class, () -> new Reservation(100, "9781234567890", LocalDateTime.now().minusSeconds(1), "active"));
    }

    @Test
    void testTC11() {
        assertThrows(IllegalArgumentException.class, () -> new Reservation(100, "9781234567890", LocalDateTime.of(2024, 1, 1, 0, 0), "active"));
    }

    @Test
    void testTC12() {
        assertThrows(IllegalArgumentException.class, () -> new Reservation(100, "9781234567890", LocalDateTime.of(2025, 12, 2, 10, 0), "fulfilled"));
    }

    @Test
    void testTC13() {
        assertThrows(IllegalArgumentException.class, () -> new Reservation(100, "9781234567890", LocalDateTime.of(2025, 12, 2, 10, 0), "canceled"));
    }

    @Test
    void testTC14() {
        assertThrows(IllegalArgumentException.class, () -> new Reservation(100, "9781234567890", LocalDateTime.of(2025, 12, 2, 10, 0), "pending"));
    }

    @Test
    void testTC15() {
        assertThrows(IllegalArgumentException.class, () -> new Reservation(100, "9781234567890", LocalDateTime.of(2025, 12, 2, 10, 0), null));
    }
}