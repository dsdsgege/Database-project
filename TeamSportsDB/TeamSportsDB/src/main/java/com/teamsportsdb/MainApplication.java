    package com.teamsportsdb;



    import com.sun.tools.javac.Main;
    import com.teamsportsdb.ui.ConnectController;
    import com.teamsportsdb.ui.MainController;
    import com.teamsportsdb.utils.SceneManager;
    import javafx.application.Application;

    import javafx.fxml.FXMLLoader;

    import javafx.scene.Scene;

    import javafx.stage.Stage;

    import java.io.IOException;
    import java.sql.*;

    public class MainApplication extends Application {
        private ConnectController connectController;
        private MainController mainController;

        @Override
        public void start(Stage stage) throws IOException {
            stage.setTitle("Csapatsport");

            SceneManager.setPrimaryStage(stage);

            //Set the scene to the database connect
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Connect.fxml"));
            Scene connScene = new Scene(fxmlLoader.load(),1950,1040);
            stage.setScene(connScene);
            stage.show();

        }


        public static void main(String[] args) {
            launch();
        }
    }