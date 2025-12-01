package unitTest;
import com.library.utils.FilterPopup;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class SetFilterSelectionListenerTest {

    // Khởi tạo JavaFX Toolkit một lần duy nhất để tránh lỗi "Toolkit not initialized"
    @BeforeAll
    static void initJfx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit đã được khởi tạo trước đó, bỏ qua lỗi
        }
    }

    // Reset trạng thái selectedItem về null trước mỗi test case
    @BeforeEach
    void resetState() throws Exception {
        Field field = FilterPopup.class.getDeclaredField("selectedItem");
        field.setAccessible(true);
        field.set(null, null);
    }

    @Test
    void testTC01() throws Exception {
        // Arrange
        AtomicReference<String> capturedValue = new AtomicReference<>();
        Consumer<String> validListener = capturedValue::set;
        
        // Đăng ký listener
        FilterPopup.setFilterSelectionListener(validListener);

        // Access private static listView via Reflection
        Field listField = FilterPopup.class.getDeclaredField("listView");
        listField.setAccessible(true);
        ListView<String> listView = (ListView<String>) listField.get(null);

        // Giả lập người dùng chọn "Author" và click chuột
        Platform.runLater(() -> {
            listView.getSelectionModel().select("Author");
            // Kích hoạt thủ công sự kiện OnMouseClicked
            if (listView.getOnMouseClicked() != null) {
                listView.getOnMouseClicked().handle(null); // Truyền null vì code không dùng tham số event
            }
        });
        
        Thread.sleep(100);

        // Assert
        assertEquals("Author", FilterPopup.getSelectedItem());
        assertEquals("Author", capturedValue.get());
    }

    @Test
    void testTC02() throws Exception {
        // Arrange
        AtomicReference<String> capturedValue = new AtomicReference<>();
        Consumer<String> validListener = capturedValue::set;
        
        FilterPopup.setFilterSelectionListener(validListener);

        Field listField = FilterPopup.class.getDeclaredField("listView");
        listField.setAccessible(true);
        ListView<String> listView = (ListView<String>) listField.get(null);

        // Giả lập không chọn gì cả (clear selection) và click
        Platform.runLater(() -> {
            listView.getSelectionModel().clearSelection();
            if (listView.getOnMouseClicked() != null) {
                listView.getOnMouseClicked().handle(null);
            }
        });

        Thread.sleep(100);

        // Assert
        assertNull(FilterPopup.getSelectedItem());
        assertNull(capturedValue.get());
    }

    @Test
    void testTC03() throws Exception {
        // Arrange
        // Listener bị null
        FilterPopup.setFilterSelectionListener(null);

        Field listField = FilterPopup.class.getDeclaredField("listView");
        listField.setAccessible(true);
        ListView<String> listView = (ListView<String>) listField.get(null);

        // Giả lập chọn "ISBN" nhưng listener null
        Platform.runLater(() -> {
            listView.getSelectionModel().select("ISBN");
            if (listView.getOnMouseClicked() != null) {
                listView.getOnMouseClicked().handle(null);
            }
        });

        Thread.sleep(100);

        // Assert
        // selectedItem không được cập nhật vì điều kiện (listener != null) trả về false
        assertNull(FilterPopup.getSelectedItem());
    }
}