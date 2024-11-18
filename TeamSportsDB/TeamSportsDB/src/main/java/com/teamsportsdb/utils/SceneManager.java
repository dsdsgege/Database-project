package com.teamsportsdb.utils;

import javafx.stage.Stage;

public class SceneManager {
    private static Stage PrimaryStage;

    public static void setPrimaryStage(Stage primaryStage) {
        PrimaryStage = primaryStage;
    }

    public static Stage getPrimaryStage() {
        return PrimaryStage;
    }
}
