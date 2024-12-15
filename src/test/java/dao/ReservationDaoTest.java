package dao;

import com.library.config.DatabaseConfig;
import com.library.dao.ReservationDao;
import com.library.models.Reservation;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReservationDaoTest {

    private Connection connection;
    private ReservationDao reservationDao = ReservationDao.getInstance();

    @BeforeAll
    void setUpDatabase() throws Exception {
        connection = DatabaseConfig.getConnection();
        reservationDao = ReservationDao.getInstance();

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                insert into reservations (user_id, isbn, reservation_date, status) VALUES
                (45, '9786047792313', CURRENT_TIMESTAMP, 'active'),
                (45, '9786048666040', CURRENT_TIMESTAMP, 'active'),
                (45, '9786048699079', CURRENT_TIMESTAMP, 'active');
            """);
        }
    }

    @Test
    void testAddReservation() {
        Reservation reservation = new Reservation();
        reservation.setUserId(45);
        reservation.setIsbn("9786048699079");
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setStatus("active");

        reservationDao.add(reservation);

        List<Reservation> reservations = reservationDao.getAll();
        assertEquals(87, reservations.size(), "There should be 87 reservations in the database.");
    }

    @Test
    void testGetAllReservations() {
        List<Reservation> reservations = reservationDao.getAll();
        assertNotNull(reservations);
        assertEquals(86, reservations.size(), "There should be 87 reservations initially.");
    }

    @Test
    void testGetFulfilledReservations() {
        List<Reservation> fulfilledReservations = reservationDao.getFulfilledReservations();
        assertNotNull(fulfilledReservations);
        assertEquals(57, fulfilledReservations.size(), "There should be 56 fulfilled reservation.");
        assertEquals("fulfilled", fulfilledReservations.get(0).getStatus());
    }

    @Test
    void testGetCancelledReservations() {
        List<Reservation> cancelledReservations = reservationDao.getCancelledReservations();
        assertNotNull(cancelledReservations);
        assertEquals(26, cancelledReservations.size(), "There should be 27 cancelled reservation.");
        assertEquals("cancelled", cancelledReservations.get(0).getStatus());
    }

    @Test
    void testUpdateStatus() {
        reservationDao.updateStatus(32, "fulfilled");
        Reservation updatedReservation = reservationDao.get(32);
        assertNotNull(updatedReservation);
        assertEquals("fulfilled", updatedReservation.getStatus(), "The status should be updated to 'fulfilled'.");
    }

    @Test
    void testGetByISBN() {
        int count = reservationDao.getByISBN("9780071606301");
        assertEquals(4, count, "There should be 4 reservation for the specified ISBN.");
    }

    @Test
    void testReservationExists() {
        Reservation reservation = reservationDao.reservationExists("9789401018531", 45);
        assertNotNull(reservation);
        assertEquals("cancelled", reservation.getStatus(), "The reservation status should be cancelled.");
    }

    @Test
    void testGetByUserName() {
        List<Reservation> reservations = reservationDao.getByUserName("manh");
        assertNotNull(reservations);
        assertEquals(62, reservations.size(), "There should be 2 reservations for testuser1.");
    }
}

