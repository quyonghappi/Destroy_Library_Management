package unitTest.favourite;
import com.library.models.Favourite;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetUserIdTest {
    @Test
    void testTC01() {
        Favourite favourite = new Favourite();
        favourite.setUserId(100);
        assertEquals(100, favourite.getUserId());
    }

    @Test
    void testTC02() {
        Favourite favourite = new Favourite();
        favourite.setUserId(1);
        assertEquals(1, favourite.getUserId());
    }

    @Test
    void testTC03() {
        Favourite favourite = new Favourite();
        assertThrows(IllegalArgumentException.class, () -> favourite.setUserId(0));
    }

    @Test
    void testTC04() {
        Favourite favourite = new Favourite();
        assertThrows(IllegalArgumentException.class, () -> favourite.setUserId(-10));
    }
}