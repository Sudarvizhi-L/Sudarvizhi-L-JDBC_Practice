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
        jdbcDataSource.setUser("satha");

        try (Connection connection = jdbcDataSource.getConnection();
             Statement statement = connection.createStatement()) {

            // Create building table
            statement.execute("""
                CREATE TABLE building(
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255)
                )
            """);

            // Create floor table
            statement.execute("""
                CREATE TABLE floor(
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255),
                    noOfZone INT,
                    floorNumber INT,
                    buildingId INT,
                    FOREIGN KEY(buildingId) REFERENCES building(id)
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
        Floor floor = floorDao.save(new Floor(null, "Production", 3, 2, 1));
        assertNotNull(floor.id(), "Floor was not saved (ID is null)");
        assertEquals("Production", floor.name());
    }

    @Test
    @Order(2)
    void testUpdate() {
        Floor floor = floorDao.save(new Floor(null, "QA", 2, 1, 1));
        Floor updated = floorDao.save(new Floor(floor.id(), "Testing", 5, 3, 1));

        assertEquals(floor.id(), updated.id());
        assertEquals("Testing", updated.name());
        assertEquals(5, updated.noOfZone());
    }

    @Test
    @Order(3)
    void testFindById() {
        Floor saved = floorDao.save(new Floor(null, "Support", 1, 1, 1));
        var result = floorDao.findById(saved.id());

        assertTrue(result.isPresent());
        assertEquals("Support", result.get().name());
    }

    @Test
    @Order(4)
    void testFindAll() {
        floorDao.save(new Floor(null, "Dev", 2, 2, 1));
        floorDao.save(new Floor(null, "Admin", 1, 0, 1));

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
        Floor floor = floorDao.save(new Floor(null, "Temp", 1, 1, 1));
        floorDao.deleteById(floor.id());
        assertTrue(floorDao.findById(floor.id()).isEmpty());
    }

    @Test
    @Order(7)
    void testDeleteAll() {
        floorDao.deleteAll();
        assertEquals(0, floorDao.count());
    }
}
