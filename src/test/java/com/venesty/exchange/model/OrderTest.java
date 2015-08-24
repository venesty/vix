package com.venesty.exchange.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import utils.PriceUtil;

import com.venesty.exchange.core.exception.OrderProcessorException;
import com.venesty.exchange.model.Order.Direction;

public class OrderTest {
	
	private OrderValidator validator = new OrderValidator();
	
	@Test
	public void testNewOrder() {
        Order order = new Order("APP.L", 1000, "500.00", Direction.BUY, "user1");
		assertThat(order.getRic(), equalTo("APP.L"));
		assertThat(order.getQuantity(), equalTo(1000));
        assertThat(order.getPrice(), equalTo(PriceUtil.roundUp("500.00")));
		assertThat(order.getDirection(), equalTo(Order.Direction.BUY));
		assertThat(order.getUser(), equalTo("user1"));
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithNull() throws OrderProcessorException {
		Order order = null;
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithNullRic() throws OrderProcessorException {
        Order order = new Order(null, 1000, "500.00", Direction.BUY, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithEmptyRic() throws OrderProcessorException {
        Order order = new Order("", 1000, "500.00", Direction.BUY, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithEmptySpaceRic() throws OrderProcessorException {
        Order order = new Order("  ", 1000, "500.00", Direction.BUY, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithNullQuantity() throws OrderProcessorException {
        Order order = new Order("APP.L", null, "500.00", Direction.BUY, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithZeroQuantity() throws OrderProcessorException {
        Order order = new Order("APP.L", 0, "500.00", Direction.BUY, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithNegativeQuantity() throws OrderProcessorException {
        Order order = new Order("APP.L", -100, "500.00", Direction.BUY, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithZeroPrice() throws OrderProcessorException {
        Order order = new Order("APP.L", 1000, "0.00", Direction.BUY, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithNegativePrice() throws OrderProcessorException {
        Order order = new Order("APP.L", 1000, "-500.00", Direction.BUY, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithNullDirection() throws OrderProcessorException {
        Order order = new Order("APP.L", 1000, "500.00", null, "user1");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test
	public void testNewOrderValidatorWithBuyDirection() throws OrderProcessorException {
        Order order = new Order("APP.L", 1000, "500.00", Direction.BUY, "user1");
		validator.validate(order);
		assertThat("No Exception was thrown", true);
	}
	
	@Test
	public void testNewOrderValidatorWithSellDirection() throws OrderProcessorException {
        Order order = new Order("APP.L", 1000, "500.00", Direction.SELL, "user1");
		validator.validate(order);
		assertThat("No Exception was thrown", true);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithNullUser() throws OrderProcessorException {
        Order order = new Order("APP.L", 1000, "500.00", Direction.BUY, null);
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithEmptyUser() throws OrderProcessorException {
        Order order = new Order("APP.L", 1000, "500.00", Direction.BUY, "");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test(expected = OrderProcessorException.class)
	public void testNewOrderValidatorWithEmptySpaceUser() throws OrderProcessorException {
        Order order = new Order("APP.L", 1000, "500.00", Direction.BUY, " ");
		validator.validate(order);
		assertThat("OrderProcessorExeption expected", false);
	}
	
	@Test
	public void testNewOrderValidatorWithValidOrder() throws OrderProcessorException {
        Order order = new Order("APP.L", 1000, "500.00", Direction.BUY, "user1");
		validator.validate(order);
		assertThat("No Exception was thrown", true);
	}

}
