package unitTest.borrowingrecord;
import com.library.models.BorrowingRecord;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateBorrowingRecordTest {

    @Test
    void testTC01() {
        assertDoesNotThrow(() -> new BorrowingRecord(100, 50, "9781234567890", LocalDateTime.of(2025, 12, 1, 0, 0), LocalDateTime.of(2025, 12, 15, 0, 0), "Borrowed"));
    }

    @Test
    void testTC02() {
        assertDoesNotThrow(() -> new BorrowingRecord(100, 50, "9781234567890", LocalDateTime.of(2025, 12, 1, 0, 0), LocalDateTime.of(2025, 12, 15, 0, 0), "Returned"));
    }

    @Test
    void testTC03() {
        assertDoesNotThrow(() -> new BorrowingRecord(100, 50, "9781234567890", LocalDateTime.of(2025, 12, 1, 0, 0), LocalDateTime.of(2025, 12, 15, 0, 0), "Lost"));
    }

    @Test
    void testTC04() {
        assertThrows(IllegalArgumentException.class, () -> new BorrowingRecord(0, 50, "9781234567890", LocalDateTime.of(2025, 12, 1, 0, 0), LocalDateTime.of(2025, 12, 15, 0, 0), "Borrowed"));
    }

    @Test
    void testTC05() {
        assertThrows(IllegalArgumentException.class, () -> new BorrowingRecord(-1, 50, "9781234567890", LocalDateTime.of(2025, 12, 1, 0, 0), LocalDateTime.of(2025, 12, 15, 0, 0), "Borrowed"));
    }

    @Test
    void testTC06() {
        assertThrows(IllegalArgumentException.class, () -> new BorrowingRecord(100, 0, "9781234567890", LocalDateTime.of(2025, 12, 1, 0, 0), LocalDateTime.of(2025, 12, 15, 0, 0), "Borrowed"));
    }

    @Test
    void testTC07() {
        assertThrows(IllegalArgumentException.class, () -> new BorrowingRecord(100, 50, null, LocalDateTime.of(2025, 12, 1, 0, 0), LocalDateTime.of(2025, 12, 15, 0, 0), "Borrowed"));
    }

    @Test
    void testTC08() {
        assertThrows(IllegalArgumentException.class, () -> new BorrowingRecord(100, 50, "123456789012", LocalDateTime.of(2025, 12, 1, 0, 0), LocalDateTime.of(2025, 12, 15, 0, 0), "Borrowed"));
    }

    @Test
    void testTC09() {
        assertThrows(IllegalArgumentException.class, () -> new BorrowingRecord(100, 50, "12345678901234", LocalDateTime.of(2025, 12, 1, 0, 0), LocalDateTime.of(2025, 12, 15, 0, 0), "Borrowed"));
    }

    @Test
    void testTC10() {
        assertThrows(IllegalArgumentException.class, () -> new BorrowingRecord(100, 50, "9781234567890", null, LocalDateTime.of(2025, 12, 15, 0, 0), "Borrowed"));
    }

    @Test
    void testTC11() {
        assertThrows(IllegalArgumentException.class, () -> new BorrowingRecord(100, 50, "9781234567890", LocalDateTime.of(2025, 12, 1, 0, 0), null, "Borrowed"));
    }

    @Test
    void testTC12() {
        assertThrows(IllegalArgumentException.class, () -> new BorrowingRecord(100, 50, "9781234567890", LocalDateTime.of(2025, 12, 1, 0, 0), LocalDateTime.of(2025, 12, 14, 0, 0), "Borrowed"));
    }

    @Test
    void testTC13() {
        assertThrows(IllegalArgumentException.class, () -> new BorrowingRecord(100, 50, "9781234567890", LocalDateTime.of(2025, 12, 1, 0, 0), LocalDateTime.of(2025, 12, 16, 0, 0), "Borrowed"));
    }

    @Test
    void testTC14() {
        assertThrows(IllegalArgumentException.class, () -> new BorrowingRecord(100, 50, "9781234567890", LocalDateTime.of(2025, 12, 1, 0, 0), LocalDateTime.of(2025, 12, 15, 0, 0), null));
    }

    @Test
    void testTC15() {
        assertThrows(IllegalArgumentException.class, () -> new BorrowingRecord(100, 50, "9781234567890", LocalDateTime.of(2025, 12, 1, 0, 0), LocalDateTime.of(2025, 12, 15, 0, 0), "Unknown"));
    }

    @Test
    void testTC16() {
        assertThrows(IllegalArgumentException.class, () -> new BorrowingRecord(100, 50, "9781234567890", LocalDateTime.of(2025, 12, 1, 0, 0), LocalDateTime.of(2025, 12, 15, 0, 0), ""));
    }
    
}
