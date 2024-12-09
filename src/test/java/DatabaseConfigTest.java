import com.library.config.DatabaseConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseConfigTest {

    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        //create connection before each test
        connection = DatabaseConfig.getConnection();
    }

    @AfterEach
    public void closeConnection() throws SQLException {
        DatabaseConfig.closeConnection(connection);
    }

    @Test
    public void testGetConnection() {
        //assert that the connection is not null
        assertNotNull(connection, "Connection should not be null");
        System.out.println("Connection established successfully!");
    }
}
