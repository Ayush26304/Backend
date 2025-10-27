package com.kartcom.catalog.exception;

public class StockUnavailableException extends RuntimeException {
    public StockUnavailableException(String message) {
        super(message);
    }
}

