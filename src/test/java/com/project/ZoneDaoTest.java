package com.project;

import com.project.Dao.ZoneDao;
import com.project.model.Zone;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZoneDaoTest {

    private static ZoneDao zoneDao;

    @BeforeAll
    static void setup() throws Exception {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        ds.setUser("satha");

        try (Connection connection = ds.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute("""
                CREATE TABLE zone (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    zoneNumber INT,
                    noOfroom INT,
                    zoneType VARCHAR(255),
                    floorId INT
                )
            """);
        }

        zoneDao = new ZoneDao(ds);
    }

    @Test
    @Order(1)
    void testSaveZone() {
        Zone zone = zoneDao.save(new Zone(null, 101, 5, "Residential", 1));
        assertNotNull(zone.id());
        assertEquals(101, zone.zoneNumber());
    }

    @Test
    @Order(2)
    void testUpdateZone() {
        Zone original = zoneDao.save(new Zone(null, 102, 4, "Commercial", 1));
        Zone updated = zoneDao.save(new Zone(original.id(), 102, 8, "Warehouse", 1));

        assertEquals(original.id(), updated.id());
        assertEquals(8, updated.noOfroom());
        assertEquals("Warehouse", updated.zoneType());
    }

    @Test
    @Order(3)
    void testFindById() {
        Zone zone = zoneDao.save(new Zone(null, 103, 2, "Gym", 1));
        var result = zoneDao.findById(zone.id());

        assertTrue(result.isPresent());
        assertEquals("Gym", result.get().zoneType());
    }

    @Test
    @Order(4)
    void testFindAll() {
        zoneDao.save(new Zone(null, 104, 3, "Storage", 1));
        zoneDao.save(new Zone(null, 105, 6, "Retail", 1));
        List<Zone> zones = zoneDao.findAll();

        assertTrue(zones.size() >= 2);
    }

    @Test
    @Order(5)
    void testCount() {
        long count = zoneDao.count();
        assertTrue(count >= 1);
    }

    @Test
    @Order(6)
    void testDeleteById() {
        Zone zone = zoneDao.save(new Zone(null, 106, 4, "Conference", 1));
        zoneDao.deleteById(zone.id());
        assertTrue(zoneDao.findById(zone.id()).isEmpty());
    }

    @Test
    @Order(7)
    void testDeleteAll() {
        zoneDao.deleteAll();
        assertEquals(0, zoneDao.count());
    }
}
