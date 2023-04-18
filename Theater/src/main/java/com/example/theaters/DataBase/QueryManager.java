package com.example.theaters.DataBase;

import com.example.theaters.DataBase.exceptions.DataBaseException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class QueryManager {
    private static ConnectionDB connectionDB;

    private QueryManager() {}

    public static void initialize(String userName, String password) {
        connectionDB = new ConnectionDB(userName, password);
    }

    public static void closeConnection() {
        connectionDB.close();
    }

    public static boolean isOpenConnection() {
        return connectionDB != null && connectionDB.isConnected();
    }

    public static List<String> getTableNames() {
        return connectionDB.getTableNames();
    }

    public static List<String> getColumnNames(String tableName) {
        return connectionDB.getColumnNames(tableName);
    }

    public static List<String> getQueryNames() {
        return connectionDB.getQueryNames();
    }

    public static List<TableColumn> getQueryColumns(String nameQuery, List<String> parameters) {
        return getColumns(connectionDB.getQueryInfo(nameQuery, parameters));
    }

    public static ObservableList<Object> getQueryData(String nameQuery, List<String> parameters) {
        return getData(connectionDB.getQueryInfo(nameQuery, parameters));
    }

    public static List<TableColumn> getTableColumns(String tableName) {
        return getColumns(connectionDB.getTableInfo(tableName));
    }

    public static ObservableList<Object> getTableData(String tableName) {
        return getData(connectionDB.getTableInfo(tableName));
    }

    public static List<String> getParametersToQuery(String nameQuery) {
        return connectionDB.getParametersToQuery(nameQuery);
    }

    public static int addRow(String tableName, List<String> columns, List<String> values) {
        return connectionDB.getResultInsertQuery(tableName, String.join(", ", columns), String.join(", ", values));
    }

    public static int updateData(String tableName, List<String> columns, List<String> newValues, List<String> oldValues) {
        StringJoiner setValue = new StringJoiner(", ");
        StringJoiner whereValue = new StringJoiner(" AND ");
        for (int i = 0; i < columns.size(); i++) {
            setValue.add(columns.get(i) + " = " + newValues.get(i));
            if (oldValues.get(i).equals("NULL")) whereValue.add(columns.get(i) + " IS " + oldValues.get(i));
            else whereValue.add(columns.get(i) + " = " + oldValues.get(i));
        }
        return connectionDB.getResultUpdateQuery(tableName, setValue.toString(), whereValue.toString());
    }

    public static int deleteRows(String tableName, List<String> columns, List<String> values) {
        StringJoiner whereValue = new StringJoiner(" AND ");
        for (int i = 0; i < columns.size(); i++) {
            if (values.get(i).equals("NULL")) whereValue.add(columns.get(i) + " IS NULL");
            else whereValue.add(columns.get(i) + " = " + values.get(i));
        }
        return connectionDB.getResultDeleteQuery(tableName, whereValue.toString());
    }

    private static List<TableColumn> getColumns(ResultSet resultSet) {
        try {
            ResultSetMetaData setMetaData = resultSet.getMetaData();
            List<TableColumn> columns = new ArrayList<>();
            for(int i = 0; i < setMetaData.getColumnCount(); i++) {
                columns.add(i, new TableColumn(setMetaData.getColumnName(i+1)));
                int finalI = i;
                columns.get(i).setCellValueFactory(
                        (Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>)
                                param -> new SimpleStringProperty(param.getValue().get(finalI).toString()));
            }
            return columns;
        }
        catch (SQLException e) {
            throw new DataBaseException("Query error", e);
        }
    }

    private static ObservableList<Object> getData(ResultSet resultSet) {
        try {
            ObservableList<Object> data = FXCollections.observableArrayList();
            while(resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i = 1 ; i <= resultSet.getMetaData().getColumnCount(); i++){
                    if (resultSet.getString(i) == null) row.add("NULL");
                    else row.add(resultSet.getString(i));
                }
                data.add(row);
            }
            return data;
        }
        catch (SQLException e) {
            throw new DataBaseException("Query error", e);
        }
    }
}
