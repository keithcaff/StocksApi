package com.keithcaff.stocksapi.exception;

public class StockConflictException extends RuntimeException {
    public StockConflictException(String message) {
        super(message);
    }

    public StockConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
