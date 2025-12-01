package unitTest;
import org.junit.jupiter.api.Test;

import com.library.QrCode.BookQR;
import javafx.scene.image.ImageView;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CreateQRCodeTest {

    @Test
    void testTC01() {
        ImageView result = BookQR.createQRCode("9783161484100");
        
        assertNotNull(result);
        assertEquals(200, result.getFitWidth());
        assertEquals(200, result.getFitHeight());
    }

    @Test
    void testTC02() {
        assertNull(BookQR.createQRCode(null));
    }
}