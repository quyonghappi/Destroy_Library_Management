package unitTest;
import org.junit.jupiter.api.Test;
import com.library.utils.DateFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ToLocalDateTimeTest {

    @Test
    void testTC_T01() {
        assertNull(DateFormat.toLocalDateTime(null));
    }

    @Test
    void testTC_T02() {
        Timestamp timestamp = Timestamp.valueOf("2025-11-30 08:00:00");
        LocalDateTime expected = LocalDateTime.of(2025, 11, 30, 8, 0);
        assertEquals(expected, DateFormat.toLocalDateTime(timestamp));
    }
}