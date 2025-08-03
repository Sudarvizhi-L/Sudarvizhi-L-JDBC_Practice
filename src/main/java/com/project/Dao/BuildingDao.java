package com.project.Dao;

import com.project.model.Building;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BuildingDao {
    private final DataSource dataSource;

    public BuildingDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Building save(Building building) throws SQLException {
        if(building.id() == null) {
            final String insertSql = "INSERT INTO building (id, name, latitude, longitude, height ,area, location_id) VALUES(?,?,?,?,?,?,?)";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(2, building.name());
                preparedStatement.setString(3, building.latitude());
                preparedStatement.setString(3, building.longitude());
                preparedStatement.setInt(4, building.height());
                preparedStatement.setInt(5, building.area());
                preparedStatement.setInt(6, building.location_id());
                preparedStatement.execute();
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return new Building(resultSet.getInt(1), building.name(), building.latitude(), building.longitude(),
                                building.height(), building.area(), building.location_id());
                    }
                }
            }



        }
        else{
            final String updateSql = "UPDATE building SET colour WHERE id = ?";
            try(Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(updateSql)){
                preparedStatement.setInt(1,building.id());
                preparedStatement.executeUpdate();
                return new Building(building.id(), building.name(), building.latitude(), building.longitude(), building.height(), building.area(), building.location_id());
            }
        }

        return building;
    }

    public List<Building> findAll() throws SQLException {
        final String findSql = "SELECT id, name, latitude, longitude, height, area, location_id FROM building";
        List<Building> listBuilding = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(findSql);
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                listBuilding.add(new Building(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("latitude"),
                        resultSet.getString("longitude"),
                        resultSet.getInt("height"),
                        resultSet.getInt("area"),
                        resultSet.getInt("location_id")

                ));
            }


        }

        return listBuilding;
    }

    public Optional<Building> findById(final int id) throws SQLException {
        final String findSqll = "SELECT id, name, latitude, longitude, height, area, location_id FROM building WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(findSqll)){
            preparedStatement.setInt(1,id);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    return Optional.of(new Building(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("latitude"),
                            resultSet.getString("longitude"),
                            resultSet.getInt("height"),
                            resultSet.getInt("area"),
                            resultSet.getInt("location_id")
                    ));

                }
            }
        }
        return Optional.empty();
    }

    public void deleteAll() throws SQLException {
        final String deleteAll = "DELETE FROM building";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteAll)){
            preparedStatement.executeUpdate();
        }
    }

    public long count() throws SQLException {
        final String countSql = "SELECT COUNT(*) FROM building";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(countSql);
            ResultSet resultSet = preparedStatement.executeQuery()){
            if(resultSet.next()){
                return resultSet.getLong(1);
            }


        }
        return 0;
    }

    public void deleteById(final int id) throws SQLException {
        final String deleteBySql = "DELETE FROM building WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteBySql)){
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
        }

    }
}
