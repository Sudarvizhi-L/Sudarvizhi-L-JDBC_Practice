package com.temperaturedata;

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
                     colour VARCHAR(255), 
                     shape VARCHAR(255), name VARCHAR(255), 
                     bulidingNumber INT, 
                     noOfFloors INT ,height DOUBLE)
        """);

        }

        buildingDao = new BuildingDao(jdbcDataSource);
    }

    @Test
    public void testSave() throws SQLException {
        Building building = buildingDao.save(new Building(null,"Blue","Circle","IT Park",001,25,222.3));
        assertEquals("Blue",building.colour(),"Insert Failed");

        Building update = buildingDao.save(new Building(building.id(), "Light Blue", building.shape(), building.name(), building.bulidingNumber(),building.noOfFloors(), building.height()));
        assertEquals(update.id(),building.id());
        assertEquals("Light Blue",update.colour(),"Not updated");

    }
    @Test
    public void testFindAll() throws SQLException {
        buildingDao.save(new Building(null,"Blue","Circle","IT Park",001,25,222.3));
        buildingDao.save(new Building(null,"Blue","Circle","IT Park",002,26,221.3));
        var buildingCount = buildingDao.findAll();
        assertEquals(2,buildingCount.size());

    }

    @Test
    public void testFindById() throws SQLException {
        var buildings = buildingDao.save(new Building(null,"Blue","Circle","IT Park",002,26,221.3));
        var result = buildingDao.findById(buildings.id());
        Assertions.assertTrue(result.isPresent());
        assertEquals("Blue",result.get().colour());
    }

    @Test
    public void testDeleteAll() throws SQLException {
        buildingDao.save(new Building(null,"Blue","Circle","IT Park",001,25,222.3));
        buildingDao.save(new Building(null,"Blue","Circle","IT Park",002,26,221.3));
        buildingDao.deleteAll();
        assertEquals(0,buildingDao.count());
    }

    @Test
    public void testCount() throws SQLException {
        buildingDao.save(new Building(null,"Blue","Circle","IT Park",001,25,222.3));
        buildingDao.save(new Building(null,"Blue","Circle","IT Park",002,26,221.3));
        assertEquals(2,buildingDao.count());

    }

    @Test
    public void testDeleteById() throws SQLException {
        var building = buildingDao.save(new Building(null,"Blue","Circle","IT Park",001,25,222.3));
        buildingDao.deleteById(building.id());
        assertTrue(buildingDao.findById(building.id()).isEmpty());


    }


}

