package unitTest;
import org.junit.jupiter.api.Test;
import com.library.utils.Check;
import javafx.application.Platform;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;

class ValidateInputTest {

    @BeforeAll
    static void initJfx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit đã được khởi tạo trước đó, bỏ qua lỗi này
        }
    }

    @Test
    void testTC01() {
        assertTrue(Check.validateInput("admin", "123456"));
    }

    @Test
    void testTC02() {
        assertFalse(Check.validateInput("", "123456"));
    }

    @Test
    void testTC03() {
        assertFalse(Check.validateInput("admin", ""));
    }

    @Test
    void testTC04() {
        assertFalse(Check.validateInput("", ""));
    }
}