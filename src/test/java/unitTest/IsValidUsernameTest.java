package unitTest;
import org.junit.jupiter.api.Test;
import com.library.utils.Check;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IsValidUsernameTest {

    @Test
    void testTC01() {
        assertTrue(Check.isValidUsername("user123"));
    }

    @Test
    void testTC02() {
        assertTrue(Check.isValidUsername("admin_system-01"));
    }

    @Test
    void testTC03() {
        assertFalse(Check.isValidUsername("user name"));
    }

    @Test
    void testTC04() {
        assertFalse(Check.isValidUsername("user@123"));
    }

    @Test
    void testTC05() {
        assertFalse(Check.isValidUsername("tài_khoản"));
    }

    @Test
    void testTC06() {
        assertFalse(Check.isValidUsername(""));
    }
}