package com.oslog.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class OslogException extends RuntimeException {

    public final Map<String, String> validaton = new HashMap<>();

    public OslogException(String message) {
        super(message);
    }

    public OslogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract String getStatusCode();

    public void addValidation(String fieldName, String message) {
        validaton.put(fieldName, message);
    }
}
