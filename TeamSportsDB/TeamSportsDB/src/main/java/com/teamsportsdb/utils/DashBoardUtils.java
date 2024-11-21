package com.teamsportsdb.utils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

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
}
