package com.venesty.exchange.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import com.venesty.exchange.model.Order;
import com.venesty.exchange.model.Order.Direction;

public class OrderTest {
	
	@Test
	public void testNewOrder() {
		Order order = new Order("APP.L", 1000, 500.00, Direction.BUY, "user1");
		assertThat(order.getRic(), equalTo("APP.L"));
		assertThat(order.getQuantity(), equalTo(1000));
		assertThat(order.getPrice(), equalTo(500.00));
		assertThat(order.getDirection(), equalTo(Order.Direction.BUY));
		assertThat(order.getUser(), equalTo("user1"));
	}

}
