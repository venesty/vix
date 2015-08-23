package com.venesty.exchange.core.exception;

public class ProcessorException extends RuntimeException {

    public ProcessorException(String message) {
        super(message);
    }

    public ProcessorException(String message, Throwable cause) {
        super(cause);
    }

}
