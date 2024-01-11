package com.emintufan.labreportingapp.exception;

public class UnauthorizedReportAccessException extends RuntimeException {
    public UnauthorizedReportAccessException(String message) {
        super(message);
    }

    public UnauthorizedReportAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
