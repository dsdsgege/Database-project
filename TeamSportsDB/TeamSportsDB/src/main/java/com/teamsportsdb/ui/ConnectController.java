package com.teamsportsdb.ui;

import com.teamsportsdb.MainApplication;
import com.teamsportsdb.utils.Database;
import com.teamsportsdb.utils.SceneManager;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.sql.SQLException;


public class ConnectController{

    @FXML
    private Label errorMessage, welcomeText;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button connectButton;



    public void onConnectButtonAction() {
        errorMessage.setVisible(false);
            String user = username.getText();
            String pass = password.getText();


            try {
                //connecting to database
                Database.connect(user, pass);
                try {
                    //if successful, load the main page
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/teamsportsdb/Main.fxml"));

                    Scene mainScene = new Scene(fxmlLoader.load(), Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());

                    errorMessage.setVisible(false);
                    welcomeText.setVisible(true);
                    username.setVisible(false);
                    password.setVisible(false);
                    connectButton.setVisible(false);
                    //WelcomeText appearing for 3s and every other appears
                    PauseTransition animation = new PauseTransition(Duration.seconds(3));
                    animation.setOnFinished(disappearEvent -> {

                        SceneManager.getPrimaryStage().setScene(mainScene);
                    });
                    animation.play();
                } catch (Exception e) {
                    errorMessage.setText(e.getMessage());
                    errorMessage.setVisible(true);
                }

            } catch (SQLException | ClassNotFoundException e) {
                errorMessage.setText(e.getMessage());
                errorMessage.setVisible(true);
            }
    }
}
