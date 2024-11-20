package com.teamsportsdb.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage PrimaryStage;

    public static void setPrimaryStage(Stage primaryStage) {
        PrimaryStage = primaryStage;
        PrimaryStage.setTitle("Csapatsport");
        PrimaryStage.setMaximized(true);
    }

    public static Stage getPrimaryStage() {
        return PrimaryStage;
    }
}