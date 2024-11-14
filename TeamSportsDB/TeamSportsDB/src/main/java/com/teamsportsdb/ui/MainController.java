package com.teamsportsdb.ui;

import com.teamsportsdb.utils.Database;
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

public class MainController implements Controller {

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

    private Stage stage;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    @FXML
    private void initialize() {
        //WelcomeText disappearing after 3s and every other appears
        PauseTransition disappear = new PauseTransition(Duration.seconds(3));
        disappear.setOnFinished(disappearEvent -> {
            welcomeText.setVisible(false);
            username.setVisible(true);
            password.setVisible(true);
            loginButton.setVisible(true);
        });
        disappear.play();
    }

    public void onLoginButtonAction() throws SQLException {
            ResultSet resultSet;
            try {
                resultSet = Database.executeQuery("SELECT felhasznalonev, jelszo FROM felhasznalo");
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            while(resultSet.next()) {
                try {
                    if (!resultSet.next()) break;
                    if(resultSet.getString("felhasznalonev").equals(username.getText()) &&
                            resultSet.getString("jelszo").equals(password.getText())) {

                        System.out.println("Successfully logged in");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
    }

    public void onRegistrationAction() throws SQLException {
        try {
            //go to registration scene
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/teamsportsdb/Registration.fxml"));

            //set newScene to that
            Scene newScene = new Scene(fxmlLoader.load());

            //swap the scenes on stage
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            errorMessage.setText(e.getMessage());
        }
    }
}
