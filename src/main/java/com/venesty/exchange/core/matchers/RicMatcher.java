package com.venesty.exchange.core.matchers;

import com.google.common.base.Predicate;
import com.venesty.exchange.model.Order;

/**
 * Simple predicate to match the RIC of an {@link Order}.
 * 
 * @author vikash
 *
 */
public class RicMatcher implements Predicate<Order> {

	private String ric;
	
	public RicMatcher(String ric) {
		this.ric = ric;
	}
	public boolean apply(Order order) {
		return this.ric.equalsIgnoreCase(order.getRic());
	}

}
