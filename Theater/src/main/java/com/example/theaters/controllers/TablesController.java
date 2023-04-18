package com.example.theaters.controllers;

import com.example.theaters.DataBase.QueryManager;
import com.example.theaters.DataBase.exceptions.DataBaseException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TablesController {
    public ListView<String> listPositions;
    public TableView<Object> resultTable;
    public ListView<TextField> fieldListView1;
    public ListView<TextField> fieldListView2;
    public TextField textField;
    public Text oldValue;
    public Text newValue;
    public Button insert;
    public Button update;
    public Button delete;
    public Button result;
    public Button back;
    public Button exit;
    private String tableName;
    private ShowEvent showEvent = ShowEvent.NUN;
    private List<String> columns = new ArrayList<>();

    public void initialize() {
        listPositions.getItems().addAll(QueryManager.getTableNames());
        setButton();
    }

    public void display() {
        tableName = listPositions.getSelectionModel().getSelectedItem();
        showEvent = ShowEvent.GOOD;
        setResultTable();
    }

    public void setButton() {
        insert.setOnAction(event -> insertEvent());
        update.setOnAction(event -> updateEvent());
        delete.setOnAction(event -> deleteEvent());
        result.setOnAction(event -> resultEvent());
        back.setOnAction(event -> WindowManager.openNextWindow((Stage)back.getScene().getWindow(), Status.MAIN));
        exit.setOnAction(event -> WindowManager.exit((Stage)exit.getScene().getWindow()));
    }

    private void insertEvent() {
        if (showEvent == ShowEvent.NUN) return;
        newValue.setText("Поля новой записи");
        oldValue.setText("");
        columns = QueryManager.getColumnNames(tableName);
        fieldListView1.getItems().clear();
        fieldListView2.getItems().clear();
        fieldListView1.getItems().addAll(columns.stream().map(TextField::new).toList());
        showEvent = ShowEvent.INSERT;
    }

    private void updateEvent() {
        if (showEvent == ShowEvent.NUN) return;
        newValue.setText("Поля новой записи");
        oldValue.setText("Поля старой записи");
        columns = QueryManager.getColumnNames(tableName);
        fieldListView1.getItems().clear();
        fieldListView2.getItems().clear();
        fieldListView1.getItems().addAll(columns.stream().map(TextField::new).toList());
        fieldListView2.getItems().addAll(columns.stream().map(TextField::new).toList());
        showEvent = ShowEvent.UPDATE;
    }

    private void deleteEvent() {
        if (showEvent == ShowEvent.NUN) return;
        newValue.setText("Поля для удаления записи");
        oldValue.setText("");
        columns = QueryManager.getColumnNames(tableName);
        fieldListView1.getItems().clear();
        fieldListView2.getItems().clear();
        fieldListView1.getItems().addAll(columns.stream().map(TextField::new).toList());
        showEvent = ShowEvent.DELETE;
    }

    private void resultEvent() {
        try {
            switch (showEvent) {
                case INSERT -> {
                    List<String> oldValues = fieldListView1.getItems().stream().map(TextInputControl::getText).toList();
                    List<String> newValues = new ArrayList<>();
                    for (String value : oldValues) {
                        if (Objects.equals(value, "NULL")) newValues.add(value);
                        else newValues.add("'" + value + "'");
                    }
                    int changedRows = QueryManager.addRow(tableName, columns, newValues);
                    if (changedRows > 0) textField.setText("Добавлено");
                    else textField.setText("Ошибка");
                    setResultTable();
                }
                case UPDATE -> {
                    List<String> oldValues1 = fieldListView1.getItems().stream().map(TextInputControl::getText).toList();
                    List<String> newValues1 = new ArrayList<>();
                    List<String> oldValues2 = fieldListView2.getItems().stream().map(TextInputControl::getText).toList();
                    List<String> newValues2 = new ArrayList<>();
                    for (int i = 0; i < oldValues1.size(); i++) {
                        if (Objects.equals(oldValues1.get(i), "NULL")) newValues1.add(oldValues1.get(i));
                        else newValues1.add("'" + oldValues1.get(i) + "'");
                        if (Objects.equals(oldValues2.get(i), "NULL")) newValues2.add(oldValues2.get(i));
                        else newValues2.add("'" + oldValues2.get(i) + "'");
                    }
                    int changedRows = QueryManager.updateData(tableName, columns, newValues1, newValues2);
                    if (changedRows > 0) textField.setText("Добавлено");
                    else textField.setText("Ошибка");
                    setResultTable();
                }
                case DELETE -> {
                    List<String> oldValues = fieldListView1.getItems().stream().map(TextInputControl::getText).toList();
                    List<String> newValues = new ArrayList<>();
                    for (String value : oldValues) {
                        if (Objects.equals(value, "NULL")) newValues.add(value);
                        else newValues.add("'" + value + "'");
                    }
                    int changedRows = QueryManager.deleteRows(tableName, columns, newValues);
                    if (changedRows > 0) textField.setText("Добавлено");
                    else textField.setText("Ошибка");
                    setResultTable();
                }
                default -> {}
            }
        }
        catch (DataBaseException e) {
            textField.setText("Ошибка");
            setResultTable();
        }
    }

    private void setResultTable() {
        resultTable.getColumns().clear();
        try {
            QueryManager.getTableColumns(tableName).forEach(el -> resultTable.getColumns().addAll(el));
            resultTable.setItems(QueryManager.getTableData(tableName));
        } catch (DataBaseException e) {
            resultTable.setItems(FXCollections.observableArrayList("Потеря соединения"));
        }
        resultTable.refresh();
    }
}
