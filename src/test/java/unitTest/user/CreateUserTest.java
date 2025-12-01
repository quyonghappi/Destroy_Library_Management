package unitTest.user;
import com.library.models.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CreateUserTest {

    @Test
    void testTC01() {
        assertDoesNotThrow(() -> new User("Nguyen Van A", "user1", "valid@gmail.com", "pass123"));
    }

    @Test
    void testTC02() {
        assertThrows(IllegalArgumentException.class, () -> new User("", "user1", "valid@gmail.com", "pass123"));
    }

    @Test
    void testTC03() {
        assertThrows(IllegalArgumentException.class, () -> new User(null, "user1", "valid@gmail.com", "pass123"));
    }

    @Test
    void testTC04() {
        assertThrows(IllegalArgumentException.class, () -> new User("Nguyen Van A", "", "valid@gmail.com", "pass123"));
    }

    @Test
    void testTC05() {
        assertThrows(IllegalArgumentException.class, () -> new User("Nguyen Van A", null, "valid@gmail.com", "pass123"));
    }

    @Test
    void testTC06() {
        assertThrows(IllegalArgumentException.class, () -> new User("Nguyen Van A", "user1", "validgmail.com", "pass123"));
    }

    @Test
    void testTC07() {
        assertThrows(IllegalArgumentException.class, () -> new User("Nguyen Van A", "user1", "valid@yahoo.com", "pass123"));
    }

    @Test
    void testTC08() {
        assertThrows(IllegalArgumentException.class, () -> new User("Nguyen Van A", "user1", "user*@gmail.com", "pass123"));
    }

    @Test
    void testTC09() {
        assertThrows(IllegalArgumentException.class, () -> new User("Nguyen Van A", "user1", "", "pass123"));
    }

    @Test
    void testTC10() {
        assertThrows(IllegalArgumentException.class, () -> new User("Nguyen Van A", "user1", "valid@gmail.com", ""));
    }

    @Test
    void testTC11() {
        assertThrows(IllegalArgumentException.class, () -> new User("Nguyen Van A", "user1", "valid@gmail.com", null));
    }
}