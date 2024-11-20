package com.teamsportsdb.ui;

import com.teamsportsdb.utils.Database;
import com.teamsportsdb.utils.LoginManager;
import com.teamsportsdb.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;


public class RegistrationController {

    @FXML
    private TextField name;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label errorMessage;


    public void initialize() {
        SceneManager.getPrimaryStage().setMaximized(true);
    }

    public void onRegButtonAction() {
        String[] col = {"felhasznalonev", "nev", "jelszo"};
        String[] val = {username.getText(), name.getText(), password.getText()};
        try {
            Database.insertRecords("felhasznalo", col, val, "felhasznalonev");

            //setting the LoginManager fields so the user basically has logged in
            LoginManager.setName(name.getText());
            LoginManager.setUsername(username.getText());
            LoginManager.setPassword(password.getText());

            //Switching the scene to the user dashboard where the users can manipulate data
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/teamsportsdb/UserDashBoard.fxml"));
            Scene userDashBoardScene = new Scene(fxmlLoader.load(),900,600);
            SceneManager.getPrimaryStage().setScene(userDashBoardScene);

        } catch (Exception e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }
    }
}
