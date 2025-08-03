package com.project;

import com.project.Dao.SensorDao;
import com.project.model.Sensor;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SensorDaoTest {

    private final SensorDao sensorDao;

    public SensorDaoTest() throws SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        ds.setUser("sudar");

        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS sensor(
                    sensor_id INT AUTO_INCREMENT PRIMARY KEY,
                    sensor_no VARCHAR(255),
                    zone_id INT
                )
            """);
        }

        sensorDao = new SensorDao(ds);
    }

    @Test
    public void testSaveAndUpdate() throws SQLException {
        Sensor sensor = sensorDao.save(new Sensor(null, 1, 2));
        assertNotNull(sensor.sensor_id());
        assertEquals(1, sensor.sensor_no());

        Sensor updated = sensorDao.save(new Sensor(sensor.sensor_id(), 2, 2));
        assertEquals(sensor.sensor_id(), updated.sensor_id());
        assertEquals(2, updated.sensor_no());
    }

    @Test
    public void testFindAll() throws SQLException {
        sensorDao.save(new Sensor(null, 3, 2));
        sensorDao.save(new Sensor(null, 4, 2));

        List<Sensor> all = sensorDao.findAll();
        assertEquals(2, all.size());
    }

    @Test
    public void testFindById() throws SQLException {
        Sensor sensor = sensorDao.save(new Sensor(null, 3, 1));
        var found = sensorDao.findById(sensor.sensor_id());
        assertTrue(found.isPresent());
        assertEquals(3, found.get().sensor_no());
    }

    @Test
    public void testDeleteById() throws SQLException {
        Sensor sensor = sensorDao.save(new Sensor(null, 4, 1));
        sensorDao.deleteById(sensor.sensor_id());
        assertTrue(sensorDao.findById(sensor.sensor_id()).isEmpty());
    }

    @Test
    public void testDeleteAll() throws SQLException {
        sensorDao.save(new Sensor(null, 2, 2));
        sensorDao.save(new Sensor(null, 3, 1));
        sensorDao.deleteAll();
        assertEquals(0, sensorDao.count());
    }

    @Test
    public void testCount() throws SQLException {
        sensorDao.save(new Sensor(null, 1, 2));
        sensorDao.save(new Sensor(null, 2, 3));
        assertEquals(2, sensorDao.count());
    }
}
