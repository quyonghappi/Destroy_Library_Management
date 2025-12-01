package unitTest.reservation;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.library.models.Reservation;

public class SetStatusTest {
    @Test
    void testTC01() {
        Reservation reservation = new Reservation(100, "9781234567890", LocalDateTime.now(), "active");
        reservation.setStatus("fulfilled");
        assertEquals("fulfilled", reservation.getStatus());
    }

    @Test
    void testTC02() {
        Reservation reservation = new Reservation(100, "9781234567890", LocalDateTime.now(), "active");
        assertThrows(IllegalArgumentException.class, () -> reservation.setStatus(""));
    }
}


