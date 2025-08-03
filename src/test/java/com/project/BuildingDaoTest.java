package com.project;

import com.project.Dao.BuildingDao;
import com.project.model.Building;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BuildingDaoTest {
    private final BuildingDao buildingDao;

    public BuildingDaoTest() throws SQLException {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        jdbcDataSource.setUser("sudar");
        try(Connection connection = jdbcDataSource.getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("""
                     CREATE TABLE building(
                     id INT AUTO_INCREMENT PRIMARY KEY,
                     name VARCHAR(255),
                     latitude VARCHAR(40),
                     longitude VARCHAR(40),
                     height INT,
                     area INT,
                     location_id INT FOREIGN KEY
        """);

        }

        buildingDao = new BuildingDao(jdbcDataSource);
    }

    @Test
    public void testSave() throws SQLException {
        Building building = buildingDao.save(new Building(null,"Building A","23.44","78.32",400,700,1));
        assertEquals("Building A",building.name(),"Insert Failed");

        Building update = buildingDao.save(new Building(null,"Building Blue","23.44","78.32",400,700,1));
        assertEquals(update.id(),building.id());
        assertEquals("Building Blue",update.name(),"Not updated");

    }
    @Test
    public void testFindAll() throws SQLException {
        buildingDao.save(new Building(null,"Building Blue","33.45","89.432",320,112,1));
        buildingDao.save(new Building(null,"Building Green","78.98","312.45",450,567,1));
        var buildingCount = buildingDao.findAll();
        assertEquals(2,buildingCount.size());

    }

    @Test
    public void testFindById() throws SQLException {
        var buildings = buildingDao.save(new Building(null,"Building Blue","21.45","83.54",200,2006,1));
        var result = buildingDao.findById(buildings.id());
        Assertions.assertTrue(result.isPresent());
        assertEquals("Building Blue",result.get().name());
    }

    @Test
    public void testDeleteAll() throws SQLException {
        buildingDao.save(new Building(null,"Blue","Circle","IT Park",001,25,1));
        buildingDao.save(new Building(null,"Blue","Circle","IT Park",002,26,1));
        buildingDao.deleteAll();
        assertEquals(0,buildingDao.count());
    }

    @Test
    public void testCount() throws SQLException {
        buildingDao.save(new Building(null,"Blue","Circle","IT Park",001,25,1));
        buildingDao.save(new Building(null,"Blue","Circle","IT Park",002,26,1));
        assertEquals(2,buildingDao.count());

    }

    @Test
    public void testDeleteById() throws SQLException {
        var building = buildingDao.save(new Building(null,"Blue","Circle","IT Park",001,25,1));
        buildingDao.deleteById(building.id());
        assertTrue(buildingDao.findById(building.id()).isEmpty());


    }


}

