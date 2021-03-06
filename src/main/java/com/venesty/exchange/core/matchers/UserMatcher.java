package com.venesty.exchange.core.matchers;

import com.google.common.base.Predicate;
import com.venesty.exchange.model.Order;

/**
 * Simple predicate to match the user of an {@link Order}.
 * 
 * @author vikash
 *
 */
public class UserMatcher implements Predicate<Order> {

	private String user;
	
	public UserMatcher(String user) {
		this.user = user;
	}
	public boolean apply(Order input) {
		return this.user.equals(input.getUser());
	}

}
