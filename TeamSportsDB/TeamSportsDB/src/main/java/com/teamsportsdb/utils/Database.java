package com.teamsportsdb.utils;



import java.sql.*;


public class Database {

    //Declare of MySQL JDBC Driver:
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //Declare database URL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/teamsports";
    //Declare Connection object, set to null for later
    private static Connection connection = null;


    //Method that handles the connection to the database
    public static void connect(String username, String password) throws SQLException, ClassNotFoundException {
        try {
            //Class.forName(JDBC_DRIVER); //Not necessary with the new JDK
            connection = DriverManager.getConnection(DB_URL, username, password);
            System.out.println("Connection successful");
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    //Method to disconnect from the database
    public static void disconnect() throws SQLException {
        if(connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Connection closed");
        }
    }

    //Method to execute a query (It's not safe, I should use prepared statements as parameter)
    //Returns a ResultSet object that stores every data regarding the query
    public static ResultSet executeQuery(String query) throws SQLException, ClassNotFoundException {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            //Create the statement with connection object
            statement = connection.createStatement();

            //Execute select query
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new SQLException("Query unsuccessful \n" + e.getMessage());
        }
        return resultSet;
    }


}
