package com.teamsportsdb.ui;

import com.teamsportsdb.utils.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;



public class RegistrationController {

    @FXML
    private TextField name;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label errorMessage;




    public void onRegButtonAction() {
        try {
            if(Database.isUsernameTaken(username.getText())) {
                errorMessage.setText("This username is already in use.\nTry something else...");
                errorMessage.setVisible(true);
            }
            else {

            }
        } catch (Exception e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }
    }
}
