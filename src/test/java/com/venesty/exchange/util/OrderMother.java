package com.venesty.exchange.util;

import java.math.BigDecimal;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.venesty.exchange.model.Order;
import com.venesty.exchange.model.Order.Direction;

public class OrderMother {
	
	public static List<Order> getOpenOrders() {
		return Lists.newArrayList(
				new Order("VOD.L", 1000, "101.00", Direction.SELL, "user1"), 
				new Order("VOD.L", 500, "100.00", Direction.SELL, "user2"), //m1
				new Order("VOD.L", 1000, "102.00", Direction.SELL, "user2"),
				new Order("VOD.L", 1000, "99.00", Direction.BUY, "user3"),
				new Order("VOD.L", 500, "101.00", Direction.BUY, "user1"), //m1
                        //
				new Order("APP.L", 1000, "101.00", Direction.SELL, "user3"),
				new Order("APP.L", 500, "100.00", Direction.SELL, "user1"), // m2
				new Order("APP.L", 1000, "102.00", Direction.SELL, "user4"),
				new Order("APP.L", 1000, "99.00", Direction.BUY, "user2"),
				new Order("APP.L", 500, "101.00", Direction.BUY, "user1")); // m2
	}
	
    public static List<Order> getUnMatchedOpenOrders() {
	     return Lists.newArrayList(
	                     new Order("VOD.L", 1000, "101.00", Direction.SELL, "user1"), //
                        new Order("VOD.L", 500, "100.00", Direction.SELL, "user2"), //
                        new Order("VOD.L", 1000, "102.00", Direction.SELL, "user2"), //
                        new Order("VOD.L", 1000, "97.00", Direction.BUY, "user3"), //
                        new Order("VOD.L", 1000, "96.00", Direction.BUY, "user4"), //
                        new Order("VOD.L", 1000, "99.00", Direction.BUY, "user5"), //
                        new Order("VOD.L", 1000, "98.00", Direction.BUY, "user6"), //
                        new Order("VOD.L", 500, "99.00", Direction.BUY, "user1"), //
                        new Order("APP.L", 1000, "101.00", Direction.SELL, "user3"), //
                        new Order("APP.L", 500, "100.00", Direction.SELL, "user1"), //
                        new Order("APP.L", 1000, "99.00", Direction.BUY, "user2"), //
                        new Order("APP.L", 500, "101.00", Direction.BUY, "user1"), //
                        new Order("APP.L", 1000, "101.00", Direction.SELL, "user4"), //
                        new Order("APP.L", 1000, "101.00", Direction.SELL, "user5"), //
                        new Order("APP.L", 1000, "101.00", Direction.SELL, "user5"), //
                        new Order("APP.L", 1500, "99", Direction.SELL, "user8"), //
                        new Order("APP.L", 1500, "96.00", Direction.SELL, "user9"), //
                        new Order("APP.L", 1500, "98.00", Direction.SELL, "user10"), //
                        new Order("APP.L", 1500, "100", Direction.SELL, "user11"), //
                        new Order("APP.L", 2000, "100.00", Direction.BUY, "user13"), //
                        new Order("APP.L", 2000, "100.00", Direction.BUY, "user14"), //
                        new Order("APP.L", 2000, "100.00", Direction.BUY, "user15"), //
                        new Order("APP.L", 2000, "100.00", Direction.BUY, "user16")); //
    }

	public static List<Order> getMatchingOrders() {
		return Lists.newArrayList(
				new Order("VOD.L", 1000, "101.00", Direction.SELL, "user1"),
				new Order("VOD.L", 1000, "101.00", Direction.BUY, "user2"),
				//
				new Order("VOD.L", 500, "100.00", Direction.SELL, "user2"),
				new Order("VOD.L", 500, "100.00", Direction.BUY, "user3"),
				
				new Order("VOD.L", 1000, "99.00", Direction.BUY, "user3"),
				new Order("VOD.L", 1000, "99.00", Direction.SELL, "user1"),
				//
				new Order("VOD.L", 500, "101.00", Direction.BUY, "user1"),
				new Order("VOD.L", 500, "101.00", Direction.SELL, "user2"),
				//
				new Order("VOD.L", 1000, "102.00", Direction.SELL, "user2"),
				new Order("VOD.L", 1000, "102.00", Direction.BUY, "user1"),
				//
				new Order("APP.L", 1000, "101.00", Direction.SELL, "user3"),
				new Order("APP.L", 1000, "101.00", Direction.BUY, "user4"),
				//
				new Order("APP.L", 500, "100.00", Direction.SELL, "user2"),
				new Order("APP.L", 500, "100.00", Direction.BUY, "user3"),
				//
				new Order("APP.L", 1000, "99.00", Direction.BUY, "user4"),
				new Order("APP.L", 1000, "99.00", Direction.SELL, "user2"),
				//
				new Order("APP.L", 500, "101.00", Direction.BUY, "user1"),
				new Order("APP.L", 500, "101.00", Direction.SELL, "user4"),
				//
				new Order("APP.L", 1000, "102.00", Direction.SELL, "user4"),
				new Order("APP.L", 1000, "102.00", Direction.BUY, "user5"));
		
	}
	
	public static List<Order> getInvalidOpenOrders() {
		return Lists.newArrayList(
				new Order(null, 1000, "101.00", Direction.SELL, "user1"),
				new Order("", 500, "100.00", Direction.SELL, "user2"),
				new Order(" ", 1000, "99.00", Direction.BUY, "user3"),
				new Order("VOD.L", null, "101.00", Direction.BUY, "user1"),
				new Order("VOD.L", 0, "102.00", Direction.SELL, "user2"),
				new Order("APP.L", -1000, "101.00", Direction.SELL, "user3"),
                        new Order("APP.L", 500, "0.00", Direction.SELL,
                        "user1"),
				new Order("APP.L", 1000, "0.0", Direction.BUY, "user2"),
				new Order("APP.L", 500, "-101.00", Direction.BUY, "user1"),
				new Order("APP.L", 1000, "102.00", null, "user4"),
				new Order("APP.L", 1000, "102.00", Direction.BUY, null),
				new Order("APP.L", 1000, "102.00", Direction.SELL, ""),
				new Order("APP.L", 1000, "102.00", Direction.BUY, " "));
		
	}
	
	public static List<Order> getExecutedOrdersFor(String ric, String user) {
	    List<Order> executedOrders = Lists.newArrayList(
	                    new Order(ric, 1000, "101.00", Direction.SELL, user),
	                    new Order(ric, 500, "100.00", Direction.SELL, user),
	                    new Order(ric, 1000, "99.00", Direction.BUY, user),
	                    new Order(ric, 500, "101.00", Direction.BUY, user),
	                    new Order(ric, 1000, "102.00", Direction.SELL, user),
	                    new Order(ric, 1000, "101.00", Direction.SELL, user));
	    
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
		
        BigDecimal sellPrice = new BigDecimal(100.00);

        BigDecimal buyPrice = new BigDecimal(101.00);

        BigDecimal sellPriceForUnmatch = new BigDecimal(300.00);
        BigDecimal buyPriceForUnmatch = new BigDecimal(10.00);

		//valid sell and buy
		for (int i=100; i<200; i++) {
            sellPrice = sellPrice.add(new BigDecimal(i));
            buyPrice = buyPrice.add(new BigDecimal(i));
            buyPriceForUnmatch = buyPriceForUnmatch.add(new BigDecimal(i));
            sellPriceForUnmatch = sellPriceForUnmatch.add(new BigDecimal(i));

            // 200 matching orders
            Order sellOrder = new Order("VOD.L", 100 * i, sellPrice, Direction.SELL, "user1");
            Order buyOrder = new Order("VOD.L", 100 * i, buyPrice, Direction.BUY, "user2");
            orders.add(sellOrder);
			orders.add(buyOrder);
			
            Order unmatchedBuyOrder = new Order("VOD.L", 100 * i, buyPriceForUnmatch, Direction.BUY, "user1");
            Order unmatchedSellOrder = new Order("VOD.L", 100 * i, sellPriceForUnmatch, Direction.BUY, "user2");
			orders.add(unmatchedBuyOrder);
            orders.add(unmatchedSellOrder);
		}
		
		// invalid buy and sell
		for (int i=100; i<225; i++) {
		    
			Order sellOrder = new Order("", 100 * i, sellPrice.add(new BigDecimal(i)), Direction.SELL, "user2");
			orders.add(sellOrder);
			
            Order buyOrder = new Order("APP.L", 100 * i, buyPrice.add(new BigDecimal(i)), Direction.BUY, "");
			orders.add(buyOrder);
			
			orders.add(null);
		}
			
		return orders;
				
	}

}
