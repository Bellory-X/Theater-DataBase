package com.example.theaters;

import com.example.theaters.controllers.WindowManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class TheaterApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        WindowManager.initialize(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}