package unitTest;
import org.junit.jupiter.api.Test;
import com.library.utils.Check;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IsValidFullNameTest {

    @Test
    void testIsValidFullName_BVA01() {
        assertTrue(Check.isValidFullName("Nam"));
    }

    @Test
    void testIsValidFullName_BVA02() {
        assertTrue(Check.isValidFullName("Nguyen Thi Ngoc Lan Huong"));
    }

    @Test
    void testIsValidFullName_BVA03() {
        assertTrue(Check.isValidFullName("Tran Mai Anh"));
    }

    @Test
    void testIsValidFullName_BVA04() {
        assertTrue(Check.isValidFullName("Tran Hoang Mai Anh"));
    }

    @Test
    void testIsValidFullName_BVA05() {
        assertTrue(Check.isValidFullName("Mai Anh"));
    }

    @Test
    void testIsValidFullName_BVA06() {
        assertFalse(Check.isValidFullName(""));
    }

    @Test
    void testIsValidFullName_BVA07() {
        assertFalse(Check.isValidFullName("Nguyen Thi Ngoc Lan Huong Hoa"));
    }

    @Test
    void testIsValidFullName_EP01() {
        assertTrue(Check.isValidFullName("Nguyen Van An"));
    }

    @Test
    void testIsValidFullName_EP02() {
        assertFalse(Check.isValidFullName("nguyen van an"));
    }

    @Test
    void testIsValidFullName_EP03() {
        assertFalse(Check.isValidFullName("Nguyen Van An1"));
    }

    @Test
    void testIsValidFullName_EP04() {
        assertFalse(Check.isValidFullName("Nguyen Van @An"));
    }

    @Test
    void testIsValidFullName_EP05() {
        assertFalse(Check.isValidFullName("Nguyen  Van An"));
    }

    @Test
    void testIsValidFullName_EP06() {
        assertFalse(Check.isValidFullName(null));
    }
}