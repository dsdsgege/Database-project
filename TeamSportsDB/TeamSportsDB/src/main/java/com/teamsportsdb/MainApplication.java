package com.teamsportsdb;

    import com.teamsportsdb.utils.SceneManager;
    import javafx.application.Application;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Scene;
    import javafx.stage.Stage;
    import java.io.IOException;

    public class MainApplication extends Application {
        @Override
        public void start(Stage stage) throws IOException {
            SceneManager.setPrimaryStage(stage);

            //Set the scene to the database connect
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Connect.fxml"));
            Scene connScene = new Scene(fxmlLoader.load());
            stage.setScene(connScene);
            stage.setMaximized(true);
            stage.show();
        }


        public static void main(String[] args) {
            launch();
        }
    }