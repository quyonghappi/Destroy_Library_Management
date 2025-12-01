package unitTest;
import org.junit.jupiter.api.Test;
import com.library.utils.DateFormat;

import java.time.LocalDate;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ToLocalDateTest {

    @Test
    void testTC_D01() {
        assertNull(DateFormat.toLocalDate(null));
    }

    @Test
    void testTC_D02() {
        Date sqlDate = Date.valueOf("2025-11-30");
        LocalDate expected = LocalDate.of(2025, 11, 30);
        assertEquals(expected, DateFormat.toLocalDate(sqlDate));
    }
}