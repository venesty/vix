package com.venesty.exchange.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import com.venesty.exchange.core.exception.OrderProcessorException;
import com.venesty.exchange.model.Order.Direction;

public class OrderTest {
	
	private OrderValidator validator = new OrderValidator();
	
	@Test
	public void testNewOrder() {
		Order order = new Order("APP.L", 1000, 500.00, Direction.BUY, "user1");
		assertThat(order.getRic(), equalTo("APP.L"));
		assertThat(order.getQuantity(), equalTo(1000));
		assertThat(order.getPrice(), equalTo(500.00));
		assertThat(order.getDirection(), equalTo(Order.Direction.BUY));
		assertThat(order.getUser(), equalTo("user1"));
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithNull() {
		Order order = null;
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithNullRic() {
		Order order = new Order(null, 1000, 500.00, Direction.BUY, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithEmptyRic() {
		Order order = new Order("", 1000, 500.00, Direction.BUY, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithEmptySpaceRic() {
		Order order = new Order("  ", 1000, 500.00, Direction.BUY, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithNullQuantity() {
		Order order = new Order("APP.L", null, 500.00, Direction.BUY, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithZeroQuantity() {
		Order order = new Order("APP.L", 0, 500.00, Direction.BUY, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithNegativeQuantity() {
		Order order = new Order("APP.L", -100, 500.00, Direction.BUY, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithNullPrice() {
		Order order = new Order("APP.L", 1000, null, Direction.BUY, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithZeroPrice() {
		Order order = new Order("APP.L", 1000, 0.00, Direction.BUY, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithNegativePrice() {
		Order order = new Order("APP.L", 1000, -500.00, Direction.BUY, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithNullDirection() {
		Order order = new Order("APP.L", 1000, 500.00, null, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test
	public void testNewOrderValidatorWithBuyDirection() {
		Order order = new Order("APP.L", 1000, 500.00, Direction.BUY, "user1");
		validator.validate(order);
		assertThat("No Exception was thrown", true);
	}
	
	@Test
	public void testNewOrderValidatorWithSellDirection() {
		Order order = new Order("APP.L", 1000, 500.00, Direction.SELL, "user1");
		validator.validate(order);
		assertThat("No Exception was thrown", true);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithNullUser() {
		Order order = new Order("APP.L", 1000, 500.00, Direction.BUY, null);
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithEmptyUser() {
		Order order = new Order("APP.L", 1000, 500.00, Direction.BUY, "");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithEmptySpaceUser() {
		Order order = new Order("APP.L", 1000, 500.00, Direction.BUY, " ");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test
	public void testNewOrderValidatorWithValidOrder() {
		Order order = new Order("APP.L", 1000, 500.00, Direction.BUY, "user1");
		validator.validate(order);
		assertThat("No Exception was thrown", true);
	}

}
