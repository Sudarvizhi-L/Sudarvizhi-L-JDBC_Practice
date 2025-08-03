package com.project;

import com.project.Dao.FloorDao;
import com.project.model.Floor;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FloorDaoTest {

    private static FloorDao floorDao;

    @BeforeAll
    static void setup() throws SQLException {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        jdbcDataSource.setUser("sudar");

        try (Connection connection = jdbcDataSource.getConnection();
             Statement statement = connection.createStatement()) {

            // Create building table
            statement.execute("""
                CREATE TABLE building(
                    id INT AUTO_INCREMENT PRIMARY KEY,
                     name VARCHAR(255),
                     latitude VARCHAR(40),
                     longitude VARCHAR(40),
                     height INT,
                     area INT,
                     location_id INT 
                )
            """);

            // Create floor table
            statement.execute("""
                CREATE TABLE floor(
                    floor_id INT AUTO_INCREMENT PRIMARY KEY,
                    floor_no INT,
                    building_id INT
                )
            """);

            // Insert a test building with id=1
            statement.execute("INSERT INTO building(name) VALUES ('Main Building')");
        }

        floorDao = new FloorDao(jdbcDataSource);
    }

    @Test
    @Order(1)
    void testSave() {
        Floor floor = floorDao.save(new Floor(null, 1, 1));
        assertNotNull(floor.floor_id(), "Floor was not saved (ID is null)");
        assertEquals(1, floor.floor_no());
    }

    @Test
    @Order(2)
    void testUpdate() {
        Floor floor = floorDao.save(new Floor(null, 2, 2));
        Floor updated = floorDao.save(new Floor(floor.floor_id(), 3, 5));

        assertEquals(floor.floor_id(), updated.floor_id());
        assertEquals(3, updated.floor_no());
    }

    @Test
    @Order(3)
    void testFindById() {
        Floor saved = floorDao.save(new Floor(null, 4, 1));
        var result = floorDao.findById(saved.floor_id());

        assertTrue(result.isPresent());
        assertEquals(4, result.get().floor_no());
    }

    @Test
    @Order(4)
    void testFindAll() {
        floorDao.save(new Floor(null, 5, 2));
        floorDao.save(new Floor(null, 6, 1));

        List<Floor> allFloors = floorDao.findAll();
        assertTrue(allFloors.size() >= 2);
    }

    @Test
    @Order(5)
    void testCount() {
        long count = floorDao.count();
        assertTrue(count >= 1);
    }

    @Test
    @Order(6)
    void testDeleteById() {
        Floor floor = floorDao.save(new Floor(null, 7, 1));
        floorDao.deleteById(floor.floor_id());
        assertTrue(floorDao.findById(floor.floor_id()).isEmpty());
    }

    @Test
    @Order(7)
    void testDeleteAll() {
        floorDao.deleteAll();
        assertEquals(0, floorDao.count());
    }
}
