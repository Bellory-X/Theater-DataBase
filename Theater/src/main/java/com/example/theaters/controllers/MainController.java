package com.example.theaters.controllers;

import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainController {
    public Button watchTable;
    public Button watchQueries;
    public Button exit;
    public Button back;

    public void initialize () {
        watchTable.setOnAction(event -> WindowManager.openNextWindow((Stage)watchTable.getScene().getWindow(), Status.ALL_TABLES));
        watchQueries.setOnAction(event -> WindowManager.openNextWindow((Stage)watchQueries.getScene().getWindow(), Status.ALL_QUERIES));
        back.setOnAction(event -> WindowManager.openNextWindow((Stage)back.getScene().getWindow(), Status.WELCOME));
        exit.setOnAction(event -> WindowManager.exit((Stage)exit.getScene().getWindow()));
    }
}
