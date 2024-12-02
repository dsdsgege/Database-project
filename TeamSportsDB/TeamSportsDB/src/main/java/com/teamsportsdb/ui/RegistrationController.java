package com.teamsportsdb.ui;

import com.teamsportsdb.utils.Database;
import com.teamsportsdb.utils.LoginManager;
import com.teamsportsdb.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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
        errorMessage.setVisible(false);
        SceneManager.getPrimaryStage().setMaximized(true);
    }

    public void onRegButtonAction() {
        errorMessage.setVisible(false);

        if(username.getText().isEmpty() || name.getText().isEmpty() || password.getText().isEmpty()) {
            errorMessage.setText("Minden mezőt ki kell tölteni");
            errorMessage.setVisible(true);
            return;
        }
        String[] col = {"felhasznalonev", "nev", "jelszo"};
        String[] val = {username.getText(), name.getText(), password.getText()};

        //Check if the username is not occupied
        try (PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT felhasznalonev FROM felhasznalo")) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    if(rs.getString(1).equals(username.getText())) {
                        errorMessage.setText(username.getText() + " felhasználónév már foglalt!");
                        errorMessage.setVisible(true);
                        return;
                    }
                }
            } catch (SQLException e) {
                errorMessage.setText(e.getMessage());
                errorMessage.setVisible(true);
                return;
            }
        } catch (SQLException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
            return;
        }

        try {
            Database.insertRecords("felhasznalo", col, val, "felhasznalonev");

            //setting the LoginManager fields so the user basically has logged in
            LoginManager.setName(name.getText());
            LoginManager.setUsername(username.getText());
            LoginManager.setPassword(password.getText());

            //Switching the scene to the user dashboard where the users can manipulate data
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/teamsportsdb/UserDashBoard.fxml"));
            Scene userDashBoardScene = new Scene(fxmlLoader.load(),Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
            SceneManager.getPrimaryStage().setScene(userDashBoardScene);
            //SceneManager.getPrimaryStage().setFullScreen(true);
        } catch (Exception e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);

        }
    }

    public void onBackButtonAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/teamsportsdb/Main.fxml"));
        try {
            Scene mainScene = new Scene(fxmlLoader.load(), Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
            SceneManager.getPrimaryStage().setScene(mainScene);
            //SceneManager.getPrimaryStage().setFullScreen(true);

        } catch (IOException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }
    }
}
