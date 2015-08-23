package com.venesty.exchange.util;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.venesty.exchange.model.Order;
import com.venesty.exchange.model.Order.Direction;

public class OrderMother {
	
	public static List<Order> getOpenOrders() {
		return Lists.newArrayList(
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
	
	public static List<Order> getInvalidOpenOrders() {
		return Lists.newArrayList(
				new Order(null, 1000, 101.00, Direction.SELL, "user1"),
				new Order("", 500, 100.00, Direction.SELL, "user2"),
				new Order(" ", 1000, 99.00, Direction.BUY, "user3"),
				new Order("VOD.L", null, 101.00, Direction.BUY, "user1"),
				new Order("VOD.L", 0, 102.00, Direction.SELL, "user2"),
				new Order("APP.L", -1000, 101.00, Direction.SELL, "user3"),
				new Order("APP.L", 500, null, Direction.SELL, "user1"),
				new Order("APP.L", 1000, 0.0, Direction.BUY, "user2"),
				new Order("APP.L", 500, -101.00, Direction.BUY, "user1"),
				new Order("APP.L", 1000, 102.00, null, "user4"),
				new Order("APP.L", 1000, 102.00, Direction.BUY, null),
				new Order("APP.L", 1000, 102.00, Direction.SELL, ""),
				new Order("APP.L", 1000, 102.00, Direction.BUY, " "));
		
	}
	
    public static List<Order> getOpenBuyOrdersForVodl() {
	    return Lists.newArrayList(
	                    new Order("VOD.L", 1000, 101.00, Direction.BUY, "user3"),
	                    new Order("VOD.L", 1000, 102.00, Direction.BUY, "user1"),
	                    new Order("VOD.L", 500, 101.00, Direction.BUY, "user2"),
	                    new Order("VOD.L", 500, 102.00, Direction.BUY, "user1"),
	                    new Order("VOD.L", 1000, 100.00, Direction.BUY, "user4"));
	}
	
    public static List<Order> getOpenSellOrdersForVodl() {
        return Lists.newArrayList(
                        new Order("VOD.L", 1000, 101.00, Direction.SELL, "user1"),
                        new Order("VOD.L", 1000, 102.00, Direction.SELL, "user2"),
                        new Order("VOD.L", 500, 101.00, Direction.SELL, "user3"),
                        new Order("VOD.L", 500, 102.00, Direction.SELL, "user1"),
                        new Order("VOD.L", 1000, 100.00, Direction.SELL, "user2"));
    }

	public static List<Order> getExecutedOrders() {
		return Lists.transform(getOpenOrders(), new Function<Order, Order>() {
			public Order apply(Order input) {
				input.setExecutionPrice(input.getPrice());
				return input;
			}
		});
	}
	
	public static List<Order> getExecutedOrdersFor(String ric, String user) {
	    List<Order> executedOrders = Lists.newArrayList(
	                    new Order(ric, 1000, 101.00, Direction.SELL, user),
	                    new Order(ric, 500, 100.00, Direction.SELL, user),
	                    new Order(ric, 1000, 99.00, Direction.BUY, user),
	                    new Order(ric, 500, 101.00, Direction.BUY, user),
	                    new Order(ric, 1000, 102.00, Direction.SELL, user),
	                    new Order(ric, 1000, 101.00, Direction.SELL, user));
	    
	    return Lists.transform(executedOrders, new Function<Order, Order>() {
            public Order apply(Order input) {
                input.setExecutionPrice(input.getPrice());
                return input;
            }
	        
        });
    }
	
	/**
	 * Large list of orders.
	 * 100 sell and 100 buy orders with match. 100 umatched.
	 * 
	 * 75 invalid orders.
	 * 
	 * 
	 * 
	 * @return
	 */
	public static List<Order> getLargeValidAndInvalidOrders() {
		
		List<Order> orders = Lists.newArrayListWithCapacity(275);
		
		//valid sell and buy
		for (int i=100; i<200; i++) {
			Order sellOrder = new Order("VOD.L", 100 * i, 100.00 + i, Direction.SELL, "user1");
			orders.add(sellOrder);
			
			Order buyOrder = new Order("VOD.L", 100 * i, 101.00 + i, Direction.BUY, "user1");
			orders.add(buyOrder);
			
			Order unmatched = new Order("VOD.L", 100 * i, 100.00 + i, Direction.BUY, "user1");
			orders.add(unmatched);
		}
		
		// invalid buy and sell
		for (int i=100; i<225; i++) {
			Order sellOrder = new Order("", 100 * i, 100.00 + i, Direction.SELL, "user2");
			orders.add(sellOrder);
			
			Order buyOrder = new Order("APP.L", 100 * i, 101.00 + i, Direction.BUY, "");
			orders.add(buyOrder);
			
			orders.add(null);
		}
			
		return orders;
				
	}

}
