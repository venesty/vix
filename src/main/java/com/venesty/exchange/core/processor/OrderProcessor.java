package com.venesty.exchange.core.processor;

import com.venesty.exchange.core.exception.OrderProcessorException;
import com.venesty.exchange.model.Order;

/**
 * API to allow new orders to be added to the system.
 * 
 * @author vikash
 *
 */
public interface OrderProcessor {

	/**
	 * Adds new  order
	 * 
	 * @param order
	 */
    public void addNewOrder(Order order);

    /**
     * Start the processor. The processing of the newly added {@link Order}s will
     * only take effect after this has been called.
     * 
     */
    public void startProcessor();

    /**
     * Should be called to stop the OrderProcessor from running.
     * Should ensure that the backlog is cleared and no new orders can be placed.
     * 
     * @throws OrderProcessorException 
     */
    public void awaitCompletion() throws OrderProcessorException;

}
