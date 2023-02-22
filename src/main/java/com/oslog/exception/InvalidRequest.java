package com.oslog.exception;

import lombok.Getter;

@Getter
public class InvalidRequest extends OslogException{
    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    public String getStatusCode() {
        return "400";
    }
}
