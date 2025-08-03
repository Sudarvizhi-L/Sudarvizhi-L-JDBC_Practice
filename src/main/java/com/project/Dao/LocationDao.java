package com.project.Dao;

import com.project.model.Location;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationDao {

    private final DataSource dataSource;

    public LocationDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Location save(Location location) {
        if (location.location_id() == null) {
            String sql = """
                INSERT INTO location (name, address, org_id)
                VALUES (?, ?, ?)
            """;

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, location.name());
                stmt.setString(2, location.address());
                stmt.setInt(3,location.org_id());

                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        return new Location(
                                keys.getInt(1),
                                location.name(),
                                location.address(),
                                location.org_id()
                        );
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            String sql = """
                UPDATE location SET name=?, address=?, org_id=?
                WHERE location_id=?
            """;

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {


                stmt.setString(1, location.name());
                stmt.setString(2, location.address());
                stmt.setInt(3, location.org_id());
                stmt.setInt(4, location.location_id());

                stmt.executeUpdate();
                return location;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    public Optional<Location> findById(int id) {
        String sql = "SELECT * FROM location WHERE location_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public List<Location> findAll() {
        String sql = "SELECT * FROM location";
        List<Location> locations = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                locations.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return locations;
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM location WHERE location_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM location";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Location mapRow(ResultSet rs) throws SQLException {
        return new Location(
                rs.getInt("location_id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getInt("org_id")
        );
    }
}
