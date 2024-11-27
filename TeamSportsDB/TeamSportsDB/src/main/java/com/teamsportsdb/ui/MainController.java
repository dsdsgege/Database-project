package com.teamsportsdb.ui;

import com.teamsportsdb.utils.Database;
import com.teamsportsdb.utils.LoginManager;
import com.teamsportsdb.utils.SceneManager;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
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
    private Button queryButton;
    @FXML
    private GridPane gridPane;




    @FXML
    private void initialize() {
        errorMessage.setVisible(false);
        gridPane.setAlignment(Pos.CENTER);
    }

    public void onLoginButtonAction() throws SQLException {
        errorMessage.setVisible(false);
            ResultSet resultSet = null;
            try {
                resultSet = Database.executeQuery("SELECT felhasznalonev, jelszo,nev FROM felhasznalo");
            } catch (RuntimeException e) {
                errorMessage.setText(e.getMessage());
                errorMessage.setVisible(true);
            }

            while(resultSet.next()) {
                try {
                    if(resultSet.getString("felhasznalonev").equals(username.getText())) {
                        if(resultSet.getString("jelszo").equals(password.getText())) {

                            //We are logging in using LoginManager
                            String name = resultSet.getString("nev");
                            String username = resultSet.getString("felhasznalonev");
                            String password = resultSet.getString("jelszo");
                            LoginManager.setName(name);
                            LoginManager.setUsername(username);
                            LoginManager.setPassword(password);

                            //Switching scene to userdashboard
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/teamsportsdb/UserDashBoard.fxml"));
                            Scene userDashBoardScene = new Scene(fxmlLoader.load(), Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
                            SceneManager.getPrimaryStage().setScene(userDashBoardScene);
                            SceneManager.getPrimaryStage().centerOnScreen();
                            SceneManager.getPrimaryStage().setMaximized(true);

                            Platform.runLater(() -> {
                                userDashBoardScene.getRoot().requestLayout();
                            });

                            //SceneManager.getPrimaryStage().setFullScreen(true);
                        } else {
                            errorMessage.setText("Helytelen jelszó!");
                            errorMessage.setVisible(true);
                        }
                    }
                } catch (SQLException e) {
                    errorMessage.setText(e.getMessage());
                    errorMessage.setVisible(true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
    }

    public void onRegistrationAction() {
        errorMessage.setVisible(false);
        try {
            //load registration scene
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/teamsportsdb/Registration.fxml"));

            //set newScene to that
            Scene regScene = new Scene(fxmlLoader.load(), Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());

            //swap the scenes on stage
            SceneManager.getPrimaryStage().setScene(regScene);

        } catch (Exception e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }
    }
    public void onQueryButtonAction() {
        errorMessage.setVisible(false);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/teamsportsdb/ComplexQuery.fxml"));
        try {
            Scene queryScene = new Scene(fxmlLoader.load(), Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
            SceneManager.getPrimaryStage().setScene(queryScene);

            //SceneManager.getPrimaryStage().setFullScreen(true);
        } catch (IOException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }
    }
}
