package unitTest.document;
import com.library.models.Document;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateDocumentTest {

    @Test
    void testTC01() {
        assertDoesNotThrow(() -> new Document("9781234567890", "Title", 1, 1, 1, 2024, 10, "Desc", "Loc", 350, "Link", "Link", LocalDateTime.now()));
    }

    @Test
    void testTC02() {
        assertThrows(IllegalArgumentException.class, () -> new Document(null, "Title", 1, 1, 1, 2024, 10, "Desc", "Loc", 350, "Link", "Link", LocalDateTime.now()));
    }

    @Test
    void testTC03() {
        assertThrows(IllegalArgumentException.class, () -> new Document("123456", "Title", 1, 1, 1, 2024, 10, "Desc", "Loc", 350, "Link", "Link", LocalDateTime.now()));
    }

    @Test
    void testTC04() {
        assertThrows(IllegalArgumentException.class, () -> new Document("12345678901234", "Title", 1, 1, 1, 2024, 10, "Desc", "Loc", 350, "Link", "Link", LocalDateTime.now()));
    }

    @Test
    void testTC05() {
        assertThrows(IllegalArgumentException.class, () -> new Document("9781234567890", "Title", 1, 1, 1, 2026, 10, "Desc", "Loc", 350, "Link", "Link", LocalDateTime.now()));
    }

    @Test
    void testTC06() {
        assertThrows(IllegalArgumentException.class, () -> new Document("9781234567890", "Title", 1, 1, 1, 2024, 10, "Desc", "Loc", 0, "Link", "Link", LocalDateTime.now()));
    }

    @Test
    void testTC07() {
        assertThrows(IllegalArgumentException.class, () -> new Document("9781234567890", "Title", 1, 1, 1, 2024, -5, "Desc", "Loc", 350, "Link", "Link", LocalDateTime.now()));
    }

    @Test
    void testTC08() {
        assertThrows(IllegalArgumentException.class, () -> new Document("123456789012", "Title", 1, 1, 1, 2024, 10, "Desc", "Loc", 300, "Link", "Link", LocalDateTime.now()));
    }

    @Test
    void testTC09() {
        assertThrows(IllegalArgumentException.class, () -> new Document("12345678901234", "Title", 1, 1, 1, 2024, 10, "Desc", "Loc", 300, "Link", "Link", LocalDateTime.now()));
    }

    @Test
    void testTC10() {
        assertDoesNotThrow(() -> new Document("9781234567890", "Title", 1, 1, 1, 2025, 10, "Desc", "Loc", 300, "Link", "Link", LocalDateTime.now()));
    }

    @Test
    void testTC11() {
        assertThrows(IllegalArgumentException.class, () -> new Document("9781234567890", "Title", 1, 1, 1, 2026, 10, "Desc", "Loc", 300, "Link", "Link", LocalDateTime.now()));
    }

    @Test
    void testTC12() {
        assertDoesNotThrow(() -> new Document("9781234567890", "Title", 1, 1, 1, 2025, 10, "Desc", "Loc", 1, "Link", "Link", LocalDateTime.now()));
    }

    @Test
    void testTC13() {
        assertThrows(IllegalArgumentException.class, () -> new Document("9781234567890", "Title", 1, 1, 1, 2025, 10, "Desc", "Loc", 0, "Link", "Link", LocalDateTime.now()));
    }

    @Test
    void testTC14() {
        assertDoesNotThrow(() -> new Document("9781234567890", "Title", 1, 1, 1, 2025, 0, "Desc", "Loc", 300, "Link", "Link", LocalDateTime.now()));
    }

    @Test
    void testTC15() {
        assertThrows(IllegalArgumentException.class, () -> new Document("9781234567890", "Title", 1, 1, 1, 2025, -1, "Desc", "Loc", 300, "Link", "Link", LocalDateTime.now()));
    }
}
