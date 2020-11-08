package com.keithcaff.stocksapi.exception;

public class StockServiceException extends RuntimeException {
    public StockServiceException(String message) {
        super(message);
    }

    public StockServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
