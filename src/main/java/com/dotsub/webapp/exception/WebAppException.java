package com.dotsub.webapp.exception;

public class WebAppException extends RuntimeException {

    private int code;

    public WebAppException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
