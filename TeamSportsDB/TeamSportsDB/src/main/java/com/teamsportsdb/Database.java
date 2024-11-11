package com.teamsportsdb;

import javax.swing.*;
import java.sql.*;

public class Database {

    //Declare of MySQL JDBC Driver:
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //Declae database URL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/music";
    //Declare Connection object, set to null for later
    private static Connection connection = null;


    public static void connect(String username, String password) throws SQLException, ClassNotFoundException {
        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, username, password);
            System.out.println("Connection successful");
        } catch (SQLException | ClassNotFoundException e) {
            throw new SQLException("Connection unsuccessful \n" + e.getMessage()) ;
        }
    }

    public static void disconnect() throws SQLException {
        if(connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Connection closed");
        }
    }


}
