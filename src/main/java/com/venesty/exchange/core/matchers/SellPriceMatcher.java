package com.venesty.exchange.core.matchers;

import com.google.common.base.Predicate;
import com.venesty.exchange.model.Order;
import com.venesty.exchange.model.Order.Direction;

public class SellPriceMatcher implements Predicate<Order> {

	private Double price;
	
	public SellPriceMatcher(Double price) {
		this.price = price;
	}
	
	public boolean apply(Order input) {
		if (input.getDirection().equals(Direction.SELL)) {
			return input.getPrice() <= this.price;
		}
		return this.price <= input.getPrice();
	}

}
