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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.hibernate.annotations.processing.SQL;

import java.sql.*;
import java.util.ArrayList;


public class UserDashBoardController {
    private GridPane gridPane;

    @FXML
    private Button queryButton,insertButton,updateButton,deleteButton,createButton,
            alterButton,dropButton,renameButton,queryExecute,updateExecute;

    @FXML
    private Label errorMessage;


    //queryButton pressed
    @FXML
    private Label headerLabel, tableLabel, columnsLabel,updateLabel, whereLabel;
    @FXML
    private ChoiceBox<String> tableChoice, columnsChoice, logicChoice;
    @FXML
    private TextField columnsField, valueField, updateField;



    public void initialize() {
        //queryButton hover effect
        queryButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(queryButton,queryButton,insertButton,updateButton,deleteButton,
                    createButton,alterButton,dropButton,renameButton);
        });
        //queryButton hover effect
        insertButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(insertButton,queryButton,insertButton,updateButton,deleteButton,
                    createButton,alterButton,dropButton,renameButton);
        });
        updateButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(updateButton,queryButton,insertButton,updateButton,deleteButton,
                    createButton,alterButton,dropButton,renameButton);
        });
        deleteButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(deleteButton,queryButton,insertButton,updateButton,deleteButton,
                    createButton,alterButton,dropButton,renameButton);
        });
        createButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(createButton,queryButton,insertButton,updateButton,deleteButton,
                    createButton,alterButton,dropButton,renameButton);
        });
        alterButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(alterButton,queryButton,insertButton,updateButton,deleteButton,
                    createButton,alterButton,dropButton,renameButton);
        });
        dropButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(dropButton,queryButton,insertButton,updateButton,deleteButton,
                    createButton,alterButton,dropButton,renameButton);
        });
        renameButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(renameButton,queryButton,insertButton,updateButton,deleteButton,
                    createButton,alterButton,dropButton,renameButton);
        });
    }

    public void onQueryButtonAction() {
        headerLabel.setText("Lekérdezés:");
        DashBoardUtils.setVisible(true,headerLabel,tableLabel,tableChoice,columnsLabel,columnsField,whereLabel,
                columnsChoice,logicChoice,valueField,
                queryExecute);


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
                String table = tableChoice.getSelectionModel().getSelectedItem();
                String query = "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '%s'".formatted(table);
                try {
                    DashBoardUtils.choiceBoxOptions(columnsChoice,query,"COLUMN_NAME");
                } catch (RuntimeException e) {
                    errorMessage.setText(e.getMessage());
                    errorMessage.setVisible(true);
                }

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
        if(DashBoardUtils.isAnyNull(table,columns,whereColumns,logic,value)) {
            errorMessage.setText("Kérlek minden mezőt tölts ki");
            errorMessage.setVisible(true);
        }

        //Getting the logical operator of logicChoice
        logic = DashBoardUtils.getLogicalOperator(logic);
        String[] columnsArray = columns.split(",");
        String query = "SELECT %s FROM %s WHERE %s %s ?".formatted(columns,table,whereColumns,logic);

        //executin with prepared statement
        try (PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, value);
            try {
                if (!DashBoardUtils.isDatatypeCorrect(table, whereColumns, value)) {
                    errorMessage.setText("Rossz adattípus! " + value);
                    errorMessage.setVisible(true);
                    return;
                }
            } catch (Exception e) {
                errorMessage.setText(e.getMessage());
                errorMessage.setVisible(true);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int rowIndex = 1;
                gridPane = new GridPane();
                gridPane.setHgap(50);
                gridPane.setVgap(20);
                gridPane.setAlignment(Pos.CENTER);

                while (resultSet.next()) {
                    for (int i = 0; i < columnsArray.length; i++) {
                        String trimmedColumn = columnsArray[i].trim();
                        Label columnLabel = new Label(trimmedColumn);
                        columnLabel.setFont(Font.font("Verdana",FontWeight.BOLD,30));
                        gridPane.add(new Label(trimmedColumn), i, 0);
                        gridPane.add(new Label(resultSet.getString(trimmedColumn)), i, rowIndex);
                    }
                    ++rowIndex;
                }

                Scene scene = new Scene(gridPane);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();

                DashBoardUtils.setVisible(false,headerLabel,tableLabel,tableChoice,columnsLabel,columnsField,whereLabel,
                        columnsChoice,logicChoice,valueField,
                        queryExecute);
            }
        } catch (SQLException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }
    }

    //Adatmódosítás button
    public void onUpdateButtonAction() {

        //setting up the labels, choiceboxes and fields
        headerLabel.setText("Oszlop értékének megváltoztatása:");
        DashBoardUtils.setVisible(true,headerLabel,tableLabel,tableChoice,columnsLabel,columnsField,updateLabel,updateField,whereLabel,columnsChoice,logicChoice,valueField,
                updateExecute);

        //Setting the options for the tables
        try {
            DashBoardUtils.choiceBoxOptions(tableChoice,"SHOW TABLES","Tables_in_teamsports");
        } catch (RuntimeException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }

        //Setting the options for the
        tableChoice.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String table = tableChoice.getSelectionModel().getSelectedItem();
                try {
                    DashBoardUtils.choiceBoxOptions(columnsChoice, "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '%s'".formatted(table), "COLUMN_NAME");
                } catch (RuntimeException e) {
                    errorMessage.setText(e.getMessage());
                    errorMessage.setVisible(true);
                }
            }
        });

    }

    public void onUpdateExecuteAction() {
        //Getting the datas from the nodes
        String table = tableChoice.getSelectionModel().getSelectedItem();
        String columns = columnsField.getText();
        String updateValues = updateField.getText();
        String whereColumn = columnsChoice.getSelectionModel().getSelectedItem();
        String logic = logicChoice.getSelectionModel().getSelectedItem();
        String value = valueField.getText();

        //checking if theyre nulls
        if(DashBoardUtils.isAnyNull(table,columns,updateValues,whereColumn,logic,value)) {
            errorMessage.setText("Kérlek minden mezőt tölts ki");
            errorMessage.setVisible(true);
        }

        String[] columnsArray = columns.split(",");
        String[] updateValuesArray = updateValues.split(",");

        for (int i = 0; i < columnsArray.length; i++) {
            try {
                columnsArray[i] = columnsArray[i].trim();
                updateValuesArray[i] = updateValuesArray[i].trim();
            } catch (IndexOutOfBoundsException e) {
                errorMessage.setText("Ez így nem jó!\t" +
                        "Az oszlopok száma nem egyenlő az értékek számával...");
                errorMessage.setVisible(true);
            }
        }

        logic = DashBoardUtils.getLogicalOperator(logic);

        String update = "UPDATE %s SET %s = ? WHERE %s %s ?";
        for (int i = 0; i < columnsArray.length; i++) {
            String formattedUpdate = String.format(update,table,columnsArray[i],whereColumn,logic);
            try (PreparedStatement preparedStatement = Database.getConnection().prepareStatement(formattedUpdate)) {
                    preparedStatement.setString(1, updateValuesArray[i]);
                if(!DashBoardUtils.isDatatypeCorrect(table,columnsArray[i],updateValuesArray[i])) {
                    errorMessage.setText("Rossz adattípus! " + updateValuesArray[i]);
                    errorMessage.setVisible(true);
                    return;
                }
                    preparedStatement.setString(2, value);
                if(!DashBoardUtils.isDatatypeCorrect(table,columnsArray[i],value)) {
                    errorMessage.setText("Rossz adattípus! " + value);
                    errorMessage.setVisible(true);
                    return;
                }

                    //execute the update
                    preparedStatement.executeUpdate();
                    DashBoardUtils.setVisible(false,headerLabel,tableLabel,tableChoice,columnsLabel,columnsField,updateLabel,updateField,whereLabel,columnsChoice,logicChoice,valueField,
                        updateExecute);

            } catch (SQLException e){
                errorMessage.setText(e.getMessage());
                errorMessage.setVisible(true);
            }

        }

    }
}
