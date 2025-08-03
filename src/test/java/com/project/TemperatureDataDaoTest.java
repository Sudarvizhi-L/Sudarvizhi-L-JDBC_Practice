//package com.project;
//
//import com.project.Dao.TemperatureDao;
//import com.project.model.Temperature;
//import org.h2.jdbcx.JdbcDataSource;
//import org.junit.Test;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.Timestamp;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class TemperatureDaoTest {
//
//    private final TemperatureDao temperatureDao;
//
//    public TemperatureDaoTest() throws SQLException {
//        JdbcDataSource ds = new JdbcDataSource();
//        ds.setURL("jdbc:h2:mem:" + System.nanoTime() + ";DB_CLOSE_DELAY=-1");
//        ds.setUser("satha");
//
//        try (Connection conn = ds.getConnection();
//             Statement stmt = conn.createStatement()) {
//            stmt.execute("""
//                CREATE TABLE IF NOT EXISTS temperature (
//                    temp_data INT,
//                    dateAndTime TIMESTAMP
//                )
//            """);
//        }
//
//        temperatureDao = new TemperatureDao(ds);
//    }
//
//    @Test
//    public void testSaveAndFindAll() throws SQLException {
//        temperatureDao.deleteAll();
//
//        Temperature temp1 = new Temperature(25, Timestamp.valueOf("2025-07-31 10:00:00"));
//        Temperature temp2 = new Temperature(30, Timestamp.valueOf("2025-07-31 11:00:00"));
//
//        temperatureDao.save(temp1);
//        temperatureDao.save(temp2);
//
//        List<Temperature> all = temperatureDao.findAll();
//        assertEquals(2, all.size());
//    }
//
//    @Test
//    public void testCount() throws SQLException {
//        temperatureDao.deleteAll();
//
//        temperatureDao.save(new Temperature(28, Timestamp.valueOf("2025-07-31 14:00:00")));
//        temperatureDao.save(new Temperature(29, Timestamp.valueOf("2025-07-31 15:00:00")));
//
//        assertEquals(2, temperatureDao.count());
//    }
//
//    @Test
//    public void testDeleteAll() throws SQLException {
//        temperatureDao.save(new Temperature(22, new Timestamp(System.currentTimeMillis())));
//        temperatureDao.save(new Temperature(23, new Timestamp(System.currentTimeMillis())));
//
//        temperatureDao.deleteAll();
//        assertEquals(0, temperatureDao.count());
//    }
//}
