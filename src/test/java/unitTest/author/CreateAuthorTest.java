package unitTest.author;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.library.models.Author;

public class CreateAuthorTest {

    @Test
    void testTC01() {
        Author author = new Author(1, "To Hoai");
        assertEquals(1, author.getAuthorId());
    }

    @Test
    void testTC02() {
        assertThrows(IllegalArgumentException.class, () -> new Author(0, "To Hoai"));
    }

    @Test
    void testTC03() {
        assertThrows(IllegalArgumentException.class, () -> new Author(1000000, "To Hoai"));
    }

    @Test
    void testTC04() {
        assertThrows(IllegalArgumentException.class, () -> new Author(1000001, "To Hoai"));
    }

    @Test
    void testTC05() {
        assertThrows(IllegalArgumentException.class, () -> new Author(100, null));
    }

    @Test
    void testTC06() {
        assertThrows(IllegalArgumentException.class, () -> new Author(100, ""));
    }

    @Test
    void testTC07() {
        assertThrows(IllegalArgumentException.class, () -> new Author(100, "Nam Cao"));
    }
}
