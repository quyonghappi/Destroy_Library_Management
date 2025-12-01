package unitTest;
import org.junit.jupiter.api.Test;
import com.library.utils.DateFormat;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ToSqlTimestampTest {

    @Test
    void testTC_T01() {
        assertNull(DateFormat.toSqlTimestamp(null));
    }

    @Test
    void testTC_T02() {
        LocalDateTime localDateTime = LocalDateTime.of(2025, 11, 30, 10, 30, 0);
        Timestamp expected = Timestamp.valueOf("2025-11-30 10:30:00");
        assertEquals(expected, DateFormat.toSqlTimestamp(localDateTime));
    }
}