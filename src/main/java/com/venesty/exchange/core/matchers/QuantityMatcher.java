package com.venesty.exchange.core.matchers;

import com.google.common.base.Predicate;
import com.venesty.exchange.model.Order;

public class QuantityMatcher implements Predicate<Order> {

	private Integer quantity;
	
	public QuantityMatcher(Integer quantity) {
		this.quantity = quantity;
	}
	public boolean apply(Order order) {
		return this.quantity.equals(order.getQuantity());
	}

}
