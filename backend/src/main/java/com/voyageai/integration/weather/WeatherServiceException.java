package com.voyageai.integration.weather;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when weather information cannot be retrieved from external provider.
 */
public class WeatherServiceException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    public WeatherServiceException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public WeatherServiceException(String message, Throwable cause, HttpStatus status, String errorCode) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
