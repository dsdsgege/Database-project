package com.teamsportsdb.ui;

import javafx.stage.Stage;

public interface Controller {
    Stage stage = new Stage();
    Stage getStage();
    void setStage(Stage stage);
}
