package com.venesty.exchange.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
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
	
    public static Map<Double, Collection<Order>> getOpenInterestOrdersForVodl(Direction direction) {
        List<Order> orders = direction.equals(Direction.SELL) ? getOpenSellOrdersForVodl() : getOpenBuyOrdersForVodl();
        return Multimaps.index(orders, new Function<Order, Double>() {
            public Double apply(Order input) {
                return new Double(input.getPrice());
            }
        }).asMap();
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

}
