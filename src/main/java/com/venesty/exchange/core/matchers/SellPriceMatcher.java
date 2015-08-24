package com.venesty.exchange.core.matchers;

import java.math.BigDecimal;

import com.google.common.base.Predicate;
import com.venesty.exchange.model.Order;
import com.venesty.exchange.model.Order.Direction;

/**
 * Simple predicate to match the sell price of an {@link Order}.
 * 
 * @author vikash
 *
 */
public class SellPriceMatcher implements Predicate<Order> {

    private BigDecimal price;
	
    public SellPriceMatcher(BigDecimal price) {
		this.price = price;
	}
	
	public boolean apply(Order input) {
		if (input.getDirection().equals(Direction.SELL)) {
            return input.getPrice().doubleValue() <= this.price.doubleValue();
		}
        return this.price.doubleValue() <= input.getPrice().doubleValue();
	}

}
