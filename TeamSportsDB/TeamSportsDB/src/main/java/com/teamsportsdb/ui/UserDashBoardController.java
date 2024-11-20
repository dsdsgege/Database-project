package com.teamsportsdb.ui;

import com.teamsportsdb.utils.Database;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class UserDashBoardController {

    @FXML
    private Button queryButton,insertButton,updateButton,deleteButton,createButton,
            alterButton,dropButton,renameButton;

    //queryButton pressed
    @FXML
    private ChoiceBox<String> tableChoice;

    public void initialize() throws SQLException {
        ResultSet resultSet = Database.executeQuery("SHOW TABLES");
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                arrayList.add(resultSet.getString("Tables_in_teamsports"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }

        tableChoice.setItems(FXCollections.observableArrayList(arrayList));
    }
}
