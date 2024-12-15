package dao;

import com.library.config.DatabaseConfig;
import com.library.dao.UserDao;
import com.library.models.Document;
import com.library.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest {
    private Connection connection;

    @BeforeAll
    public void setUpDatabase() throws Exception {
        connection = DatabaseConfig.getConnection();
        try (Statement stmt = connection.createStatement()) {
            //create table
            stmt.execute("""
               create table if not exists users (
               user_id INT AUTO_INCREMENT,
               full_name VARCHAR(255) NOT NULL,
               user_name VARCHAR(50) NOT NULL UNIQUE,
               email VARCHAR(255) NOT NULL UNIQUE,
               password_hash VARCHAR(255) NOT NULL,
               user_role ENUM('reader', 'admin') DEFAULT 'reader',
               account_status ENUM('active', 'suspended') DEFAULT 'active',
               join_date DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
               last_login DATETIME,
               account_locked BOOLEAN DEFAULT FALSE,
               PRIMARY KEY(user_id)
                );
            """);

            //insert sample data
            stmt.execute("""
            INSERT INTO users (user_id, full_name, user_name, email, password_hash, user_role, account_status, join_date, last_login, account_locked)
            VALUES
                ('50', 'Fullname User', 'username1', 'user1@gmail.com', 'hash1', 'reader', 'active', '2024-12-15', CURRENT_TIMESTAMP, 0),
                ('51', 'Fullname User 2', 'username2', 'user2@gmail.com', 'hash2', 'reader' , 'active', '2024-12-14',  CURRENT_TIMESTAMP, 0);
            """);
        }
        }

    @Test
    public void TestGetAll() {
        UserDao userDao = new UserDao();
        List<User> userList = userDao.getAll();
        assertNotNull(userList);
        assertEquals(22, userList.size(), "There should be 22 users in the database");
    }

    @Test
    public void TestDelete() throws SQLException {
        UserDao userDao = new UserDao();
        User user1 = new User(50, "Fullname User", "username1", "user1@gmail.com", "hash1", "reader", "active", LocalDate.now());
        User user2 = new User(51, "Fullname User 2", "username2", "user2@gmail.com", "hash2", "reader", "active", LocalDate.now());
        userDao.delete(user1);
        userDao.delete(user2);
    }

    @Test
    public void TestFindUserByName() throws SQLException {
        UserDao userDao = new UserDao();
        User users = UserDao.findUserByName("duong");
        assertNotNull(users);
    }

    @Test
    public void TestSearchByEmail() throws SQLException {
        UserDao userDao = new UserDao();
        List<User> users = UserDao.searchByEmail("duong");
        assertNotNull(users);
        assertEquals(1, users.size(), "There should be 1 user search by email");
    }

    @Test
    public void TestGetUserHasFine() throws SQLException {
        UserDao userDao = new UserDao();
        List<User> users = UserDao.getUserHasFine();
        assertNotNull(users);
        assertEquals(2, users.size(), "There should be 1 user search by email");
    }

}
