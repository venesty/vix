package com.venesty.exchange.model;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.venesty.exchange.core.exception.OrderProcessorException;

/**
 * Basic validation for an {@link Order}.
 * 
 * @author vikash
 *
 */
public class OrderValidator {
	private static Logger LOG = LoggerFactory.getLogger(OrderValidator.class); 
	
	/**
	 * Validate an Order to ensure, it;s not null and ric, quantity, price, direction and user 
	 * properties are set.
	 * 
	 * @param order to be validated
	 * @throws OrderProcessorException as soon as a validation fails.
	 */
	public void validate(Order order) throws OrderProcessorException {
		if (order == null) {
			LOG.error("Order was null");
			throw new OrderProcessorException("Order cannot be null.");
		}
		
		if (StringUtils.isBlank(order.getRic())) {
			//TODO should also check for valid RIC patterns
			LOG.error("Invalid order Ric: ", order);
			throw new OrderProcessorException("Invalid order Ric: " + order.getRic());
		}
		
		if (order.getQuantity() == null || order.getQuantity() <= 0) {
			LOG.error("Invalid quantity for order", order);
			throw new OrderProcessorException("Invalid quantity for order: " + order.getQuantity());
		}
		
		if (order.getPrice() ==  null || order.getPrice() <= 0.0) {
			LOG.error("Invalid price set for order", order);
			throw new OrderProcessorException("Invalid price set for order: " + order.getPrice());
		}
		
		if (order.getDirection() == null) {
			LOG.error("Invalid direction for order", order);
			throw new OrderProcessorException("Invalid direction for order: " + order.getDirection());
		}
		
		if (StringUtils.isBlank(order.getUser())) {
			LOG.error("Invalid user for order", order);
			throw new OrderProcessorException("Invalid user for order: " + order.getUser());
		}
	}

}
