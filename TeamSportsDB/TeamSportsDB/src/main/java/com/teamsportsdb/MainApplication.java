    package com.teamsportsdb;



    import com.sun.tools.javac.Main;
    import com.teamsportsdb.ui.ConnectController;
    import com.teamsportsdb.ui.MainController;
    import javafx.application.Application;

    import javafx.fxml.FXMLLoader;

    import javafx.scene.Scene;

    import javafx.stage.Stage;

    import java.io.IOException;
    import java.sql.*;

    public class MainApplication extends Application {
        @Override
        public void start(Stage stage) throws IOException {
            stage.setTitle("Csapatsport");

            //Set the scene to the database connect
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Connect.fxml"));
            Scene connScene = new Scene(fxmlLoader.load(),1920,1080);
            stage.setScene(connScene);
            stage.show();

            //making the scene swap possible (from Connect.fxml to Main.fxml) with a ConnectController object
            ConnectController connectController = fxmlLoader.getController();
            connectController.setStage(stage);

            //making the scene swap possible with a MainController object
            fxmlLoader = new FXMLLoader(getClass().getResource("/com/teamsportsdb/Main.fxml"));
            fxmlLoader.load();
            MainController mainController = fxmlLoader.getController();
            mainController.setStage(stage);

        }


        public static void main(String[] args) {
            launch();
        }
    }