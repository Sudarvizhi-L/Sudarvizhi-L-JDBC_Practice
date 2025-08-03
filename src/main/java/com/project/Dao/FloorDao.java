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
            if (floor.floor_id() == null) {
                final String insertSql = "INSERT INTO floor( floor_no, building_id) VALUES (?, ?)";
                try (PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    //ps.setInt(1, floor.floor_id());
                    ps.setInt(1, floor.floor_no());
                    ps.setInt(2, floor.building_id());

                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            return new Floor(rs.getInt(1), floor.floor_no(), floor.building_id());
                        }
                    }
                }
            } else {
                final String updateSql = "UPDATE floor SET floor_no = ?, building_id = ? WHERE id = ?";
                try (PreparedStatement ps = connection.prepareStatement(updateSql)) {
                    ps.setInt(1, floor.floor_no());
                    ps.setInt(2, floor.building_id());
                    ps.setInt(3, floor.floor_id());

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
                            rs.getInt("floor_id"),
                            rs.getInt("floor_no"),
                            rs.getInt("building_id")
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
                        rs.getInt("floor_id"),
                        rs.getInt("floor_no"),
                        rs.getInt("building_id")
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
