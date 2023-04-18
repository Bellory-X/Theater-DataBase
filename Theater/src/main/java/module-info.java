module com.example.theathers {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.theaters to javafx.fxml;
    exports com.example.theaters;
    exports com.example.theaters.controllers;
    opens com.example.theaters.controllers to javafx.fxml;
}