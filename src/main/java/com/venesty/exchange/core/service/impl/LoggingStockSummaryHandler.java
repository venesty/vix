package com.venesty.exchange.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.venesty.exchange.core.service.StockSummaryHandler;
import com.venesty.exchange.model.Order;
import com.venesty.exchange.model.Order.Direction;

/**
 * Simple logging implementation of {@link StockSummaryHandler}
 * 
 * Uses log4j to output details received.
 * 
 * @author vikash
 *
 */
public class LoggingStockSummaryHandler implements StockSummaryHandler {
    private Logger LOG = LoggerFactory.getLogger(LoggingStockSummaryHandler.class);


    public void handleExecutedQuantityFor(Integer qty, String ric, String user) {
        LOG.info("Executed quantity for " + ric + ", " + user + " " + qty + "");
	}


    public void handleAverageExecutionPriceFor(Double executedPrice, String ric) {
        LOG.info("Average " + ric + " exec. price" + executedPrice + "");
    }

    public void handleOpenInterestFor(Integer totalQuantity, String ric, Direction direction, Double price) {
        LOG.info("Open " + ric + " " + direction + " " + totalQuantity + " @ " + price + "");
    }

    public void handleNewOrder(Order order) {
        String orderValue = order.toString();
        if (order.getExecutionPrice() != null) {
            orderValue = orderValue + " Matched!  Executed at " + order.getExecutionPrice();
        }
        LOG.info(orderValue);
    }


}
