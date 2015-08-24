package com.venesty.exchange.core.matchers;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.common.base.Predicate;
import com.venesty.exchange.model.Order;

/**
 * Simple predicate to match the exact price of an {@link Order}
 * 
 * @author vikash
 *
 */
public class ExactPriceMatcher implements Predicate<Order> {

    private BigDecimal price;
	
    public ExactPriceMatcher(BigDecimal price) {
        this.price = price.setScale(2, RoundingMode.UP);
	}
	
	public boolean apply(Order input) {
        return this.price.equals(input.getPrice());
	}

}
