package com.example.cinebook;

import com.example.cinebook.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class CineBookApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        SceneManager.setStage(stage);
        SceneManager.switchScene("/com/example/cinebook/view/login.fxml", "CineBook - Login", 980, 680);
    }
}
