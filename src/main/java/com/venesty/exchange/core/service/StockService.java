/*
 * Copyright 2013 Deutsche Bank. All rights reserved.
 * 
 * This software is the confidential and proprietary information of Deutsche Bank. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in accordance with the terms of the license agreement you entered
 * into with Deutsche Bank.
 */

package com.venesty.exchange.core.service;

import java.util.Map;

public interface StockService<T> {
    
    public void process(T value);

    public Integer getExecutedQuantityFor(T value);

    public Double getAverageExecutedOrderFor(T value);

    public Map<Double, Integer> getOpenInterestFor(T value);

}
