package unitTest.publisher;
import com.library.models.Publisher;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SetNameTest {

    @Test
    void testTC01() {
        Publisher publisher = new Publisher("Old Name", 1);
        publisher.setName("NXB Trẻ");
        assertEquals("NXB Trẻ", publisher.getName());
    }

    @Test
    void testTC02() {
        Publisher publisher = new Publisher("Old Name", 1);
        assertThrows(IllegalArgumentException.class, () -> publisher.setName(null));
    }
}


