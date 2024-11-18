package com.teamsportsdb.ui;

import com.teamsportsdb.utils.Database;
import com.teamsportsdb.utils.SceneManager;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController {

    @FXML
    private Label welcomeText;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorMessage;
    @FXML
    private Button registration;
    @FXML
    private Button query;




    @FXML
    private void initialize() {
        //WelcomeText disappearing after 3s and every other appears
        PauseTransition disappear = new PauseTransition(Duration.seconds(3));
        disappear.setOnFinished(disappearEvent -> {
            welcomeText.setVisible(false);
            username.setVisible(true);
            password.setVisible(true);
            loginButton.setVisible(true);
            query.setVisible(true);
            registration.setVisible(true);
        });
        disappear.play();
    }

    public void onLoginButtonAction() throws SQLException {
            ResultSet resultSet = null;
            try {
                resultSet = Database.executeQuery("SELECT felhasznalonev, jelszo FROM felhasznalo");
            } catch (RuntimeException e) {
                errorMessage.setText(e.getMessage());
                errorMessage.setVisible(true);
            }

            while(resultSet.next()) {
                try {
                    if (!resultSet.next()) break;
                    if(resultSet.getString("felhasznalonev").equals(username.getText()) &&
                            resultSet.getString("jelszo").equals(password.getText())) {

                        System.out.println("Successfully logged in");
                    }
                } catch (SQLException e) {
                    errorMessage.setText(e.getMessage());
                    errorMessage.setVisible(true);
                }
            }
    }

    public void onRegistrationAction() {
        try {
            //load registration scene
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/teamsportsdb/Registration.fxml"));

            //set newScene to that
            Scene regScene = new Scene(fxmlLoader.load(),1950,1040);

            //swap the scenes on stage
            SceneManager.getPrimaryStage().setScene(regScene);

        } catch (Exception e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }
    }
}
