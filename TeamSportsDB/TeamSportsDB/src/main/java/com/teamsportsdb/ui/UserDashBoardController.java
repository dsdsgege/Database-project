package com.teamsportsdb.ui;

import com.teamsportsdb.utils.DashBoardUtils;
import com.teamsportsdb.utils.Database;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;


public class UserDashBoardController {
    private GridPane gridPane;

    @FXML
    private Button queryButton,insertButton,updateButton,deleteButton,createButton,
            alterButton,dropButton,renameButton;
    @FXML
    private Button queryExecute;
    @FXML
    private Label errorMessage;


    //queryButton pressed
    @FXML
    private Label headerLabel;
    @FXML
    private Label tableLabel;
    @FXML
    private ChoiceBox<String> tableChoice;
    @FXML
    private Label columnsLabel;
    @FXML
    private TextField columnsField;
    @FXML
    private Label whereLabel;
    @FXML
    private ChoiceBox<String> columnsChoice;
    @FXML
    private ChoiceBox<String> logicChoice;
    @FXML
    private TextField valueField;
    @FXML
    private TextField updateField;



    public void initialize() {
        //queryButton hover effect
        queryButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(queryButton,queryButton,insertButton,updateButton,deleteButton,createButton,alterButton,dropButton,renameButton);
        });
        //queryButton hover effect
        insertButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(insertButton,queryButton,insertButton,updateButton,deleteButton,createButton,alterButton,dropButton,renameButton);
        });
        updateButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(updateButton,queryButton,insertButton,updateButton,deleteButton,createButton,alterButton,dropButton,renameButton);
        });
        deleteButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(deleteButton,queryButton,insertButton,updateButton,deleteButton,createButton,alterButton,dropButton,renameButton);
        });
        createButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(createButton,queryButton,insertButton,updateButton,deleteButton,createButton,alterButton,dropButton,renameButton);
        });
        alterButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(alterButton,queryButton,insertButton,updateButton,deleteButton,createButton,alterButton,dropButton,renameButton);
        });
        dropButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(dropButton,queryButton,insertButton,updateButton,deleteButton,createButton,alterButton,dropButton,renameButton);
        });
        renameButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(renameButton,queryButton,insertButton,updateButton,deleteButton,createButton,alterButton,dropButton,renameButton);
        });
    }

    public void onQueryButtonAction() {
        headerLabel.setText("Lekérdezés:");
        headerLabel.setVisible(true);
        tableLabel.setVisible(true);
        tableChoice.setVisible(true);
        columnsLabel.setVisible(true);
        columnsField.setVisible(true);
        whereLabel.setVisible(true);
        columnsChoice.setVisible(true);
        logicChoice.setVisible(true);
        valueField.setVisible(true);
        queryExecute.setVisible(true);

        //Using my util to load the choices of the tables
        try {
            DashBoardUtils.choiceBoxOptions(tableChoice,"SHOW TABLES","Tables_in_teamsports");
        } catch (RuntimeException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }

        //Getting the data from the tableChoice, also setting the options for columns
        tableChoice.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                columnsChoice.getItems().clear();
                String table = tableChoice.getSelectionModel().getSelectedItem();
                String query = "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '%s'".formatted(table);
                DashBoardUtils.choiceBoxOptions(columnsChoice,query,"COLUMN_NAME");
            }
        });
    }

    //If Futtatés is clicked
    public void onQueryExecuteAction() {
        //getting the datas
        String table = tableChoice.getSelectionModel().getSelectedItem();
        String columns = columnsField.getText();
        String whereColumns = columnsChoice.getSelectionModel().getSelectedItem();
        String logic = logicChoice.getSelectionModel().getSelectedItem();
        String value = valueField.getText();

        //Checking if every field has been set
        if(table == null || columns == null || whereColumns == null || logic == null || value == null) {
            errorMessage.setText("Kérlek minden mezőt tölts ki!!");
            errorMessage.setVisible(true);
        }

        //Getting the logical operator of logicChoice
        logic = DashBoardUtils.getLogicalOperator(logic);
        String[] columnsArray = columns.split(",");
        String query = "SELECT %s FROM %s WHERE %s %s ?".formatted(columns,table,whereColumns,logic);

        //executin with prepared statement
        try (PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, value);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                int rowIndex = 1;
                gridPane = new GridPane();
                gridPane.setHgap(50);
                gridPane.setVgap(50);

                while (resultSet.next()) {
                    for (int i = 0; i < columnsArray.length; i++) {
                        String trimmedColumn = columnsArray[i].trim();
                        gridPane.add(new Label(trimmedColumn), i, 0);
                        gridPane.add(new Label(resultSet.getString(trimmedColumn)), i, rowIndex);
                    }
                    ++rowIndex;
                }
                gridPane.setAlignment(Pos.CENTER);
                Scene scene = new Scene(gridPane);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }
        } catch (SQLException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }
    }

//    public void onUpdateButtonAction() {
//
//        //setting up the labels, choiceboxes and fields
//        headerLabel.setText("Új adat felvitele:");
//        headerLabel.setVisible(true);
//        tableLabel.setVisible(true);
//        tableChoice.setVisible(true);
//        columnsLabel.setVisible(true);
//        columnsField.setVisible(true);
//        updateField.setVisible(true);
//        whereLabel.setVisible(true);
//        columnsChoice.setVisible(true);
//        logicChoice.setVisible(true);
//        valueField.setVisible(true);
//
//        //gettin tableChoice's
//
//        tableChoice.valueProperty().addListener(event -> {
//
//        });
//
//        //Checking if every field has been set
//        if(table == null || columns == null || whereColumns == null || logic == null || value == null) {
//            errorMessage.setText("Kérlek minden mezőt tölts ki!!");
//            errorMessage.setVisible(true);
//        }
//
//        String query = "UPDATE ? SET ";
//        String[] columns = columnsField.getText().split(",");
//        String[] updates = updateField.getText().split(",");
//        for (int i =0; i < columns.length; i++) {
//            query += columns[i] + " = " + updates[i];
//            if(i < updates.length - 1) {}
//        }
//    }
}
