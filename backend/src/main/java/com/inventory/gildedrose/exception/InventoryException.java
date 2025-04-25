package com.inventory.gildedrose.exception;

public class InventoryException extends RuntimeException {
    private final ErrorType errorType;

    public enum ErrorType {
        FILE_IO_ERROR,
        JSON_PARSE_ERROR,
        VALIDATION_ERROR,
        GENERAL_ERROR
    }

    public InventoryException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public InventoryException(String message, Throwable cause, ErrorType errorType) {
        super(message, cause);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
