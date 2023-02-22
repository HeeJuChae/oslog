package com.oslog.exception;

public class PostNotFound extends OslogException{

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public String getStatusCode() {
        return "404";
    }
}
