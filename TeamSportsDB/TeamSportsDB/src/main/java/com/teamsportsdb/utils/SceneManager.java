package com.teamsportsdb.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage PrimaryStage;

    public static void setPrimaryStage(Stage primaryStage) {
        PrimaryStage = primaryStage;
        PrimaryStage.setTitle("Csapatsport");
    }

    public static Stage getPrimaryStage() {
        return PrimaryStage;
    }
}