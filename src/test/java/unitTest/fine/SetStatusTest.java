package unitTest.fine;
import com.library.models.Fine;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetStatusTest {

    @Test
    void testTC01() {
        Fine fine = new Fine();
        fine.setStatus("UNPAID");
        assertEquals("UNPAID", fine.getStatus());
    }

    @Test
    void testTC02() {
        Fine fine = new Fine();
        assertThrows(IllegalArgumentException.class, () -> fine.setStatus(" "));
    }
}
