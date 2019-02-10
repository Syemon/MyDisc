package com.mydisc.MyDisc.exception;

public class NotFoundFolderException extends RuntimeException {
    public NotFoundFolderException(String message) {
        super(message);
    }

    public NotFoundFolderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundFolderException(Throwable cause) {
        super(cause);
    }
}
