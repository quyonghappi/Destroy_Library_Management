package unitTest.category;
import com.library.models.Category;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SetNameTest {

    @Test
    void testTC01() {
        Category category = new Category(10, "Khoa học");
        category.setName("Văn học");
        assertEquals("Văn học", category.getName());
    }

    @Test
    void testTC02() {
        Category category = new Category(10, "Khoa học");
        assertThrows(IllegalArgumentException.class, () -> category.setName("A"));
    }

    @Test
    void testTC03() {
        Category category = new Category(10, "Khoa học");
        assertThrows(IllegalArgumentException.class, () -> category.setName(""));
    }

    @Test
    void testTC04() {
        Category category = new Category(10, "Khoa học");
        assertThrows(IllegalArgumentException.class, () -> category.setName(null));
    }
}
