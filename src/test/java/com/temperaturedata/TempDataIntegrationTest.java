package com.temperaturedata;

import com.project.Dao.LocationDao;
import com.project.Dao.OrganizationDao;
import com.project.model.Location;
import com.project.model.Organization;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TempDataIntegrationTest {
    private static OrganizationDao organizationDao;
    private static LocationDao locationDao;
    @BeforeAll
    static void setup() throws Exception {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        ds.setUser("sudar");

        try (Connection conn = ds.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE organization (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255),
                    contactNo VARCHAR(20),
                    email VARCHAR(255),
                    website VARCHAR(20)
                )
            """);
            stmt.execute("""
                CREATE TABLE location (
                    location_id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255),
                    address VARCHAR(255),
                    org_id INT REFERENCES Organization
                )
            """);
        }

        organizationDao = new OrganizationDao(ds);
        locationDao = new LocationDao(ds);
    }

    @Test
    public void testFlow(){
        Organization org = new Organization(null, "Tech Corp", "1234", "contact@techcorp.com", "www.abc.com");
        Organization saved = organizationDao.save(org);
        assertNotNull(saved.id());
        assertEquals("Tech Corp", saved.name());

        Location location = locationDao.save(new Location(null, "Madurai", "Othakadai", saved.id()));
        assertNotNull(location.location_id());
        assertEquals("Madurai", location.name());
    }
}
