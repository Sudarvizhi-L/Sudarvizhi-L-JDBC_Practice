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
        if (location.id() == null) {
            String sql = """
                INSERT INTO location (doorno, street, areaname, city, district, state, country, pincode, latitude, longitude)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, location.doorno());
                stmt.setInt(2, location.street());
                stmt.setString(3, location.areaname());
                stmt.setString(4, location.city());
                stmt.setString(5, location.district());
                stmt.setString(6, location.state());
                stmt.setString(7, location.country());
                stmt.setString(8, location.pincode());
                stmt.setDouble(9, location.latitude());
                stmt.setDouble(10, location.longitude());

                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        return new Location(
                                keys.getInt(1),
                                location.doorno(),
                                location.street(),
                                location.areaname(),
                                location.city(),
                                location.district(),
                                location.state(),
                                location.country(),
                                location.pincode(),
                                location.latitude(),
                                location.longitude()
                        );
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            String sql = """
                UPDATE location SET doorno=?, street=?, areaname=?, city=?, district=?, state=?, country=?, pincode=?, latitude=?, longitude=?
                WHERE id=?
            """;

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, location.doorno());
                stmt.setInt(2, location.street());
                stmt.setString(3, location.areaname());
                stmt.setString(4, location.city());
                stmt.setString(5, location.district());
                stmt.setString(6, location.state());
                stmt.setString(7, location.country());
                stmt.setString(8, location.pincode());
                stmt.setDouble(9, location.latitude());
                stmt.setDouble(10, location.longitude());
                stmt.setInt(11, location.id());

                stmt.executeUpdate();
                return location;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    public Optional<Location> findById(int id) {
        String sql = "SELECT * FROM location WHERE id = ?";

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
        String sql = "DELETE FROM location WHERE id = ?";

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
                rs.getInt("id"),
                rs.getString("doorno"),
                rs.getInt("street"),
                rs.getString("areaname"),
                rs.getString("city"),
                rs.getString("district"),
                rs.getString("state"),
                rs.getString("country"),
                rs.getString("pincode"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude")
        );
    }
}
