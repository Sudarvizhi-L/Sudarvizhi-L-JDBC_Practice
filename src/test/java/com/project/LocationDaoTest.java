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
        ds.setUser("satha");

        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE location (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    doorno VARCHAR(255),
                    street INT,
                    areaname VARCHAR(255),
                    city VARCHAR(255),
                    district VARCHAR(255),
                    state VARCHAR(255),
                    country VARCHAR(255),
                    pincode VARCHAR(255),
                    latitude DOUBLE,
                    longitude DOUBLE
                )
            """);
        }

        locationDao = new LocationDao(ds);
    }

    @Test
    @Order(1)
    void testSaveLocation() {
        Location location = locationDao.save(new Location(null, "10A", 2, "Indira Nagar", "Chennai", "Chennai", "TN", "India", "600020", 13.01, 80.21));
        assertNotNull(location.id());
        assertEquals("10A", location.doorno());
    }

    @Test
    @Order(2)
    void testUpdateLocation() {
        Location loc = locationDao.save(new Location(null, "12B", 5, "Anna Nagar", "Madurai", "Madurai", "TN", "India", "625001", 9.93, 78.12));
        Location updated = locationDao.save(new Location(loc.id(), "12B", 5, "KK Nagar", "Madurai", "Madurai", "TN", "India", "625001", 9.94, 78.13));

        assertEquals(loc.id(), updated.id());
        assertEquals("KK Nagar", updated.areaname());
    }

    @Test
    @Order(3)
    void testFindById() {
        Location loc = locationDao.save(new Location(null, "23C", 8, "Ashok Nagar", "Coimbatore", "Coimbatore", "TN", "India", "641001", 11.01, 76.96));
        var result = locationDao.findById(loc.id());

        assertTrue(result.isPresent());
        assertEquals("Ashok Nagar", result.get().areaname());
    }

    @Test
    @Order(4)
    void testFindAll() {
        locationDao.save(new Location(null, "40D", 3, "Velachery", "Chennai", "Chennai", "TN", "India", "600042", 12.97, 80.22));
        locationDao.save(new Location(null, "50E", 4, "Tambaram", "Chennai", "Chennai", "TN", "India", "600045", 12.92, 80.11));
        List<Location> locations = locationDao.findAll();

        assertTrue(locations.size() >= 2);
    }

    @Test
    @Order(5)
    void testDeleteById() {
        Location loc = locationDao.save(new Location(null, "60F", 6, "Guindy", "Chennai", "Chennai", "TN", "India", "600032", 13.00, 80.23));
        locationDao.deleteById(loc.id());
        assertTrue(locationDao.findById(loc.id()).isEmpty());
    }

    @Test
    @Order(6)
    void testDeleteAll() {
        locationDao.deleteAll();
        assertEquals(0, locationDao.findAll().size());
    }
}
