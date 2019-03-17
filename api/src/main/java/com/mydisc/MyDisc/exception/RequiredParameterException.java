package com.mydisc.MyDisc.exception;

public class RequiredParameterException extends RuntimeException {
    public RequiredParameterException(String message) {
        super(message);
    }

    public RequiredParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequiredParameterException(Throwable cause) {
        super(cause);
    }
}
