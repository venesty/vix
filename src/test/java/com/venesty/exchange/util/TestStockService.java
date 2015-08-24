package com.venesty.exchange.util;

import com.google.common.collect.Iterables;
import com.venesty.exchange.core.service.impl.StockServiceImpl;

public class TestStockService extends StockServiceImpl {
	public TestStockService() {
		super();
	}
    
	public int currentOpenOrdersSize() {
		return Iterables.size(getOpenOrders());
	}
	
	public int currentExecutedOrdersSize() {
		return Iterables.size(getExecutedOrders());
	}


}
