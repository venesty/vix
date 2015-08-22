package com.venesty.exchange.core.service;

import com.venesty.exchange.model.Order;
import com.venesty.exchange.model.Order.Direction;

public interface StockSummaryHandler {
    
    public void handleNewOrder(Order order);

    public void handleExecutedQuantityFor(Integer qty, String ric, String user);

    public void handleAverageExecutionPriceFor(Double executedPrice, String ric);

    public void handleOpenInterestFor(Integer totalQuantity, String ric, Direction direction, Double price);

}
