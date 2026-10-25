package com.voyageai.integration.ai;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an error occurs during communication with or processing of Google Gemini API.
 */
public class GeminiServiceException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    public GeminiServiceException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public GeminiServiceException(String message, Throwable cause, HttpStatus status, String errorCode) {
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
