package com.venesty.exchange.core.exception;

import com.venesty.exchange.core.processor.OrderProcessor;

/**
 * {@link RuntimeException} thrown by the {@link OrderProcessor}
 * @author vikash
 *
 */
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
