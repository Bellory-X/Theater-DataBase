package com.example.theaters.controllers;

import com.example.theaters.DataBase.QueryManager;
import com.example.theaters.DataBase.exceptions.DataBaseException;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;

public class QueriesController {
    public ListView<String> listPositions;
    public TableView<Object> resultTable;
    public ListView<TextField> fieldListView;
    public TextField textField;
    public Button result;
    public Button back;
    public Button exit;
    private String queryName;
    private ShowEvent showEvent = ShowEvent.NUN;

    public void initialize() {
        listPositions.getItems().addAll(QueryManager.getQueryNames());
        setButton();
    }

    public void display() {
        queryName = listPositions.getSelectionModel().getSelectedItem();
        showEvent = ShowEvent.GOOD;
        fieldListView.getItems().clear();
        fieldListView.getItems().addAll(QueryManager.getParametersToQuery(queryName)
                .stream().map(TextField::new).toList());
    }

    public void setButton() {
        result.setOnAction(event -> resultEvent());
        back.setOnAction(event -> WindowManager.openNextWindow((Stage)back.getScene().getWindow(), Status.MAIN));
        exit.setOnAction(event -> WindowManager.exit((Stage)exit.getScene().getWindow()));
    }

    private void resultEvent() {
        resultTable.getColumns().clear();
        if (showEvent == ShowEvent.GOOD) {
            try {
                QueryManager.getQueryColumns(queryName,
                        fieldListView.getItems().stream()
                                .map(TextInputControl::getText)
                                .toList())
                        .forEach(el -> resultTable.getColumns().addAll(el));
                resultTable.setItems(QueryManager.getQueryData(queryName, fieldListView.getItems().stream()
                        .map(TextInputControl::getText)
                        .toList()));
                textField.setText("Запрос выполнен");
            } catch (DataBaseException e) {
                textField.setText("Ошибка");
            }
        }
        resultTable.refresh();
    }
}
