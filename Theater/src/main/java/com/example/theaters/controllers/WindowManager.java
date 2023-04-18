package com.example.theaters.controllers;

import com.example.theaters.DataBase.QueryManager;
import com.example.theaters.TheaterApplication;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WindowManager {
    private static final Map<Status, String> viewsMap = new HashMap<>();

    private WindowManager() {}

    public static void initialize(Stage stage) throws IOException {
        viewsMap.put(Status.WELCOME, "views/welcome-view.fxml");
        viewsMap.put(Status.MAIN, "views/main-view.fxml");
        viewsMap.put(Status.ALL_TABLES, "views/tables-view.fxml");
        viewsMap.put(Status.ALL_QUERIES, "views/queries-view.fxml");
        Scene scene = new Scene(new FXMLLoader(
                TheaterApplication.class.getResource(viewsMap.get(Status.WELCOME))).load());
        stage.setTitle("Theaters.txt Base");
        stage.setScene(scene);
        stage.show();
    }

    public static void signIn(Stage stage, String userName, String password) {
        if (QueryManager.isOpenConnection()) QueryManager.closeConnection();
        QueryManager.initialize(userName, password);
        openNextWindow(stage, Status.MAIN);
    }

    public static void openNextWindow(Stage stage, Status nextStatus) {
        stage.close();
        try {
            Stage currentStage = new Stage();
            currentStage.setTitle("TOURIST CLUB");
            currentStage.initModality(Modality.APPLICATION_MODAL);
            currentStage.setScene(new Scene(new FXMLLoader(
                    TheaterApplication.class.getResource(viewsMap.get(nextStatus))).load()));
            currentStage.show();
        }
        catch (IOException e) {
            throw new RuntimeException("Open next window error, unable create Scene", e);
        }
    }

    public static void exit(Stage stage) {
        if (QueryManager.isOpenConnection()) QueryManager.closeConnection();
        stage.close();
    }
}
