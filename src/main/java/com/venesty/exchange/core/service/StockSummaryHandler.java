/*
 * Copyright 2013 Deutsche Bank. All rights reserved.
 * 
 * This software is the confidential and proprietary information of Deutsche Bank. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in accordance with the terms of the license agreement you entered
 * into with Deutsche Bank.
 */

package com.venesty.exchange.core.service;

import com.venesty.exchange.model.Order;
import com.venesty.exchange.model.Order.Direction;

public interface StockSummaryHandler {
    
    public void handleNewOrder(Order order);

    public void handleExecutedQuantityFor(Integer qty, String ric, String user);

    public void handleAverageExecutionPriceFor(Double executedPrice, String ric);

    public void handleOpenInterestFor(Integer totalQuantity, String ric, Direction direction, Double price);

}
