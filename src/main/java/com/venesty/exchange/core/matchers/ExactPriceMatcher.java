package com.venesty.exchange.core.matchers;

import com.google.common.base.Predicate;
import com.venesty.exchange.model.Order;

public class ExactPriceMatcher implements Predicate<Order> {

	private Double price;
	
	public ExactPriceMatcher(Double price) {
		this.price = price;
	}
	
	public boolean apply(Order input) {
		return this.price.equals(input.getPrice());
	}

}