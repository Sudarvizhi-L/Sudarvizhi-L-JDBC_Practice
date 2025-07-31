package com.project.Dao;

import com.project.model.Floor;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FloorDao {
    private final DataSource dataSource;

    public FloorDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Floor save(Floor floor) {
        try (Connection connection = dataSource.getConnection()) {
            if (floor.id() == null) {
                final String insertSql = "INSERT INTO floor(name, noOfZone, floorNumber, buildingId) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, floor.name());
                    ps.setInt(2, floor.noOfZone());
                    ps.setInt(3, floor.floorNumber());
                    ps.setInt(4, floor.buildingId());

                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            return new Floor(rs.getInt(1), floor.name(), floor.noOfZone(), floor.floorNumber(), floor.buildingId());
                        }
                    }
                }
            } else {
                final String updateSql = "UPDATE floor SET name = ?, noOfZone = ?, floorNumber = ?, buildingId = ? WHERE id = ?";
                try (PreparedStatement ps = connection.prepareStatement(updateSql)) {
                    ps.setString(1, floor.name());
                    ps.setInt(2, floor.noOfZone());
                    ps.setInt(3, floor.floorNumber());
                    ps.setInt(4, floor.buildingId());
                    ps.setInt(5, floor.id());

                    ps.executeUpdate();
                    return floor;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving floor: " + e.getMessage(), e);
        }
        return null;
    }

    public Optional<Floor> findById(int id) {
        final String sql = "SELECT * FROM floor WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Floor(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("noOfZone"),
                            rs.getInt("floorNumber"),
                            rs.getInt("buildingId")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding floor by ID", e);
        }
        return Optional.empty();
    }

    public List<Floor> findAll() {
        List<Floor> floors = new ArrayList<>();
        final String sql = "SELECT * FROM floor";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                floors.add(new Floor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("noOfZone"),
                        rs.getInt("floorNumber"),
                        rs.getInt("buildingId")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all floors", e);
        }
        return floors;
    }

    public void deleteById(int id) {
        final String sql = "DELETE FROM floor WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting floor by ID", e);
        }
    }

    public void deleteAll() {
        final String sql = "DELETE FROM floor";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting all floors", e);
        }
    }

    public long count() {
        final String sql = "SELECT COUNT(*) FROM floor";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting floors", e);
        }
        return 0;
    }
}
