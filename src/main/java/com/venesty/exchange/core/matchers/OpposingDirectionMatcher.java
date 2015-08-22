package com.venesty.exchange.core.matchers;

import com.google.common.base.Predicate;
import com.venesty.exchange.model.Order;

public class OpposingDirectionMatcher implements Predicate<Order> {

	private Order.Direction direction;
	
	public OpposingDirectionMatcher(Order.Direction direction) {
		this.direction = direction;
	}
	
	public boolean apply(Order input) {
		return !direction.equals(input.getDirection());
	}

}
