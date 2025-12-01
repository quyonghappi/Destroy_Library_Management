package unitTest.author;
import com.library.models.Author;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SetNameTest {

    @Test
    void testTC01() {
        Author author = new Author(1, "Default");
        author.setName("Victor");
        assertEquals("Victor", author.getName());
    }

    @Test
    void testTC02() {
        Author author = new Author(1, "Default");
        assertThrows(IllegalArgumentException.class, () -> author.setName(""));
    }
}