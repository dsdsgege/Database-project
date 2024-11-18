package com.teamsportsdb.utils;



import com.mysql.cj.protocol.Resultset;
import javafx.scene.chart.PieChart;

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
    public static ResultSet executeQuery(String query) throws RuntimeException {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            //Create the statement with connection object
            statement = connection.createStatement();

            //Execute select query
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException("Query unsuccessful \n" + e.getMessage());
        }
        return resultSet;
    }


    //Method to insert datas into given table and given columns
    //Returns false if there is already data with the same key

    //EZT NEM ÍGY KELL
//    public static boolean insertRecords(String table, String[] col, String[] val) throws RuntimeException {
//        String columns = String.join(",", col);
//        String values = String.join(",", val);
//        String query = "INSERT INTO %s (%s) VALUES (%s)".formatted(table,columns,values);
//        ResultSet resultset = null;
//        try {
//            resultset = executeQuery(query);
//        } catch (RuntimeException e) {
//            throw new RuntimeException(e);
//        }
//    }


    //Method to examine if the username is already in the database as it works as a Primary key in the table
    public static boolean isUsernameTaken(String username) throws RuntimeException, SQLException {
        ResultSet resultSet = null;

        //getting the needed resultset
        resultSet = executeQuery("SELECT felhasznalonev FROM felhasznalo");

        //Searching for the given username in the resultset, return true if found, otherwise false
        String column = "felhasznalonev";
        return isInResultSet(resultSet,column,username);
    }


    //Method to search in a resultSet
    //returns true if found
    private static boolean isInResultSet(ResultSet resultSet, String column, String record) throws RuntimeException, SQLException {
        try {
        while(resultSet.next()) {
            if (resultSet.getString(column).equals(record)) {
                return true;
            }
        }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return false;
    }
}
