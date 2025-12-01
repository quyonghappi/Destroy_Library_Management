package unitTest.favourite;
import com.library.models.Favourite;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetIsbnTest {
    
    @Test
    void testTC01() {
        Favourite favourite = new Favourite();
        favourite.setIsbn("9781234567890");
        assertEquals("9781234567890", favourite.getIsbn());
    }

    @Test
    void testTC02() {
        Favourite favourite = new Favourite();
        assertThrows(IllegalArgumentException.class, () -> favourite.setIsbn(null));
    }

    @Test
    void testTC03() {
        Favourite favourite = new Favourite();
        assertThrows(IllegalArgumentException.class, () -> favourite.setIsbn(""));
    }

    @Test
    void testTC04() {
        Favourite favourite = new Favourite();
        assertThrows(IllegalArgumentException.class, () -> favourite.setIsbn("123456789012"));
    }

    @Test
    void testTC05() {
        Favourite favourite = new Favourite();
        assertThrows(IllegalArgumentException.class, () -> favourite.setIsbn("12345678901234"));
    }
}