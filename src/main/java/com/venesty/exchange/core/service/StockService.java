package com.venesty.exchange.core.service;

import java.util.Map;

public interface StockService<T> {
    
    public void process(T value);

    public Integer getExecutedQuantityFor(T value);

    public Double getAverageExecutedOrderFor(T value);

    public Map<Double, Integer> getOpenInterestFor(T value);

}
