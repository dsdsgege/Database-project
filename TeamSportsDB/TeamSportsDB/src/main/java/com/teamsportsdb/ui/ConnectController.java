package com.teamsportsdb.ui;

import com.teamsportsdb.MainApplication;
import com.teamsportsdb.utils.Database;
import com.teamsportsdb.utils.SceneManager;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;


public class ConnectController{

    @FXML
    private Label errorMessage;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button connectButton;



    public void onConnectButtonAction() {
            String user = username.getText();
            String pass = password.getText();


            try {
                //connecting to database
                Database.connect(user, pass);
                try {
                    //if successful, load the main page
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/teamsportsdb/Main.fxml"));

                    Scene mainScene = new Scene(fxmlLoader.load(),1950,1040);

                    //we passed the stage so we can swap scenes
                    SceneManager.getPrimaryStage().setScene(mainScene);
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
