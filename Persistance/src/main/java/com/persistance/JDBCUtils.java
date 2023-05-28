package com.persistance;



import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {
    private String url;
    private String user;
    private String password;

    public JDBCUtils() {
        loadDBCredentials();
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("s a facut conexiune");
        } catch (SQLException e) {
            System.out.println("Error getting connection " + e);
        }

        return connection;
    }

    private void loadDBCredentials() {
        Properties properties = new Properties();

        try (InputStream input = Repository.class.getClassLoader().getResourceAsStream("configBG.properties")) {
            properties.load(input);

            url = properties.getProperty("jdbc.url");
            user = properties.getProperty("jdbc.user");
            password = properties.getProperty("jdbc.password");
            System.out.println(url + " " + user + " " + password);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}