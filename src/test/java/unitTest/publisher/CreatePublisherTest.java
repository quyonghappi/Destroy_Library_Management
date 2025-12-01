package unitTest.publisher;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.library.models.Publisher;

public class CreatePublisherTest {

    @Test
    void testTC01() {
        assertDoesNotThrow(() -> new Publisher("NXB Kim Đồng", 100));
    }

    @Test
    void testTC02() {
        assertDoesNotThrow(() -> new Publisher("NXB Kim Đồng", 1));
    }

    @Test
    void testTC03() {
        assertThrows(IllegalArgumentException.class, () -> new Publisher("NXB Kim Đồng", 0));
    }

    @Test
    void testTC04() {
        assertThrows(IllegalArgumentException.class, () -> new Publisher("NXB Kim Đồng", -1));
    }

    @Test
    void testTC05() {
        assertDoesNotThrow(() -> new Publisher("A", 100));
    }

    @Test
    void testTC06() {
        assertThrows(IllegalArgumentException.class, () -> new Publisher("", 100));
    }

    @Test
    void testTC07() {
        assertThrows(IllegalArgumentException.class, () -> new Publisher(null, 100));
    }
}
