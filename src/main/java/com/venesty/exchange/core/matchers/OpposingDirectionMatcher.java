package com.venesty.exchange.core.matchers;

import com.google.common.base.Predicate;
import com.venesty.exchange.model.Order;
import com.venesty.exchange.model.Order.Direction;

/**
 * Simple predicate to match the {@link Direction} of an {@link Order}.
 * 
 * NB: We are matching opposing directions.
 * 
 * @author vikash
 *
 */
public class OpposingDirectionMatcher implements Predicate<Order> {

	private Order.Direction direction;
	
	public OpposingDirectionMatcher(Order.Direction direction) {
		this.direction = direction;
	}
	
	public boolean apply(Order input) {
		return !direction.equals(input.getDirection());
	}

}
