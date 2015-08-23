package com.venesty.exchange.core.service;

import java.util.Map;

import com.venesty.exchange.model.Order;
import com.venesty.exchange.model.Order.Direction;

/**
 * Service interface that deals with an {@link Order}.
 * 
 * @author vikash
 *
 * @param <T> an Order or subclass of it.
 */
public interface StockService<T extends Order> {
    
	/**
	 * Call to process an {@link Order}
	 * 
	 * @param input order
	 */
    public void process(T input);

   
    /**
     * Get the executed quantity for a particular ric.
     * 
     * @param ric code to lookup.
     * @param user assigned who placed the Order for above ric
     * 
     * @return quantity
     */
    public Integer getExecutedQuantityFor(String ric, String byUser);

    /**
     * Provides the average executed price.
     * 
     * @param ric to lookup
     * 
     * @return average executed price as Double
     */
    public Double getAverageExecutedPriceFor(String ric);

    /**
     * Returns the total stock quantity for eith buy or sell at given price point for
     * a ric.
     *   
     * @param ric to lookup
     * 
     * @param direction -buying or selling stock
     * 
     * @return {@link Map} key providing the price and value then total quantity.
     */
    public Map<Double, Integer> getOpenInterestFor(String ric, Direction direction);

}
