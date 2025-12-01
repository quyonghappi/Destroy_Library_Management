package unitTest;
import org.junit.jupiter.api.Test;
import com.library.utils.DateFormat;
import java.time.LocalDate;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ToSqlDateTest {

    @Test
    void testTC_D01() {
        assertNull(DateFormat.toSqlDate(null));
    }

    @Test
    void testTC_D02() {
        LocalDate localDate = LocalDate.of(2025, 11, 30);
        Date expected = Date.valueOf("2025-11-30");
        assertEquals(expected, DateFormat.toSqlDate(localDate));
    }
}
