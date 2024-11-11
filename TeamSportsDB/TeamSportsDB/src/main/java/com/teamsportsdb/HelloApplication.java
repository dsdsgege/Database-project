package com.teamsportsdb;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

import com.teamsportsdb.Database;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        stage.setMaximized(true);
        stage.setTitle("Csapatsport");

    /*Gridpane and Scene for the database connection*/
        GridPane connectionGridPane = new GridPane();
        connectionGridPane.setAlignment(Pos.CENTER);

        //database datas
        TextField username = new TextField();
        username.setPromptText("Database username");
        TextField password = new TextField();
        password.setPromptText("Database password");

        connectionGridPane.add(username, 0, 0);
        connectionGridPane.add(password, 0, 1);

        //Connect button
        Button connectButton = new Button("Connect");
        connectionGridPane.add(connectButton, 0, 2);

        //EventHandler for the Connect button  --- after a successful connection we can step forward to the login/register
        EventHandler<ActionEvent> connectEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    Database.connect(username.getText(),password.getText());
                    /*GridPane for application*/
                    GridPane root = new GridPane();
                    root.setAlignment(Pos.CENTER);
                    root.setVgap(10);
                    root.setHgap(0);

                    Label greeting = new Label("Welcome to my Database Project");
                    greeting.setFont(Font.font("Arial", FontWeight.BOLD, 30));

                    //Buttons
                    Button loginButton = new Button("Login");
                    Button regButton = new Button("Registration");
                    loginButton.setVisible(false);
                    regButton.setVisible(false);

                    root.add(greeting,0,0);
                    root.add(loginButton,0,0);
                    root.add(regButton,0,1);

                    // disappear after 3s delay
                    PauseTransition disappear = new PauseTransition(Duration.seconds(3));
                    disappear.setOnFinished(disappearEvent -> {
                        greeting.setVisible(false);
                        loginButton.setVisible(true);
                        regButton.setVisible(true);
                    });
                    disappear.play();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (SQLException | ClassNotFoundException e) {
                    Label error = new Label();
                    error.setText("Error: " + e.getMessage());
                    connectionGridPane.add(error, 0, 3);
                }
            }
        };
        connectButton.setOnAction(connectEvent);

        //Set the scene to the database connect
        Scene connScene = new Scene(connectionGridPane);
        stage.setScene(connScene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}