package com.teamsportsdb.utils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DashBoardUtils {

    public static String getLogicalOperator(String logic) {
        switch (logic) {
            case "nagyobb":
                logic = ">";
                break;
            case "kisebb":
                logic = "<";
                break;
            case "egyenlő":
                logic = "=";
                break;
            case "nem egyenlő":
                logic = "<>";
                break;
            case "olyan, mint":
                logic = "LIKE";
                break;
            case "nem olyan":
                logic = "NOT LIKE";
                break;
            default:
                logic = "=";
        }
        return logic;
    }

    public static void updateButtonStyles(Button clickedButton, Button... allButton) {
        String defaultStyle = "-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 5px; -fx-padding: 5 15; -fx-cursor: hand;";
        String hoverStyle = "-fx-background-color: white; -fx-text-fill: #007bff; -fx-font-size: 16px; -fx-background-radius: 5px; -fx-padding: 5 15; -fx-cursor: hand;";
        for (Button button : allButton) {
            button.setStyle(defaultStyle);
        }

        clickedButton.setStyle(hoverStyle);
    }

    public static void choiceBoxOptions(ChoiceBox<String> choiceBox, String query, String columnName) throws RuntimeException {
        if(choiceBox == null || query == null) {
            throw new RuntimeException("Choice box or sql query is null");
        }

        choiceBox.getItems().clear();

        ResultSet result = Database.executeQuery(query);
        ArrayList<String> options = new ArrayList<>();
        try {
            while (result.next()) {
                options.add(result.getString(columnName));
            }
//            choiceBox.setItems(FXCollections.observableArrayList(options));
            choiceBox.getItems().addAll(options);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                result.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static void setVisible(boolean bool, Node... nodes) throws RuntimeException {
        try{
            for (Node node : nodes) {
                node.setVisible(bool);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isAnyNull( String... strings) {
        for (String str : strings) {
            if(str == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDatatypeCorrect(String table, String column, String value) throws RuntimeException {
        String query = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + table + "' AND COLUMN_NAME = '" + column + "'" ;
        String type = "";
        try (PreparedStatement statement = Database.getConnection().prepareStatement(query)){
            ResultSet result = statement.executeQuery();
            if(result.next()) {
                type = result.getString("DATA_TYPE");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            switch (type.toLowerCase()) {
                case "int":
                    int intValue = Integer.parseInt(value);
                    break;
                case "double":
                    double doubleValue = Double.parseDouble(value);
                    break;
                case "varchar":
                    if(value instanceof String) {
                        return true;
                    }
                    break;
                case "date":
                    if(value.charAt(4) == '-' && value.charAt(7) == '-' && value.charAt(10) == '-') {
                        return true;
                    }
                    break;
            }
        } catch (NumberFormatException e) {
           return false;
        }
        return true;
    }

    //Method to get all the columns names of a table in an array
    public static String[] columnNamesToArray(String table) throws RuntimeException {
        ArrayList<String> columnNames = new ArrayList<>();
        try (PreparedStatement preparedStatement = Database.getConnection().prepareStatement(
                "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNs WHERE TABLE_NAME = ?")) {
            preparedStatement.setString(1,table);
            try {
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    columnNames.add(rs.getString("COLUMN_NAME"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return columnNames.toArray(new String[columnNames.size()]);
    }

    //If there is username in the statement then felhasznalonev = with the logged in user's username
    public boolean isThereUsername(String columns) {
        String[] columnsInArray = columns.split(",");
        for (String column: columnsInArray) {
            if (column.trim().equals("felhasznalonev")) {
                return true;
            }
        }
        return false;
    }
}
