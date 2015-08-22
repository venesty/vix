package com.venesty.exchange.core.matcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.venesty.exchange.core.matchers.ExactPriceMatcher;
import com.venesty.exchange.core.matchers.OpposingDirectionMatcher;
import com.venesty.exchange.core.matchers.QuantityMatcher;
import com.venesty.exchange.core.matchers.RicMatcher;
import com.venesty.exchange.core.matchers.SellPriceMatcher;
import com.venesty.exchange.core.matchers.UserMatcher;
import com.venesty.exchange.model.Order;
import com.venesty.exchange.model.Order.Direction;

public class PredicateTest {

	private List<Order> openOrders;

	@Before
	public void setUp() {
		
		openOrders = Lists.newArrayList(
				new Order("VOD.L", 1000, 101.00, Direction.SELL, "user1"),
				new Order("VOD.L", 500, 100.00, Direction.SELL, "user2"),
				new Order("VOD.L", 1000, 99.00, Direction.BUY, "user3"),
				new Order("VOD.L", 500, 101.00, Direction.BUY, "user1"),
				new Order("VOD.L", 1000, 102.00, Direction.SELL, "user2"),
				new Order("APP.L", 1000, 101.00, Direction.SELL, "user3"),
				new Order("APP.L", 500, 100.00, Direction.SELL, "user1"),
				new Order("APP.L", 1000, 99.00, Direction.BUY, "user2"),
				new Order("APP.L", 500, 101.00, Direction.BUY, "user1"),
				new Order("APP.L", 1000, 102.00, Direction.SELL, "user4"));
	}

	@Test
	public void testExactPriceMatch() {
		Iterable<Order> matches = Iterables.filter(openOrders, new ExactPriceMatcher(99.00));
		assertThat(matches, contains(openOrders.get(2), openOrders.get(7)));
	}

	@Test
	public void testOpposingDirectionMatcher() {
		Iterable<Order> matches = Iterables.filter(openOrders,  new OpposingDirectionMatcher(Direction.SELL));
		assertThat(matches, contains(openOrders.get(2), openOrders.get(3), openOrders.get(7), openOrders.get(8)));
	}

	@Test
	public void testQuantityMatcher() {
		Iterable<Order> matches = Iterables.filter(openOrders, new QuantityMatcher(500));
		assertThat(matches, contains(openOrders.get(1), openOrders.get(3), openOrders.get(6), openOrders.get(8)));
	}

	@Test
	public void testRicMatcher() {
		Iterable<Order> matches = Iterables.filter(openOrders, new RicMatcher("vod.l"));
		assertThat(matches, contains(openOrders.get(0), openOrders.get(1), openOrders.get(2), openOrders.get(3), openOrders.get(4)));
	}

	@Test
	public void testSellPriceMatcher() {
		Iterable<Order> matches = Iterables.filter(openOrders, new SellPriceMatcher(99.99));
		assertThat(matches, contains(openOrders.get(3), openOrders.get(8)));
	}
	
	@Test
	public void testUserMatcher() {
		Iterable<Order> matches = Iterables.filter(openOrders, new UserMatcher("user4"));
		assertThat(matches, contains(openOrders.get(9)));
	}
	
	@Test
	public void testMultipleMatchers() {
		
		Iterable<Order> matches = Iterables.filter(openOrders, Predicates.and(new RicMatcher("app.l"), 
				new OpposingDirectionMatcher(Direction.BUY), 
				new QuantityMatcher(1000),
				new SellPriceMatcher(101.10)));
		
		assertThat(matches, contains(openOrders.get(5)));
	}

}
