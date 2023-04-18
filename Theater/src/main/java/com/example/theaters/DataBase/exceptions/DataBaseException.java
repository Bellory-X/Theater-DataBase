package com.example.theaters.DataBase.exceptions;

public class DataBaseException extends RuntimeException {
    public DataBaseException(String message, Throwable e) {
        super(message, e);
    }
}
