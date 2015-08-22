package com.venesty.exchange.core.exception;

public class ProcessException extends RuntimeException {

    public ProcessException(String message) {
        super(message);
    }

    public ProcessException(Throwable cause) {
        super(cause);
    }

}
