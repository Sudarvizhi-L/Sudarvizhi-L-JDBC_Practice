package com.temperaturedata.Dao;

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
            final String insertSql = "INSERT INTO building (colour, shape, name, bulidingNumber, noOfFloors ,height) VALUES(?,?,?,?,?,?)";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, building.colour());
                preparedStatement.setString(2, building.shape());
                preparedStatement.setString(3, building.name());
                preparedStatement.setInt(4, building.bulidingNumber());
                preparedStatement.setInt(5, building.noOfFloors());
                preparedStatement.setDouble(6, building.height());
                preparedStatement.execute();
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return new Building(resultSet.getInt(1), building.colour(), building.shape(), building.name(), building.bulidingNumber(),
                                building.noOfFloors(), building.height());
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
                return new Building(building.id(), building.colour(), building.shape(), building.name(), building.bulidingNumber(), building.noOfFloors(), building.height());
            }
        }

        return building;
    }

    public List<Building> findAll() throws SQLException {
        final String findSql = "SELECT id, colour, shape, name, bulidingNumber, noOfFloors, height FROM building";
        List<Building> listBuilding = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(findSql);
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                listBuilding.add(new Building(
                        resultSet.getInt("id"),
                        resultSet.getString("colour"),
                        resultSet.getString("shape"),
                        resultSet.getString("name"),
                        resultSet.getInt("bulidingNumber"),
                        resultSet.getInt("noOfFloors"),
                        resultSet.getDouble("height")

                ));
            }


        }

        return listBuilding;
    }

    public Optional<Building> findById(final int id) throws SQLException {
        final String findSqll = "SELECT id, colour, shape, name, bulidingNumber, noOfFloors, height FROM building WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(findSqll)){
            preparedStatement.setInt(1,id);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    return Optional.of(new Building(
                            resultSet.getInt("id"),
                            resultSet.getString("colour"),
                            resultSet.getString("shape"),
                            resultSet.getString("name"),
                            resultSet.getInt("bulidingNumber"),
                            resultSet.getInt("noOfFloors"),
                            resultSet.getDouble("height")
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
