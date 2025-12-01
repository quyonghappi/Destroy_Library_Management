package unitTest.fine;
import com.library.models.Fine;
 import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetFineAmountTest {
   
    @Test
    void testTC01() {
        Fine fine = new Fine();
        BigDecimal amount = new BigDecimal("150.00");
        fine.setFineAmount(amount);
        assertEquals(amount, fine.getFineAmount());
    }

    @Test
    void testTC02() {
        Fine fine = new Fine();
        BigDecimal amount = new BigDecimal("0.01");
        fine.setFineAmount(amount);
        assertEquals(amount, fine.getFineAmount());
    }

    @Test
    void testTC03() {
        Fine fine = new Fine();
        assertThrows(IllegalArgumentException.class, () -> fine.setFineAmount(new BigDecimal("0.00")));
    }

    @Test
    void testTC04() {
        Fine fine = new Fine();
        assertThrows(IllegalArgumentException.class, () -> fine.setFineAmount(new BigDecimal("-50.00")));
    }

    @Test
    void testTC05() {
        Fine fine = new Fine();
        assertThrows(IllegalArgumentException.class, () -> fine.setFineAmount(new BigDecimal("-0.01")));
    }

    @Test
    void testTC06() {
        Fine fine = new Fine();
        assertThrows(IllegalArgumentException.class, () -> fine.setFineAmount(null));
    }
}
