package com.keithcaff.stocksapi.exception;

public class StockRestClientException extends RuntimeException {
    public StockRestClientException(String message) {
        super(message);
    }

    public StockRestClientException(String message, Throwable cause) {
        super(message, cause);
    }
}