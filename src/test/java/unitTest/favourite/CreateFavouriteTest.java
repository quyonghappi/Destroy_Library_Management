package unitTest.favourite;
import com.library.models.Favourite;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CreateFavouriteTest {
    @Test
    void testTC01() {
        assertDoesNotThrow(() -> new Favourite(5, "9781234567890"));
    }

    @Test
    void testTC02() {
        assertThrows(IllegalArgumentException.class, () -> new Favourite(0, "9781234567890"));
    }

    @Test
    void testTC03() {
        assertThrows(IllegalArgumentException.class, () -> new Favourite(-1, "9781234567890"));
    }

    @Test
    void testTC04() {
        assertThrows(IllegalArgumentException.class, () -> new Favourite(5, null));
    }

    @Test
    void testTC05() {
        assertThrows(IllegalArgumentException.class, () -> new Favourite(5, "123456789012"));
    }

    @Test
    void testTC06() {
        assertThrows(IllegalArgumentException.class, () -> new Favourite(5, "12345678901234"));
    }
}