package com.project;

import com.project.Dao.OrganizationDao;
import com.project.model.Organization;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrganizationDaoTest {

    private static OrganizationDao organizationDao;

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
        }

        organizationDao = new OrganizationDao(ds);
    }

    @Test
    @Order(1)
    void testSave() {
        Organization org = new Organization(null, "Tech Corp", "1234", "contact@techcorp.com", "www.abc.com");
        Organization saved = organizationDao.save(org);
        assertNotNull(saved.id());
        assertEquals("Tech Corp", saved.name());
    }

    @Test
    @Order(2)
    void testUpdate() {
        Organization org = organizationDao.save(new Organization(null, "Edu Inc", "9876543210", "info@edu.com", "www.website.com"));
        Organization updated = organizationDao.save(new Organization(org.id(), "Edu Ltd", "9876543210", "info@edu.com", "www.websitenew.com"));

        assertEquals(org.id(), updated.id());
        assertEquals("Edu Ltd", updated.name());
        assertEquals("www.websitenew.com", updated.website());
    }

    @Test
    @Order(3)
    void testFindById() {
        Organization org = organizationDao.save(new Organization(null, "Health Org", "9999999999", "contact@health.org", "www.website.com"));
        var result = organizationDao.findById(org.id());

        assertTrue(result.isPresent());
        assertEquals("Health Org", result.get().name());
    }

    @Test
    @Order(4)
    void testFindAll() {
        organizationDao.save(new Organization(null, "EnviroTech", "1111111111",  "enviro@tech.org", "www.websiteenv.com"));
        organizationDao.save(new Organization(null, "Green Future", "2222222222", "green@future.org", "www.websitegreen.com"));
        List<Organization> list = organizationDao.findAll();

        assertTrue(list.size() >= 2);
    }

    @Test
    @Order(5)
    void testCount() {
        long count = organizationDao.count();
        assertTrue(count >= 1);
    }

    @Test
    @Order(6)
    void testDeleteById() {
        Organization org = organizationDao.save(new Organization(null, "Temp Org", "3333333333",  "temp@org.com", "www.websitetemp.com"));
        organizationDao.deleteById(org.id());
        assertTrue(organizationDao.findById(org.id()).isEmpty());
    }

    @Test
    @Order(7)
    void testDeleteAll() {
        organizationDao.deleteAll();
        assertEquals(0, organizationDao.count());
    }
}
