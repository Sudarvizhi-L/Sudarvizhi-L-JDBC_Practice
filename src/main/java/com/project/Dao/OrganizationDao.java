package com.project.Dao;

import com.project.model.Organization;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrganizationDao {

    private final DataSource dataSource;

    public OrganizationDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Organization save(Organization org) {
        try (Connection conn = dataSource.getConnection()) {
            if (org.id() == null) {
                String sql = "INSERT INTO organization(name, certificateid, type, email, phone, ceo) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, org.name());
                ps.setString(2, org.certificateid());
                ps.setString(3, org.type());
                ps.setString(4, org.email());
                ps.setString(5, org.phone());
                ps.setString(6, org.ceo());
                ps.executeUpdate();

                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    return new Organization(keys.getInt(1), org.name(), org.certificateid(), org.type(), org.email(), org.phone(), org.ceo());
                }
                return null;
            } else {
                String sql = "UPDATE organization SET name=?, certificateid=?, type=?, email=?, phone=?, ceo=? WHERE id=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, org.name());
                ps.setString(2, org.certificateid());
                ps.setString(3, org.type());
                ps.setString(4, org.email());
                ps.setString(5, org.phone());
                ps.setString(6, org.ceo());
                ps.setInt(7, org.id());
                ps.executeUpdate();
                return org;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Organization> findById(int id) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM organization WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new Organization(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("certificateid"),
                        rs.getString("type"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("ceo")
                ));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Organization> findAll() {
        try (Connection conn = dataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM organization");

            List<Organization> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Organization(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("certificateid"),
                        rs.getString("type"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("ceo")
                ));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long count() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM organization")) {
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(int id) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM organization WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAll() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM organization");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
