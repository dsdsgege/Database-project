package com.teamsportsdb.utils;





import java.sql.*;
import java.util.Arrays;


public class Database {

    //Declare of MySQL JDBC Driver:
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //Declare database URL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/teamsports";
    //Declare Connection object, set to null for later
    private static Connection connection = null;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        Database.connection = connection;
    }

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
    public static void insertRecords(String table, String[] col, String[] val, String primaryKey) throws RuntimeException, SQLException {
        //algorithm to check if there is duplication in the primary key column
        int index = Arrays.binarySearch(col, primaryKey);
        for (; index < val.length; index += col.length) {
            if(isDuplicate(table,primaryKey,val[index])) {
                throw new RuntimeException("Duplicate record found as Primary Key " + val[index]);
            }
        }


        String columns = String.join(",", col);
        String placeholeders = "?";

        //setting placeholders
        for (int i = 0; i < col.length-1; i++) {
            placeholeders = placeholeders + ", ?";
        }
        String query = "INSERT INTO %s (%s) VALUES (%s)".formatted(table,columns,placeholeders);

        //execute query
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (int i = 0; i < val.length; i++) {
                preparedStatement.setString(i + 1, val[i]);
            }

            //execute insertion
            preparedStatement.executeUpdate();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }


    //Method to examine if the record is already in use if it works as a Primary key in the table
    public static boolean isDuplicate(String table, String primaryKey, String record) throws RuntimeException, SQLException {
        ResultSet resultSet = null;

        //getting the needed resultset
        try {
            resultSet = executeQuery("SELECT %s FROM %s".formatted(primaryKey, table));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }



        //Searching for the given record in the resultset, return true if found, otherwise false
        //so if found it's true, so PK already in the table
        String column = "felhasznalonev";
        return isInResultSet(resultSet,primaryKey,record);
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
