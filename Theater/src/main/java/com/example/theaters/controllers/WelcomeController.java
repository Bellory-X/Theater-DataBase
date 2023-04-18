package com.example.theaters.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WelcomeController {
    public Button welcome;
    public Button exit;
    public TextField userName;
    public TextField password;

    public void initialize() {
        welcome.setOnAction(event -> WindowManager.signIn((Stage)welcome.getScene().getWindow(), userName.getText(), password.getText()));
        exit.setOnAction(event -> WindowManager.exit((Stage)exit.getScene().getWindow()));
    }
}