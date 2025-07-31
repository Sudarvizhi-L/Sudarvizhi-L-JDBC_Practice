package com.project.Dao;

import com.project.model.Zone;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ZoneDao {

    private final DataSource dataSource;

    public ZoneDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Zone save(Zone zone) {
        try (Connection connection = dataSource.getConnection()) {
            if (zone.id() == null) {
                // INSERT
                String insertSql = """
                    INSERT INTO zone (zoneNumber, noOfroom, zoneType, floorId) VALUES (?, ?, ?, ?)
                """;
                try (PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, zone.zoneNumber());
                    ps.setInt(2, zone.noOfroom());
                    ps.setString(3, zone.zoneType());
                    ps.setInt(4, zone.floorId());

                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            return new Zone(rs.getInt(1), zone.zoneNumber(), zone.noOfroom(), zone.zoneType(), zone.floorId());
                        }
                    }
                }
            } else {
                // UPDATE
                String updateSql = """
                    UPDATE zone SET zoneNumber = ?, noOfroom = ?, zoneType = ?, floorId = ? WHERE id = ?
                """;
                try (PreparedStatement ps = connection.prepareStatement(updateSql)) {
                    ps.setInt(1, zone.zoneNumber());
                    ps.setInt(2, zone.noOfroom());
                    ps.setString(3, zone.zoneType());
                    ps.setInt(4, zone.floorId());
                    ps.setInt(5, zone.id());

                    ps.executeUpdate();
                    return zone;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving zone: " + e.getMessage(), e);
        }
        return null;
    }

    public Optional<Zone> findById(int id) {
        String sql = "SELECT * FROM zone WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Zone(
                            rs.getInt("id"),
                            rs.getInt("zoneNumber"),
                            rs.getInt("noOfroom"),
                            rs.getString("zoneType"),
                            rs.getInt("floorId")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding zone by ID", e);
        }
        return Optional.empty();
    }

    public List<Zone> findAll() {
        List<Zone> zones = new ArrayList<>();
        String sql = "SELECT * FROM zone";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                zones.add(new Zone(
                        rs.getInt("id"),
                        rs.getInt("zoneNumber"),
                        rs.getInt("noOfroom"),
                        rs.getString("zoneType"),
                        rs.getInt("floorId")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all zones", e);
        }
        return zones;
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM zone WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting zone by ID", e);
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM zone";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting all zones", e);
        }
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM zone";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting zones", e);
        }
        return 0;
    }
}
