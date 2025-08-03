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
        ds.setUser("sudar");

        try (Connection connection = ds.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute("""
                CREATE TABLE zone (
                    zone_id INT AUTO_INCREMENT PRIMARY KEY,
                    zone_no INT,
                    floor_id INT
                )
            """);
        }

        zoneDao = new ZoneDao(ds);
    }

    @Test
    @Order(1)
    void testSaveZone() {
        Zone zone = zoneDao.save(new Zone(null, 101, 5));
        assertNotNull(zone.zone_id());
        assertEquals(101, zone.zone_no());
    }

    @Test
    @Order(2)
    void testUpdateZone() {
        Zone original = zoneDao.save(new Zone(null, 102, 4));
        Zone updated = zoneDao.save(new Zone(original.zone_id(), 102, 8));

        assertEquals(original.zone_id(), updated.zone_id());
        assertEquals(8, updated.floor_id());
    }

    @Test
    @Order(3)
    void testFindById() {
        Zone zone = zoneDao.save(new Zone(null, 103, 2));
        var result = zoneDao.findById(zone.zone_id());

        assertTrue(result.isPresent());
        assertEquals(2, result.get().floor_id());
    }

    @Test
    @Order(4)
    void testFindAll() {
        zoneDao.save(new Zone(null, 104, 3));
        zoneDao.save(new Zone(null, 105, 6));
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
        Zone zone = zoneDao.save(new Zone(null, 106, 4));
        zoneDao.deleteById(zone.zone_id());
        assertTrue(zoneDao.findById(zone.zone_id()).isEmpty());
    }

    @Test
    @Order(7)
    void testDeleteAll() {
        zoneDao.deleteAll();
        assertEquals(0, zoneDao.count());
    }
}
