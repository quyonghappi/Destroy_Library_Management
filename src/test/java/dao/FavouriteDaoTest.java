package dao;

import com.library.config.DatabaseConfig;
import com.library.dao.FavouriteDao;
import com.library.models.Favourite;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FavouriteDaoTest {

    private Connection connection;

    @BeforeAll
    public void setUpDatabase() throws Exception {
        connection = DatabaseConfig.getConnection();
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                INSERT INTO favouritebooks (user_id, isbn) VALUES 
                (45, '9780593315309'),
                (45, '9780595375974');
            """);
        }
    }


    @Test
    public void testGetById() {
        FavouriteDao favouriteDao = FavouriteDao.getInstance();
        Favourite favourite = favouriteDao.get(35);
        assertNotNull(favourite);
        assertEquals(35, favourite.getFavouriteId());
        assertEquals("9781693541377", favourite.getIsbn());
    }

    @Test
    public void testAdd() {
        FavouriteDao favouriteDao = FavouriteDao.getInstance();
        Favourite favourite = new Favourite(45, "9780698411449");
        favouriteDao.add(favourite);

        List<Favourite> favourites = favouriteDao.getAll();
        assertEquals(4, favourites.size(), "There should be 4 favourite books after adding one.");
    }

    @Test
    public void testGetAll() {
        FavouriteDao favouriteDao = FavouriteDao.getInstance();
        List<Favourite> favourites = favouriteDao.getAll();
        assertNotNull(favourites);
        assertEquals(3, favourites.size(), "There should be 3 favourite books in the database.");
    }

    @Test
    public void testGetByUsername() {
        FavouriteDao favouriteDao = FavouriteDao.getInstance();
        List<Favourite> favourites = favouriteDao.getByUserName("manh");
        assertEquals(4, favourites.size(), "There should be 4 favourite books after adding one.");
    }

    @Test
    public void testDeleteById() {
        FavouriteDao favouriteDao = FavouriteDao.getInstance();
        Favourite favourite = favouriteDao.get(49);
        favouriteDao.delete(favourite);

        List<Favourite> favourites = favouriteDao.getAll();
        assertEquals(3, favourites.size(), "There should be 3 favourite books after deletion.");
    }

}

