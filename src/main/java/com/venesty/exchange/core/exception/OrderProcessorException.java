package com.venesty.exchange.core.exception;

public class OrderProcessorException extends RuntimeException {

    public OrderProcessorException(String message) {
        super(message);
    }

    public OrderProcessorException(Throwable cause) {
        super(cause);
    }

    public OrderProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

}
