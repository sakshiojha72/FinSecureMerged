package com.ds.app.exception;

public class TrainingNotCompleteException extends RuntimeException {
    public TrainingNotCompleteException(String message) {
        super(message);
    }
}