package com.teamsportsdb.ui;

import com.teamsportsdb.utils.DashBoardUtils;
import com.teamsportsdb.utils.Database;
import com.teamsportsdb.utils.LoginManager;
import com.teamsportsdb.utils.SceneManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.hibernate.annotations.processing.SQL;

import javax.swing.event.ChangeEvent;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


public class UserDashBoardController {
    private GridPane gridPane;

    @FXML
    private Button queryButton,insertButton,updateButton,deleteButton,queryExecute,updateExecute,insertExecute,deleteExecute;

    @FXML
    private Label errorMessage;


    @FXML
    private Label headerLabel, tableLabel, columnsLabel,updateLabel, whereLabel, loggedInLabel;
    @FXML
    private ChoiceBox<String> tableChoice, columnsChoice, logicChoice;
    @FXML
    private TextField columnsField, valueField, updateField;



    public void initialize()  {
        if(LoginManager.getName() == null || LoginManager.getUsername() == null || LoginManager.getPassword() == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/teamsportsdb/Main.fxml"));
            try {
                SceneManager.getPrimaryStage().setScene(new Scene(fxmlLoader.load()));
                SceneManager.getPrimaryStage().setMaximized(true);
            } catch (IOException e) {
                errorMessage.setText(e.getMessage());
                errorMessage.setVisible(true);
            }
        }
        loggedInLabel.setStyle("-fx-font-size: 16px;");
        loggedInLabel.setText("Bejelentkezve: " + LoginManager.getUsername());
        SceneManager.getPrimaryStage().setMaximized(true);
        //queryButton hover effect
        queryButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(queryButton,queryButton,insertButton,updateButton,deleteButton);
        });
        //queryButton hover effect
        insertButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(insertButton,queryButton,insertButton,updateButton,deleteButton);
        });
        updateButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(updateButton,queryButton,insertButton,updateButton,deleteButton);
        });
        deleteButton.setOnMouseClicked(event-> {
            DashBoardUtils.updateButtonStyles(deleteButton,queryButton,insertButton,updateButton,deleteButton);
        });
    }

    public void onExitButtonAction() throws IOException {
        LoginManager.setName(null);
        LoginManager.setUsername(null);
        LoginManager.setPassword(null);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/teamsportsdb/Main.fxml"));
        Scene mainScene = new Scene(fxmlLoader.load());
        SceneManager.getPrimaryStage().setScene(mainScene);
        SceneManager.getPrimaryStage().setMaximized(true);

    }

    public void onQueryButtonAction() {
        //hide every node that is for managing
        try {


            DashBoardUtils.setVisible(false, headerLabel, tableLabel, tableChoice, columnsLabel, columnsField, whereLabel,
                    columnsChoice, logicChoice, valueField, queryExecute, updateExecute, updateLabel, updateField, errorMessage, insertExecute, deleteExecute);
            headerLabel.setText("Adatok lekérdezése:");
            //show the nodes that is for this particular managing (query)
            DashBoardUtils.setVisible(true, headerLabel, tableLabel, tableChoice, columnsLabel, columnsField, whereLabel,
                    columnsChoice, logicChoice, valueField,
                    queryExecute);
        } catch (Exception e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }


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

        //Checking if table and columns has been set
        if(table == null || columns == null ) {
            errorMessage.setText("A táblát és oszlopokat kötelező megadni!");
            errorMessage.setVisible(true);
        }

        //Getting the logical operator of logicChoice
        if(logic != null) {
            logic = DashBoardUtils.getLogicalOperator(logic);
        }


        String selectedColumns = columns.equalsIgnoreCase("mind") ? "*": columns;
        String query;
        if(whereColumns == null || logic == null || value == null) {
            errorMessage.setText("FIGYELEM!! Szűrés nélküli lekérdezés!");
            errorMessage.setVisible(true);
            query = "SELECT %s FROM %s".formatted(selectedColumns,table);
        } else {
            query = "SELECT %s FROM %s WHERE %s %s ?".formatted(selectedColumns,table,whereColumns,logic);
        }

        //executin with prepared statement
        String[] columnsArray = null;
        try (PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query)) {
            if(whereColumns != null && logic != null && value != null)
                preparedStatement.setString(1, value);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                int rowIndex = 1;
                gridPane = new GridPane();
                gridPane.setHgap(10); // Set horizontal spacing between columns
                gridPane.setVgap(10); // Set vertical spacing between rows

                while (resultSet.next()) {
                    if(selectedColumns.equals("*")) {
                        //dynamically getting the columns to an array
                        try {
                            columnsArray = DashBoardUtils.columnNamesToArray(table);
                        } catch (RuntimeException e) {
                            errorMessage.setText(e.getMessage());
                            errorMessage.setVisible(true);
                        }
                    } else {
                        columnsArray = columns.split(",");
                    }

                    for (int i = 0; i < columnsArray.length; i++) {
                        String trimmedColumn = columnsArray[i].trim();
                        Label firstRow = new Label(trimmedColumn);
                        firstRow.setStyle("-fx-border-color:black; -fx-border-width: 2px; -fx-padding: 5px; -fx-font-size: 16px");

                        Label dataRow = new Label(resultSet.getString(trimmedColumn));
                        dataRow.setStyle("-fx-border-color:grey; -fx-border-width: 1px; -fx-padding: 2px;");
                        gridPane.add(firstRow, i, 0);
                        gridPane.add(dataRow, i, rowIndex);
                    }
                    ++rowIndex;
                }
                gridPane.setAlignment(Pos.CENTER);
                Scene scene = new Scene(gridPane);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                errorMessage.setVisible(false);
            }
        } catch (SQLException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }

    }

    //Új adat button
    //INSERT INTO
    public void onInsertButtonAction() {
        //hide every node that is for any kind of managing
        try {
            DashBoardUtils.setVisible(false, headerLabel, tableLabel, tableChoice, columnsLabel, columnsField, whereLabel,
                    columnsChoice, logicChoice, valueField, queryExecute, updateExecute, updateLabel, updateField, errorMessage, insertExecute, deleteExecute);

            //show the labels that we need for this managing (INSERT INTO)
            headerLabel.setText("Új adat(ok) felvitele:");
            DashBoardUtils.setVisible(true, headerLabel, tableLabel, tableChoice, columnsLabel, columnsField, updateLabel, updateField, insertExecute);
        } catch (RuntimeException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }

        //Getting the table options for the choicebox
        try {
            DashBoardUtils.choiceBoxOptions(tableChoice,"SHOW TABLES","tables_in_teamsports");
        } catch (RuntimeException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }
    }

    //Futtatás when Új adat is clicked
    public void onInsertExecuteAction() {
        //Making variables of the given datas
        String table = tableChoice.getSelectionModel().getSelectedItem();
        String columns = columnsField.getText();
        String values = updateField.getText();

        if(table == null || values == null){
            errorMessage.setText("A tábla és adatok kitöltése kötelező!");
            errorMessage.setVisible(true);
            return;
        }

        String[] columnsArray = columns.split(",");
        String[] valuesArray = values.split(",");
        if(valuesArray.length % columnsArray.length != 0 ){
            errorMessage.setText("Az értékek az oszlopok számának többszörösei kell, hogy legyenek");
            errorMessage.setVisible(true);
            return;
        }

        String valuesPlaceHolers = "";
        for (int i = 0; i < valuesArray.length-1; i++) {
            valuesPlaceHolers += "?,";
        }
        valuesPlaceHolers += "?";

        String query = "INSERT INTO %s (%s) VALUES (%s)".formatted(table,columns,valuesPlaceHolers);
        try (PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query)) {
            for (int j = 0; j < valuesArray.length; j++) {
                preparedStatement.setString(j+1, valuesArray[j].trim());
            }
            preparedStatement.executeUpdate();
            errorMessage.setVisible(false);
        } catch (SQLException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }
    }



    //Adatmódosítás button
    //Update statement
    public void onUpdateButtonAction() {
        //hide every node that is for managing
        try {
            DashBoardUtils.setVisible(false, headerLabel, tableLabel, tableChoice, columnsLabel, columnsField, whereLabel,
                    columnsChoice, logicChoice, valueField, queryExecute, updateExecute, updateLabel, updateField, errorMessage, insertExecute, deleteExecute);
        } catch (RuntimeException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }
        //show the labels that is for this particular managing (UPDATE)
        headerLabel.setText("Adatok megváltoztatása:");
        try {
            DashBoardUtils.setVisible(true, headerLabel, tableLabel, tableChoice, columnsLabel, columnsField, updateLabel, updateField, whereLabel, columnsChoice, logicChoice, valueField,
                    updateExecute);
        } catch (RuntimeException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }

        //Setting the options for the tables
        try {
            DashBoardUtils.choiceBoxOptions(tableChoice,"SHOW TABLES","Tables_in_teamsports");
        } catch (RuntimeException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }

        //Setting the options for the tableChoice
        //using the util method to adding options with query
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
        if(DashBoardUtils.isAnyNull(table,columns,updateValues)) {
            errorMessage.setText("Minden mezőt kötelező kitölteni, a szűrésen kívül");
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

        if(logic != null) {
            logic = DashBoardUtils.getLogicalOperator(logic);
        }

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
                if(whereColumn != null && logic != null && value != null) {
                    preparedStatement.setString(2, value);
                    if(!DashBoardUtils.isDatatypeCorrect(table,columnsArray[i],value)) {
                        errorMessage.setText("Rossz adattípus! " + value);
                        errorMessage.setVisible(true);
                        return;
                    }
                } else {
                    preparedStatement.setString(2, "");
                }

                    //execute the update
                    preparedStatement.executeUpdate();
                    errorMessage.setVisible(false);
            } catch (SQLException e){
                errorMessage.setText(e.getMessage());
                errorMessage.setVisible(true);
            }

        }
    }

    public void onDeleteButtonAction() {
        //hide the managing space
        try {
            DashBoardUtils.setVisible(false,headerLabel,tableLabel,tableChoice,columnsLabel,columnsField,whereLabel,
                    columnsChoice,logicChoice,valueField, queryExecute, updateExecute, updateLabel, updateField,errorMessage,insertExecute,deleteExecute);
        } catch  (RuntimeException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }


        headerLabel.setText("Adatok törlése:");
        try {
            DashBoardUtils.setVisible(true,tableLabel, headerLabel,tableChoice,whereLabel,columnsChoice,logicChoice,valueField,deleteExecute);
        } catch (RuntimeException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }

        try{
            DashBoardUtils.choiceBoxOptions(tableChoice,"SHOW TABLES","Tables_in_teamsports");
        } catch (RuntimeException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }

        tableChoice.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String table = tableChoice.getSelectionModel().getSelectedItem();

                try {
                    DashBoardUtils.choiceBoxOptions(columnsChoice, "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '%s'".formatted(table),
                            "COLUMN_NAME");
                } catch (RuntimeException e) {
                    errorMessage.setText(e.getMessage());
                    errorMessage.setVisible(true);
                }
            }
        });
    }

    public void onDeleteExecuteAction() {
        String table = tableChoice.getSelectionModel().getSelectedItem();
        String whereColumn = columnsChoice.getSelectionModel().getSelectedItem();
        String logic = DashBoardUtils.getLogicalOperator(logicChoice.getSelectionModel().getSelectedItem());
        String value= valueField.getText();

        if(table == null) {
            errorMessage.setText("Legalább a táblát meg kell adni!");
            errorMessage.setVisible(true);
            return;
        }

        String query = "DELETE FROM %s".formatted(table);

        if(whereColumn != null && logic != null && value != null) {
            query += " WHERE %s %s ?".formatted(whereColumn,logic);
        }

        try (PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, value);
            if(!DashBoardUtils.isDatatypeCorrect(table,whereColumn,value)) {
                errorMessage.setText("Rossz adattípus! " + value);
                errorMessage.setVisible(true);
                return;
            }

            if(value.equals(LoginManager.getUsername())) {
                errorMessage.setText("Saját magad nem törölheted ki!");
                errorMessage.setVisible(true);
                return;
            }

            preparedStatement.executeUpdate();
            errorMessage.setVisible(false);
        } catch (SQLException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }

    }
}
