package com.persistance;

import javax.swing.text.Utilities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.model.UserType;
import com.model.Utilizator;

public class RepoUtilizatori implements Repository<String, Utilizator>{
    private final JDBCUtils jdbcUtils = new JDBCUtils();



    @Override
    public Optional<Utilizator> findOne(String s) {
        String query = "SELECT * from utilizatori where username = (?)";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1,s);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                UserType rol = UserType.valueOf(resultSet.getString("rol"));
                Utilizator user = new Utilizator(username,password,email,rol);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Utilizator> findAll() {
        return null;
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        String query = "INSERT INTO utilizatori(username,password,email,rol) VALUES(?,?,?,?)";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1,entity.getUsername());
            statement.setString(2,entity.getPassword());
            statement.setString(3,entity.getEmail());
            statement.setString(4,entity.getType().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Utilizator> delete(String s) {
        return Optional.empty();
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        return Optional.empty();
    }
}
