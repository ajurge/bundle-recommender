package com.product.bundle.recommender.errors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ErrorMessage {

    private Long timestamp = System.currentTimeMillis();
    private String status;
    private String error;
    private String exception;
    private String message;
    private String path;


    private List<String> errors;

    public ErrorMessage() {
    }

    public ErrorMessage(List<String> errors) {
        this.errors = errors;
    }

    public ErrorMessage(String error) {
        this(Collections.singletonList(error));
    }

    public ErrorMessage(String... errors) {
        this(Arrays.asList(errors));
    }

    public ErrorMessage(String status, String error, String message, String exception, String path, List<String> errors) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.exception = exception;
        this.path = path;
        this.errors = errors;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
