//package com.project.Dao;
//
//import com.project.model.Temperature;
//
//import javax.sql.DataSource;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class TemperatureDataDao {
//    private final DataSource dataSource;
//
//    public TemperatureDao(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    public void save(Temperature temperature) throws SQLException {
//        final String insertSql = "INSERT INTO temperature (temp_data, dateAndTime) VALUES (?, ?)";
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement ps = conn.prepareStatement(insertSql)) {
//            ps.setInt(1, temperature.temp_data());
//            ps.setTimestamp(2, temperature.dateAndTime());
//            ps.executeUpdate();
//        }
//    }
//
//    public List<Temperature> findAll() throws SQLException {
//        final String sql = "SELECT temp_data, dateAndTime FROM temperature";
//        List<Temperature> temps = new ArrayList<>();
//
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                temps.add(new Temperature(
//                        rs.getInt("temp_data"),
//                        rs.getTimestamp("dateAndTime")
//                ));
//            }
//        }
//        return temps;
//    }
//
//    public long count() throws SQLException {
//        final String sql = "SELECT COUNT(*) FROM temperature";
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            if (rs.next()) {
//                return rs.getLong(1);
//            }
//        }
//        return 0;
//    }
//
//    public void deleteAll() throws SQLException {
//        final String sql = "DELETE FROM temperature";
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.executeUpdate();
//        }
//    }
//}
