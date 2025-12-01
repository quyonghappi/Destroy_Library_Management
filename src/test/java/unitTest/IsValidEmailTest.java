package unitTest;
import com.library.utils.Check;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class IsValidEmailTest {

    @Test
    void testIsValidEmail_TC01() {
        String input = "user123@gmail.com";
        boolean result = Check.isValidEmail(input);
        assertTrue(result, "TC01 Failed: Email Gmail chuẩn phải trả về true");
    }

    @Test
    void testIsValidEmail_TC02() {
        String input = "sinhvien@uet.edu.vn";
        boolean result = Check.isValidEmail(input);
        assertTrue(result, "TC02 Failed: Email đuôi .edu.vn phải trả về true");
    }

    @Test
    void testIsValidEmail_TC03() {
        String input = "";
        boolean result = Check.isValidEmail(input);
        assertFalse(result, "TC03 Failed: Chuỗi rỗng phải trả về false");
    }

    @Test
    void testIsValidEmail_TC04() {
        String input = null;
        boolean result = Check.isValidEmail(input);
        assertFalse(result, "TC04 Failed: Null phải trả về false");
    }

    @Test
    void testIsValidEmail_TC05() {
        String input = "usergmail.com";
        boolean result = Check.isValidEmail(input);
        assertFalse(result, "TC05 Failed: Thiếu @ phải trả về false");
    }

    @Test
    void testIsValidEmail_TC06() {
        String input = "@gmail.com";
        boolean result = Check.isValidEmail(input);
        assertFalse(result, "TC06 Failed: Thiếu prefix phải trả về false");
    }

    @Test
    void testIsValidEmail_TC07() {
        String input = "user@yahoo.com";
        boolean result = Check.isValidEmail(input);
        assertFalse(result, "TC07 Failed: Domain yahoo.com phải trả về false");
    }

    @Test
        void testIsValidEmail_TC08() {
        String input = "student@harvard.edu";
        boolean result = Check.isValidEmail(input);
        assertFalse(result, "TC08 Failed: Đuôi .edu (không có .vn) phải trả về false");
    }

    @Test
    void testIsValidEmail_TC09() {
        String input = "user*name@gmail.com";
        boolean result = Check.isValidEmail(input);
        assertFalse(result, "TC09 Failed: Ký tự * không được phép trong prefix");
    }
}