package com.project;

import com.project.Dao.LocationDao;
import com.project.model.Location;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LocationDaoTest {

    private static LocationDao locationDao;

    @BeforeAll
    static void setup() throws Exception {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        ds.setUser("sudar");

        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE location (
                    location_id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255),
                    address VARCHAR(255),
                    org_id INT
                )
            """);
        }

        locationDao = new LocationDao(ds);
    }

    @Test
    @Order(1)
    void testSaveLocation() {
        Location location = locationDao.save(new Location(null, "Madurai", "Othakadai", 1));
        assertNotNull(location.location_id());
        assertEquals("Madurai", location.name());
    }

    @Test
    @Order(2)
    void testUpdateLocation() {
        Location loc = locationDao.save(new Location(null, "Madurai", "Othakadai", 1));
        Location updated = locationDao.save(new Location(loc.location_id(), "Madurai", "Anna Nagar", 2));

        assertEquals(loc.location_id(), updated.location_id());
        assertEquals("Anna Nagar", updated.address());
    }

    @Test
    @Order(3)
    void testFindById() {
        Location loc = locationDao.save(new Location(null, "23C", "Ashok Nagar", 2));
        var result = locationDao.findById(loc.location_id());

        assertTrue(result.isPresent());
        assertEquals("Ashok Nagar", result.get().address());
    }

    @Test
    @Order(4)
    void testFindAll() {
        locationDao.save(new Location(null, "Chennai", "Velachery", 3));
        locationDao.save(new Location(null, "Chennai", "Tambaram", 3));
        List<Location> locations = locationDao.findAll();

        assertTrue(locations.size() >= 2);
    }

    @Test
    @Order(5)
    void testDeleteById() {
        Location loc = locationDao.save(new Location(null, "Chennai", "Guindy", 2));
        locationDao.deleteById(loc.location_id());
        assertTrue(locationDao.findById(loc.location_id()).isEmpty());
    }

    @Test
    @Order(6)
    void testDeleteAll() {
        locationDao.deleteAll();
        assertEquals(0, locationDao.findAll().size());
    }
}
