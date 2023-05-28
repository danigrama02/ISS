package com.persistance;

import com.model.Bug;
import com.model.Gravitate;
import com.model.Status;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RepoBuguri implements Repository<Integer, Bug>{

    private final JDBCUtils jdbcUtils = new JDBCUtils();

    @Override
    public Optional<Bug> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Iterable<Bug> findAll() {
        Set<Bug> buguri = new HashSet<>();

        String query = "SELECT * from \"buguri\"";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String denumire = resultSet.getString("denumire");
                String descriere = resultSet.getString("descriere");
                Gravitate gravitate = Gravitate.valueOf(resultSet.getString("gravitate"));
                Status status = Status.valueOf(resultSet.getString("status"));
                String timestamp = resultSet.getString("timestamp");
                Bug bug = new Bug (id,denumire,descriere,gravitate,status,timestamp);
                buguri.add(bug);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return buguri;
    }

    @Override
    public Optional<Bug> save(Bug entity) {
        String query = "INSERT INTO buguri(denumire,descriere,gravitate,status,timestamp) VALUES(?,?,?,?,?)";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1,entity.getDenumire());
            statement.setString(2,entity.getDescriere());
            statement.setString(3,entity.getGravitate().toString());
            statement.setString(4,entity.getStatus().toString());
            statement.setString(5,entity.getTimestamp());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Bug> delete(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Optional<Bug> update(Bug entity) {
        String query = "UPDATE buguri set status=? where id=?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1,entity.getStatus().toString());
            statement.setInt(2,entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(entity);
    }
}
