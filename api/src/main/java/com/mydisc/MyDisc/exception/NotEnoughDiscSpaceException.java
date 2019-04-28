package com.mydisc.MyDisc.exception;

public class NotEnoughDiscSpaceException extends RuntimeException {
    public NotEnoughDiscSpaceException(String message) {
        super(message);
    }

    public NotEnoughDiscSpaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughDiscSpaceException(Throwable cause) {
        super(cause);
    }
}
