package com.venesty.exchange.core.service;

import com.venesty.exchange.model.Order;
import com.venesty.exchange.model.Order.Direction;

/**
 * Simple API that delegates the handling of a newly added order.
 * 
 * Also handles Order summary and lets the implementation decide.
 * 
 * @author vikash
 *
 */
public interface StockSummaryHandler {
    
	/**
	 * New order added.
	 * 
	 * @param order
	 */
    public void handleNewOrder(Order order);

    /**
     * Executed quantity received for given ric and user.
     * 
     * @param qty
     * @param ric
     * @param user
     */
    public void handleExecutedQuantityFor(Integer qty, String ric, String user);

    /**
     * Average execution price received for given ric.
     * 
     * @param executedPrice
     * @param ric
     */
    public void handleAverageExecutionPriceFor(Double executedPrice, String ric);

    /**
     * Open interest info received for given ric, {@link Direction} for given price point.
     * 
     * @param totalQuantity
     * @param ric
     * @param direction
     * @param price
     */
    public void handleOpenInterestFor(Integer totalQuantity, String ric, Direction direction, Double price);

}
