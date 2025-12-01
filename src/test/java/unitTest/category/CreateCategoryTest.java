package unitTest.category;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.library.models.Category;

public class CreateCategoryTest {

    @Test
    void testTC01() {
        assertDoesNotThrow(() -> new Category(10, "Khoa học"));
    }

    @Test
    void testTC02() {
        assertThrows(IllegalArgumentException.class, () -> new Category(1, "Khoa học"));
    }

    @Test
    void testTC03() {
        assertThrows(IllegalArgumentException.class, () -> new Category(0, "Khoa học"));
    }

    @Test
    void testTC04() {
        assertThrows(IllegalArgumentException.class, () -> new Category(-10, "Khoa học"));
    }

    @Test
    void testTC05() {
        assertThrows(IllegalArgumentException.class, () -> new Category(10, "A"));
    }

    @Test
    void testTC06() {
        assertThrows(IllegalArgumentException.class, () -> new Category(10, ""));
    }

    @Test
    void testTC07() {
        assertThrows(IllegalArgumentException.class, () -> new Category(10, null));
    }
}

