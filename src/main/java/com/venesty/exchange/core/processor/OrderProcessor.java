package com.venesty.exchange.core.processor;

import com.venesty.exchange.model.Order;

public interface OrderProcessor {

    public void addNewOrder(Order order);

    public void startProcessor();

    public void awaitCompletion();

}
